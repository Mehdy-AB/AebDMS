package com.Aeb.AebDMS.app.user.service;

import com.Aeb.AebDMS.app.user.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ICommentService {
    
    Comment saveComment(Comment comment);
    
    List<Comment> findAll();
    
    Page<Comment> findAll(Pageable pageable);
    
    Optional<Comment> findById(Long id);
    
    Comment updateComment(Comment comment);
    
    void deleteById(Long id);
}
