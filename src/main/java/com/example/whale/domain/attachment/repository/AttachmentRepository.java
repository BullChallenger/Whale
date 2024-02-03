package com.example.whale.domain.attachment.repository;

import com.example.whale.domain.attachment.entity.AttachmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<AttachmentEntity, Long> {

}
