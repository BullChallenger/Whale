package com.example.whale.controller;

import com.example.whale.dto.ResponseDTO;
import com.example.whale.dto.heart.AddHeartDTO.AddHeartRequestDTO;
import com.example.whale.dto.heart.AddHeartDTO.AddHeartResponseDTO;
import com.example.whale.service.HeartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/hearts")
public class HeartController {

    private final HeartService heartService;

    @PostMapping(value = "/add")
    public ResponseDTO<AddHeartResponseDTO> addHeart(@RequestBody AddHeartRequestDTO dto) {
        return ResponseDTO.ok(heartService.addHeart(dto));
    }

    @DeleteMapping(value = "/sub/{heartId}")
    public ResponseDTO<Void> subHeart(@PathVariable Long heartId) {
        heartService.subHeart(heartId);
        return ResponseDTO.ok();
    }

}
