package com.example.whale.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.whale.domain.ArticleEntity;
import com.example.whale.dto.ResponseDTO;
import com.example.whale.dto.article.CreateArticleDTO.CreateArticleRequestDTO;
import com.example.whale.dto.article.CreateArticleDTO.CreateArticleResponseDTO;
import com.example.whale.dto.article.GetArticlePageResponseDTO;
import com.example.whale.dto.article.GetArticleResponseDTO;
import com.example.whale.dto.article.UpdateArticleDTO.UpdateArticleRequestDTO;
import com.example.whale.dto.article.UpdateArticleDTO.UpdateArticleResponseDTO;
import com.example.whale.dto.user.AuthenticationUser;
import com.example.whale.service.ArticleService;
import com.example.whale.service.UploadService;
import com.example.whale.util.AuthenticationUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/articles")
public class ArticleController extends BaseController {

    private final ArticleService articleService;
    private final UploadService uploadService;

    @PostMapping(value = "/save")
    public ResponseDTO<CreateArticleResponseDTO> saveArticle(
            @RequestPart(value = "dto") CreateArticleRequestDTO dto,
            @RequestPart(value = "attachment", required = false) List<MultipartFile> attachments,
            Authentication authentication) throws IOException
    {
        AuthenticationUser principal = AuthenticationUtil.convertAuthentication(authentication);
        ArticleEntity article = articleService.saveArticle(principal.getId(), dto, attachments);
        if (attachments != null) {
            uploadService.uploadAttachmentInArticle(article, attachments);
        }
        return ResponseDTO.ok(CreateArticleResponseDTO.from(article));
    }

    @GetMapping(value = "/find/{articleId}")
    public ResponseDTO<GetArticleResponseDTO> findArticleById(@PathVariable(value = "articleId") Long articleId) {
        return ResponseDTO.ok(articleService.findArticleById(articleId));
    }

    @PatchMapping(value = "/update")
    public ResponseDTO<UpdateArticleResponseDTO> updateArticle(@RequestPart(value = "dto") UpdateArticleRequestDTO dto,
                                                               @RequestPart(value = "attachment", required = false) List<MultipartFile> attachments) throws IOException {
        return ResponseDTO.ok(articleService.updateArticle(dto, attachments));
    }

    @DeleteMapping(value = "/delete/{articleId}")
    public ResponseDTO<Void> deleteArticleById(@PathVariable(value = "articleId") Long articleId) {
        articleService.deleteArticleById(articleId);
        return ResponseDTO.ok();
    }

    @GetMapping(value = "/read")
    public ResponseDTO<Page<GetArticlePageResponseDTO>> readArticlePage(@RequestParam(required = false) Long lastArticleId,
                                                                        Pageable pageable) {
        return ResponseDTO.ok(articleService.readArticlePage(lastArticleId, pageable));
    }

}
