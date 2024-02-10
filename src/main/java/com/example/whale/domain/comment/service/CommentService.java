package com.example.whale.domain.comment.service;

import com.example.whale.domain.article.entity.ArticleEntity;
import com.example.whale.domain.comment.entity.CommentEntity;
import com.example.whale.domain.user.entity.UserEntity;
import com.example.whale.domain.comment.dto.CreateCommentDTO.CreateCommentRequestDTO;
import com.example.whale.domain.comment.dto.CreateCommentDTO.CreateCommentResponseDTO;
import com.example.whale.domain.comment.dto.GetCommentResponseDTO;
import com.example.whale.domain.comment.dto.GetOneCommentDTO;
import com.example.whale.domain.comment.dto.UpdateCommentRequestDTO;
import com.example.whale.domain.article.repository.ArticleRepository;
import com.example.whale.domain.comment.repository.CommentRepository;
import com.example.whale.domain.user.repository.UserRepository;
import com.example.whale.domain.comment.repository.querydsl.CustomCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    private final CustomCommentRepository customCommentRepository;

    public CreateCommentResponseDTO saveComment(Long writerId, CreateCommentRequestDTO dto) {
        Long parentCommentId = dto.getParentCommentId();
        Optional<CommentEntity> parentComment = Optional.empty();
        UserEntity writer = findWriter(writerId);
        ArticleEntity article = findArticle(dto.getArticleId());

        if (hasParentComment(parentCommentId)) {
            parentComment = commentRepository.findById(parentCommentId);
        }

        if (parentComment.isPresent()) {
            CommentEntity comment = CommentEntity.of(writer, dto.getContent(), parentComment.get(), article);
            article.getComments().addCommentInArticle(comment);
            return CreateCommentResponseDTO.from(commentRepository.save(comment));
        }

        CommentEntity rootComment = CommentEntity.of(writer, dto.getContent(), null, article);
        article.getComments().addCommentInArticle(rootComment);
        return CreateCommentResponseDTO.from(commentRepository.save(rootComment));
    }

    private UserEntity findWriter(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("User Not Found!")
        );
    }

    private ArticleEntity findArticle(Long articleId) {
        return articleRepository.findById(articleId).orElseThrow(
                () -> new EntityNotFoundException("Article Not Found!")
        );
    }

    private boolean hasParentComment(Long parentCommentId) {
        return parentCommentId != 0;
    }

    public GetCommentResponseDTO findCommentById(Long commentId) {
        return customCommentRepository.findCommentById(commentId);
    }

    public GetOneCommentDTO findOneCommentById(Long commentId) {
        return customCommentRepository.findOneCommentById(commentId);
    }

    public GetOneCommentDTO updateComment(UpdateCommentRequestDTO dto) {
        CommentEntity findComment = commentRepository.findById(dto.getCommentId()).orElseThrow(
                () -> new EntityNotFoundException("Comment Not Found!")
        );

        findComment.updateContent(dto.getContent());
        return GetOneCommentDTO.from(commentRepository.save(findComment));
    }

    public void deleteCommentById(Long commentId) {
        customCommentRepository.deleteCommentById(commentId);
    }

}
