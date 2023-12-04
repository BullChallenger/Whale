package com.example.whale.controller;

import com.example.whale.dto.FindUserDTO.FindUserResponseDTO;
import com.example.whale.dto.ResponseDTO;
import com.example.whale.dto.SignUpDTO.SignUpRequestDTO;
import com.example.whale.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/users")
public class UserController extends  BaseController {

    private final UserService userService;

    @PostMapping(value = "/signUp")
    public ResponseDTO<String> signUp(@RequestBody SignUpRequestDTO dto) {
        userService.signUp(dto);
        return ResponseDTO.ok("성공");
    }

    @GetMapping(value = "/find/{userId}")
    public ResponseDTO<FindUserResponseDTO> findUserById(@PathVariable("userId") Long userId) {
        return ResponseDTO.ok(userService.findUserById(userId));
    }

}
