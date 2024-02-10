package com.example.whale.domain.article.entity;

import com.example.whale.domain.attachment.entity.AttachmentEntity;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AttachmentCollection {

    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY)
    private List<AttachmentEntity> attachments = new ArrayList<>();

    public void addAttachmentsInArticle(List<AttachmentEntity> attachments) {
        this.attachments.addAll(attachments);
    }

    public List<AttachmentEntity> attachments() {
        return attachments;
    }
}