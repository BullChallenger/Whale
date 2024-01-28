package com.example.whale.controller;

import com.example.whale.dto.attachment.AttachmentToResource;
import java.io.IOException;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.util.MultiValueMap;
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

import com.example.whale.util.AuthenticationUtil;
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
    public ResponseEntity<MultiValueMap<String, HttpEntity<?>>> findArticleById(@PathVariable(value = "articleId") Long articleId) {
        GetArticleResponseDTO articleById = articleService.findArticleById(articleId);
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        List<AttachmentToResource> attachments = articleById.getAttachments();
        for (int i = 0; i < attachments.size(); i++) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION,
                    buildContentDispositionHeaderValue(attachments.get(i).getEncodedFileName(), attachments.get(i).getExtension())
            );
            headers.setContentType(MediaType.parseMediaType(attachments.get(i).getContentType() + ";charset=UTF-8"));
            builder.part("file" + i, new HttpEntity<>(attachments.get(i).getResource(), headers));
        }
        HttpHeaders jsonHeader = new HttpHeaders();
        jsonHeader.setContentType(MediaType.APPLICATION_JSON);
        builder.part("json", new HttpEntity<>(articleById, jsonHeader));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        return new ResponseEntity<>(builder.build(), headers, HttpStatus.OK);
    }

    private boolean isImage(String ext) {
        return ".png".equalsIgnoreCase(ext) || ".jpg".equalsIgnoreCase(ext);
    }

    private String buildContentDispositionHeaderValue(String encodedFileName, String ext) {
        if (isImage(ext)) {
            return "inline; filename=" + encodedFileName + ext;
        } else {
            return "attachment; filename=" + encodedFileName + ext;
        }
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
