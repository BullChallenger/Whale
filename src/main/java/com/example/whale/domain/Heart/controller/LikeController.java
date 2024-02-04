package com.example.whale.domain.Heart.controller;

import com.example.whale.domain.Heart.dto.AddLikeRequestDTO;
import com.example.whale.domain.Heart.dto.SubLikeRequestDTO;
import com.example.whale.domain.Heart.service.HeartService;
import com.example.whale.domain.common.dto.ResponseDTO;
import com.example.whale.domain.Heart.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/hearts")
public class LikeController {

    private final LikeService likeService;
    private final HeartService heartService;

    @PostMapping(value = "/add")
    public ResponseDTO<String> addLikeInArticle(@RequestBody AddLikeRequestDTO dto) {
        likeService.addLikeInArticle(dto);
//        heartService.addHeart(dto);
        return ResponseDTO.ok("좋아요 성공");
    }

    @DeleteMapping(value = "/sub")
    public ResponseDTO<Void> subHeart(@RequestBody SubLikeRequestDTO dto) {
        likeService.subLikeInArticle(dto);
        return ResponseDTO.ok();
    }

}
