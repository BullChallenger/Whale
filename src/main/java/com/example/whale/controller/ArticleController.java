package com.example.whale.controller;

import com.example.whale.controller.util.AuthenticationUtil;
import com.example.whale.dto.ResponseDTO;
import com.example.whale.dto.article.CreateArticleDTO.CreateArticleRequestDTO;
import com.example.whale.dto.article.CreateArticleDTO.CreateArticleResponseDTO;
import com.example.whale.dto.article.GetArticlePageResponseDTO;
import com.example.whale.dto.article.GetArticleResponseDTO;
import com.example.whale.dto.article.UpdateArticleDTO.UpdateArticleRequestDTO;
import com.example.whale.dto.article.UpdateArticleDTO.UpdateArticleResponseDTO;
import com.example.whale.dto.user.AuthenticationUser;
import com.example.whale.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/articles")
public class ArticleController extends BaseController {

    private final ArticleService articleService;

    @PostMapping(value = "/save")
    public ResponseDTO<CreateArticleResponseDTO> saveArticle(
            @RequestPart(value = "dto") CreateArticleRequestDTO dto,
            @RequestPart(value = "attachment", required = false) List<MultipartFile> attachments,
            Authentication authentication) throws IOException
    {
        AuthenticationUser principal = AuthenticationUtil.convertAuthentication(authentication);
        return ResponseDTO.ok(articleService.saveArticle(principal.getId(), dto, attachments));
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
