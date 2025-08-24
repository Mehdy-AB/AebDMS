package com.Aeb.AebDMS.app.user.repository;

import com.Aeb.AebDMS.app.user.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // Add custom query methods here
}
