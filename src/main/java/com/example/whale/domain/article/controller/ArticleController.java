package com.example.whale.domain.article.controller;

import com.example.whale.domain.article.dto.GetArticleResponseConvertDTO;
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

import com.example.whale.domain.article.dto.CreateArticleDTO.CreateArticleRequestDTO;
import com.example.whale.domain.article.dto.CreateArticleDTO.CreateArticleResponseDTO;
import com.example.whale.domain.article.dto.GetArticlePageResponseDTO;
import com.example.whale.domain.article.dto.UpdateArticleDTO.UpdateArticleRequestDTO;
import com.example.whale.domain.article.dto.UpdateArticleDTO.UpdateArticleResponseDTO;
import com.example.whale.domain.article.entity.ArticleEntity;
import com.example.whale.domain.article.repository.ArticleRepository;
import com.example.whale.domain.article.service.ArticleService;
import com.example.whale.domain.article.service.ReadArticleServiceFacade;
import com.example.whale.domain.attachment.service.UploadService;
import com.example.whale.domain.common.controller.BaseController;
import com.example.whale.domain.common.dto.ResponseDTO;
import com.example.whale.domain.user.model.AuthenticationUser;
import com.example.whale.global.util.AuthenticationUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/articles")
public class ArticleController extends BaseController {

    private final ArticleService articleService;
    private final ReadArticleServiceFacade articleServiceFacade;

    @PostMapping(value = "/save")
    public ResponseDTO<CreateArticleResponseDTO> saveArticle(
            @RequestPart(value = "dto") CreateArticleRequestDTO dto,
            @RequestPart(value = "attachment", required = false) List<MultipartFile> attachments,
            Authentication authentication) throws IOException
    {
        AuthenticationUser principal = AuthenticationUtil.convertAuthentication(authentication);
        return ResponseDTO.ok(CreateArticleResponseDTO.from(articleService.saveArticle(principal.getId(), dto, attachments)));
    }

    @GetMapping(value = "/find/{articleId}")
    public ResponseDTO<GetArticleResponseConvertDTO> findArticleById(@PathVariable(value = "articleId") Long articleId) {
        return ResponseDTO.ok(articleServiceFacade.readArticleByIdV2(articleId));
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
        return ResponseDTO.ok(articleServiceFacade.readArticlePage(lastArticleId, pageable));
    }

}
