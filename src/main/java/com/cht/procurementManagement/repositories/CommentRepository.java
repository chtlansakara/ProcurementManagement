package com.cht.procurementManagement.repositories;

import com.cht.procurementManagement.entities.Comment;
import com.cht.procurementManagement.enums.ReviewType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByRequestId(Long requestId);

    List<Comment> findAllByRequestIdAndType(Long requestId, ReviewType reviewType);

}
