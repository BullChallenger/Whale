package com.example.whale.domain.Like.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.whale.domain.Like.dto.AddLikeRequestDTO;
import com.example.whale.domain.Like.dto.SubLikeRequestDTO;
import com.example.whale.domain.Like.service.HeartService;
import com.example.whale.domain.Like.service.LikeService;
import com.example.whale.domain.common.dto.ResponseDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/hearts")
public class LikeController {

    private final LikeService likeService;
    private final HeartService heartService;

    @PostMapping(value = "/add")
    public ResponseDTO<String> addLikeInArticle(@RequestBody AddLikeRequestDTO dto) {
        likeService.addLikeInArticle(dto);
       // heartService.addHeart(dto);
        return ResponseDTO.ok("좋아요 성공");
    }

    @DeleteMapping(value = "/sub")
    public ResponseDTO<Void> subHeart(@RequestBody SubLikeRequestDTO dto) {
        likeService.subLikeInArticle(dto);
        return ResponseDTO.ok();
    }

}
