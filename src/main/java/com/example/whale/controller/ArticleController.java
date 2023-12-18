package com.example.whale.controller;

import com.example.whale.dto.ResponseDTO;
import com.example.whale.dto.article.CreateArticleDTO.CreateArticleRequestDTO;
import com.example.whale.dto.article.CreateArticleDTO.CreateArticleResponseDTO;
import com.example.whale.dto.article.GetArticleResponseDTO;
import com.example.whale.dto.article.UpdateArticleDTO.UpdateArticleRequestDTO;
import com.example.whale.dto.article.UpdateArticleDTO.UpdateArticleResponseDTO;
import com.example.whale.service.ArticleService;
import lombok.RequiredArgsConstructor;
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
            @RequestPart(value = "attachment", required = false) List<MultipartFile> attachments) throws IOException {
        return ResponseDTO.ok(articleService.saveArticle(dto, attachments));
    }

    @GetMapping(value = "/find/{articleId}")
    public ResponseDTO<GetArticleResponseDTO> findArticleById(@PathVariable(value = "articleId") Long articleId) {
        return ResponseDTO.ok(articleService.findArticleById(articleId));
    }

    @PatchMapping(value = "/update")
    public ResponseDTO<UpdateArticleResponseDTO> updateArticle(@RequestBody UpdateArticleRequestDTO dto) {
        return ResponseDTO.ok(articleService.updateArticle(dto));
    }

    @DeleteMapping(value = "/delete/{articleId}")
    public ResponseDTO<Void> deleteArticleById(@PathVariable(value = "articleId") Long articleId) {
        articleService.deleteArticleById(articleId);
        return ResponseDTO.ok();
    }

}
