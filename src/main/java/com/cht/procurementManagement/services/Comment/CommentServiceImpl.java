package com.cht.procurementManagement.services.Comment;

import com.cht.procurementManagement.dto.CommentDto;
import com.cht.procurementManagement.entities.Comment;
import com.cht.procurementManagement.entities.Request;
import com.cht.procurementManagement.entities.User;
import com.cht.procurementManagement.repositories.CommentRepository;
import com.cht.procurementManagement.repositories.RequestRepository;
import com.cht.procurementManagement.repositories.UserRepository;
import com.cht.procurementManagement.services.auth.AuthService;
import com.cht.procurementManagement.services.notification.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements  CommentService{

    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final CommentRepository commentRepository;
    //to set user created by
    private final AuthService authService;
    private final NotificationService notificationService;

    public CommentServiceImpl(UserRepository userRepository,
                              RequestRepository requestRepository,
                              CommentRepository commentRepository,
                              AuthService authService, NotificationService notificationService) {
        this.userRepository = userRepository;
        this.requestRepository = requestRepository;
        this.commentRepository = commentRepository;
        this.authService = authService;
        this.notificationService = notificationService;
    }

    //create comment
    @Override
    public CommentDto createComment(CommentDto commentDto) {
        //setting the createdBy user & created date only
        //set type & request id before sending here
        Long loggedUserId = authService.getLoggedUserDto().getId();

        //finding the User Object from db
        User userCreatedBy = userRepository.findById(loggedUserId)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        //finding the relevant request from db from request-id
        Optional<Request> optionalRequest = requestRepository.findById(commentDto.getRequestId());
        if(optionalRequest.isPresent()){
            //create comment & set details
            Comment comment = new Comment();
            comment.setContent(commentDto.getContent());
            comment.setType(commentDto.getType());
            comment.setAuthorizedBy(commentDto.getAuthorizedBy());
            //setting current time
            comment.setCreatedDate(new Date());
            //setting user
            comment.setCreatedBy(userCreatedBy);
            //setting request
            comment.setRequest(optionalRequest.get());

            //send notification
            notificationService.onRequestRejection(optionalRequest.get(), commentDto.getType());

            //save to db & return as dto
            return commentRepository.save(comment).getCommentDto();
        }else{
            throw new EntityNotFoundException("Request not found!");
        }
    }

    @Override
    public List<CommentDto> getCommentsByRequestId(Long requestId) {
        return commentRepository.findAllByRequestId(requestId)
                .stream()
                .sorted(Comparator.comparing(Comment::getCreatedDate))
                .map(Comment::getCommentDto)
                .collect(Collectors.toList());
    }
}
