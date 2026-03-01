package com.cht.procurementManagement.services.notification;

import com.cht.procurementManagement.dto.NotificationDto;
import com.cht.procurementManagement.entities.Notification;
import com.cht.procurementManagement.entities.Procurement;
import com.cht.procurementManagement.entities.Request;
import com.cht.procurementManagement.entities.User;
import com.cht.procurementManagement.enums.*;
import com.cht.procurementManagement.mappers.NotificationMapper;
import com.cht.procurementManagement.repositories.NotificationRepository;
import com.cht.procurementManagement.repositories.UserRepository;
import com.cht.procurementManagement.services.auth.AuthService;
import com.cht.procurementManagement.utils.SseEmitterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yaml.snakeyaml.comments.CommentType;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService{
    private static final Logger log = LoggerFactory.getLogger(SseEmitterRegistry.class);
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final SseEmitterRegistry sseEmitterRegistry;
    private final NotificationMapper notificationMapper;
    private final AuthService authService;
    public NotificationServiceImpl(NotificationRepository notificationRepository,
                                   UserRepository userRepository,
                                   SseEmitterRegistry sseEmitterRegistry,
                                   NotificationMapper notificationMapper, AuthService authService) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.sseEmitterRegistry = sseEmitterRegistry;

        this.notificationMapper = notificationMapper;
        this.authService = authService;
    }

    private void createAndSend(String message,
                               NotificationType type,
                               Long referenceId,
                               AuditEntityType referenceType,
                               List<Long> recipientUserIds){
        for(Long userId: recipientUserIds){
            Notification notification = new Notification();
            notification.setMessage(message);
            notification.setType(type);
            notification.setReferenceId(referenceId);
            notification.setReferenceType(referenceType);
            notification.setUserId(userId);
            notification.setRead(false);

            Notification saved = notificationRepository.save(notification);
            NotificationDto dto = notificationMapper.toDto(saved);

            //push live to the user
            sseEmitterRegistry.sendToUser(userId, dto);
        }
    }

    //get user ids by role and division - subdiv users
    private List<Long> getSubdivUserIds(Long subDivId){
        return userRepository
                .findByUserRoleAndSubdiv_Id(UserRole.SUBDIVUSER, subDivId)
                .stream()
                .map(User::getId)
                .toList();
    }
    //admindiv users
    private List<Long> getAdmindivUserIds(Long adminDivId){
        return userRepository
                .findByUserRoleAndAdmindiv_Id(UserRole.ADMINDIVUSER, adminDivId)
                .stream()
                .map(User::getId)
                .toList();
    }

    //supplies users
    private List<Long> getSuppliesUserIds(){
        return userRepository
                .findByUserRole(UserRole.SUPPLIESUSER)
                .stream()
                .map(User::getId)
                .toList();
    }


    //methods for controllers
    @Override
    public List<NotificationDto> getNotificationsByUserId(Long userId){


        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(notificationMapper::toDto)
                .toList();
    }
    @Override
        public void markRead(Long notificationId) {
            notificationRepository.findById(notificationId).ifPresent(n -> {
                n.setRead(true);
                notificationRepository.save(n);
            });
    }

    @Override
    public void markAllRead(Long userId){
        //get unread list
        List<Notification> unread = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .filter(n-> !n.getIsRead())
                .toList();

        //change value and save to db
        unread.forEach(n -> n.setRead(true));
        notificationRepository.saveAll(unread);

    }

    //method to be called upon trigger events

    //when sub-division submit a request
    @Override
    public void onRequestSubmitted(Request request){
        //get user role of createdBy
//        if(request.getCreatedBy().getUserRole().equals(UserRole.SUPPLIESUSER)){
//            //no need to notify anyone
//        }
        if(request.getCreatedBy().getUserRole().equals(UserRole.SUBDIVUSER)){
            //notify relevant admin-division
            //1. find admindiv users
            List<Long> admindivUserIds = getAdmindivUserIds(request.getAdmindiv().getId());
            //2. send notifications
            createAndSend(
                    "To be reviewed: " + request.getCreatedBy().getSubdiv().getName()
                    + " submitted a new procurement request.",
                    NotificationType.REQUEST_SUBMITTED,
                    request.getId(),
                    AuditEntityType.REQUEST,
                    admindivUserIds
            );
        }
        if(request.getCreatedBy().getUserRole().equals(UserRole.ADMINDIVUSER)){
            //notify supplies division
            //1. get supplies user ids
            List<Long> suppliesUserIds = getSuppliesUserIds();
            //2. send notifications
            createAndSend(
                    "To be reviewed: " + request.getCreatedBy().getAdmindiv().getName()
                    +" submitted a new procurement request #"+ request.getId(),
                    NotificationType.REQUEST_SUBMITTED,
                    request.getId(),
                    AuditEntityType.REQUEST,
                    suppliesUserIds
            );
        }
    }

    //when approves a request
    @Override
    public void onRequestApproval(Request request, ApprovalType approvalType){
        //check approval type

        //if approved by admin div
        if(approvalType.equals(ApprovalType.ADMIN_DIV)){
            //notify sub-division & supplies division
            List<Long> recipientIds = new ArrayList<>();
            //1. find subdiv users - who created the request
            recipientIds.addAll(getSubdivUserIds(request.getCreatedBy().getSubdiv().getId()));
            recipientIds.addAll(getSuppliesUserIds());
            createAndSend(
                    "Request #" +request.getId() + " has been approved by admin-division & pending supplies-review",
                    NotificationType.REQUEST_APPROVED_BY_ADMIN,
                    request.getId(),
                    AuditEntityType.REQUEST,
                    recipientIds
            );
        }

        //if approved by supplies
        if(approvalType.equals(ApprovalType.SUPPLIES)){
            //notify both sub-div and admin-div
            List<Long> recipientIds = new ArrayList<>();
            recipientIds.addAll(getSubdivUserIds(request.getCreatedBy().getSubdiv().getId()));
            recipientIds.addAll(getAdmindivUserIds(request.getAdmindiv().getId()));
            createAndSend(
                    "Procurement request with #" +request.getId()  +
                             "  has been approved by supplies-division",
                    NotificationType.REQUEST_APPROVED_BY_SUPPLIES,
                    request.getId(),
                    AuditEntityType.REQUEST,
                    recipientIds
            );
        }
    }


    //when rejecting a request
    @Override
    public void onRequestRejection(Request request, ReviewType type){
        //if by admin div - notify sub div ids
        if(type.equals(ReviewType.ADMIN_DIV)){
            List<Long> subdivUserIds = getSubdivUserIds(request.getCreatedBy().getSubdiv().getId());
            createAndSend(
                    "Request #" +request.getId() + " has been rejected by admin-division",
                    NotificationType.REQUEST_REJECTED_BY_ADMIN,
                    request.getId(),
                    AuditEntityType.REQUEST,
                    subdivUserIds
            );
        }

        //if by supplies - notify both sub div and admin div users
        if(type.equals(ReviewType.SUPPLIES)){
            //notify both sub-div and admin-div
            List<Long> recipientIds = new ArrayList<>();
            recipientIds.addAll(getSubdivUserIds(request.getCreatedBy().getSubdiv().getId()));
            recipientIds.addAll(getAdmindivUserIds(request.getAdmindiv().getId()));
            createAndSend(
                    "Procurement request with #" +request.getId()  +
                            "  has been rejected by supplies-division",
                    NotificationType.REQUEST_REJECTED_BY_SUPPLIES,
                    request.getId(),
                    AuditEntityType.REQUEST,
                    recipientIds
            );

        }
    }


    //when procurement created
    @Override
    public void onProcurementCreation(Procurement procurement, Request request){
        //notify both sub-div and admin-div
        List<Long> recipientIds = new ArrayList<>();
        recipientIds.addAll(getSubdivUserIds(request.getCreatedBy().getSubdiv().getId()));
        recipientIds.addAll(getAdmindivUserIds(request.getAdmindiv().getId()));
        createAndSend(
                        "A procurement (#"+ procurement.getId() + ") has been created for the request #"
                         +request.getId()  + " created by the sub-division  " +request.getCreatedBy().getSubdiv().getName(),
                NotificationType.PROCUREMENT_CREATED,
                procurement.getId(),
                AuditEntityType.PROCUREMENT,
                recipientIds
        );
    }


    //when procurement status is changed
    @Override
    public void onProcurementStatusChanged(Procurement procurement){
        String statusName = null;
        //get stage string
        String updatedStage = mapStage(procurement.getProcurementStage());
        //get status if has
        if(procurement.getStatus()!= null){
            statusName = procurement.getStatus().getName();
        }

        String updateString = statusName!= null ? updatedStage+ " - "+ statusName: updatedStage;


        //notify both sub-div and admin-div
        List<Long> recipientIds = new ArrayList<>();
        recipientIds.addAll(getSubdivUserIds(procurement.getRequest().getCreatedBy().getSubdiv().getId()));
        recipientIds.addAll(getAdmindivUserIds(procurement.getRequest().getCreatedBy().getAdmindiv().getId()));

        //notify sub-division who created the request
        createAndSend(
                "Procurement #" + procurement.getId() + " status updated to: "+
                        updateString,
                NotificationType.PROCUREMENT_STATUS_UPDATE,
                procurement.getId(),
                AuditEntityType.PROCUREMENT,
               recipientIds
        );
    }

    //delete related notifications when an entity is deleted
    @Transactional
    @Override
    public void deleteNotifications(AuditEntityType type,Long entityId){
        notificationRepository.deleteAllByReferenceTypeAndReferenceId(type, entityId);
    }

    //delete all notifications
    public void deleteAllNotifications(){
        notificationRepository.deleteAll();
    }


    //helper class method to get logged user's id
    private Long getLoggedUserId(){
        return authService.getLoggedUserDto().getId();
    }


    private String mapStage(ProcurementStage stage){
        if(stage.equals(ProcurementStage.PROCUREMENT_PROCESS_NOT_COMMENCED)) return "Not Commenced yet";
        if(stage.equals(ProcurementStage.PURCHASE_PROCESS_COMMENCED)) return "In Purchase Process";
        if(stage.equals(ProcurementStage.PURCHASE_ORDERS_ISSUED)) return "PO Issued";
        if(stage.equals(ProcurementStage.GOODS_RECEIVED)) return "Goods Received";
        if(stage.equals(ProcurementStage.PAID_AND_COMPLETED)) return "Paid and Completed";

        return "Undefined";
    }

}
