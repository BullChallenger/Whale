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

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/articles")
public class ArticleController extends BaseController {

    private final ArticleService articleService;

    @PostMapping(value = "/save")
    public ResponseDTO<CreateArticleResponseDTO> saveArticle(@RequestBody CreateArticleRequestDTO dto) {
        return ResponseDTO.ok(articleService.saveArticle(dto));
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
