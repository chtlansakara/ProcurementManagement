package com.cht.procurementManagement.services.Comment;

import com.cht.procurementManagement.dto.CommentDto;

import javax.sound.sampled.ReverbType;
import java.util.List;

public interface CommentService {

    CommentDto createComment(CommentDto commentDto);

    //get comments by request id
    List<CommentDto> getCommentsByRequestId(Long requestId);
}
