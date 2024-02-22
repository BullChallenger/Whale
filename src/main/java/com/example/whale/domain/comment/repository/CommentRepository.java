package com.example.whale.domain.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.whale.domain.comment.entity.CommentEntity;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

}
