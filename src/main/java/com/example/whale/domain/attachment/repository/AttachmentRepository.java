package com.example.whale.domain.attachment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.whale.domain.attachment.entity.AttachmentEntity;

public interface AttachmentRepository extends JpaRepository<AttachmentEntity, Long> {

}
