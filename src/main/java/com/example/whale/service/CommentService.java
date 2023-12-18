package com.example.whale.service;

import com.example.whale.domain.ArticleEntity;
import com.example.whale.domain.CommentEntity;
import com.example.whale.domain.UserEntity;
import com.example.whale.dto.comment.CreateCommentDTO.CreateCommentRequestDTO;
import com.example.whale.dto.comment.CreateCommentDTO.CreateCommentResponseDTO;
import com.example.whale.dto.comment.GetCommentResponseDTO;
import com.example.whale.dto.comment.GetOneCommentDTO;
import com.example.whale.dto.comment.UpdateCommentRequestDTO;
import com.example.whale.repository.ArticleRepository;
import com.example.whale.repository.CommentRepository;
import com.example.whale.repository.UserRepository;
import com.example.whale.repository.querydsl.CustomCommentRepository;
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

    public CreateCommentResponseDTO saveComment(CreateCommentRequestDTO dto) {
        Long parentCommentId = dto.getParentCommentId();
        Optional<CommentEntity> parentComment = Optional.empty();
        UserEntity writer = findWriter(dto.getWriterId());
        ArticleEntity article = findArticle(dto.getArticleId());

        if (hasParentComment(parentCommentId)) {
            parentComment = commentRepository.findById(parentCommentId);
        }

        if (parentComment.isPresent()) {
            CommentEntity comment = CommentEntity.of(writer, dto.getContent(), parentComment.get(), article);
            article.addCommentInArticle(comment);
            return CreateCommentResponseDTO.from(commentRepository.save(comment));
        }

        CommentEntity rootComment = CommentEntity.of(writer, dto.getContent(), null, article);
        article.addCommentInArticle(rootComment);
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
