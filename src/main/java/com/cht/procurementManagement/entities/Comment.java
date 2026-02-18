package com.cht.procurementManagement.entities;

import com.cht.procurementManagement.dto.CommentDto;
import com.cht.procurementManagement.enums.ReviewType;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    @Enumerated(EnumType.STRING)
    private ReviewType type;
    private String authorizedBy;
    private Date createdDate;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User createdBy;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "request_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Request request;

    //get dto method
    public CommentDto getCommentDto() {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(id);
        commentDto.setContent(content);
        commentDto.setType(type);
        commentDto.setAuthorizedBy(authorizedBy);
        commentDto.setCreatedDate(createdDate);
        //user details
        if (createdBy != null) {
            commentDto.setCreatedByUserId(createdBy.getId());
            commentDto.setCreatedByUserEmail(createdBy.getEmail());
            commentDto.setCreatedbyUsername(createdBy.getName());
            commentDto.setCreatedByUserEmployeeId(createdBy.getEmployeeId());
            commentDto.setUserRoleCreatedBy(createdBy.getUserRole());
            commentDto.setSubdivCreatedBy(createdBy.getSubdiv().getName());
            commentDto.setSubdivCodeCreatedBy(createdBy.getSubdiv().getCode());
            commentDto.setAdmindivCreatedBy(createdBy.getAdmindiv().getName());
            commentDto.setAdmindivCodeCreatedBy(createdBy.getAdmindiv().getCode());
        }
        //request details
        if (request != null) {
            commentDto.setRequestId(request.getId());
            commentDto.setRequestTitle(request.getTitle());
            commentDto.setRequestSubdivIdList(
                    request.getSubdivList()
                            .stream()
                            .map(Subdiv::getId)
                            .toList());
            commentDto.setRequestSubdivNameList(
                    request.getSubdivList()
                            .stream()
                            .map(Subdiv::getName)
                            .toList());
            commentDto.setRequestSubdivCodeList(
                    request.getSubdivList()
                            .stream()
                            .map(Subdiv::getCode)
                            .toList());
        }

        return commentDto;
    }




    //get-set methods

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ReviewType getType() {
        return type;
    }

    public void setType(ReviewType type) {
        this.type = type;
    }

    public String getAuthorizedBy() {
        return authorizedBy;
    }

    public void setAuthorizedBy(String authorizedBy) {
        this.authorizedBy = authorizedBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }
}
