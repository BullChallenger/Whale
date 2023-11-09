package com.example.whale.controller;

import com.example.whale.dto.LoginDTO.LoginRequestDTO;
import com.example.whale.dto.LoginDTO.LoginResponseDTO;
import com.example.whale.dto.ResponseDTO;
import com.example.whale.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController extends BaseController {

    private final LoginService loginService;

    @PostMapping(value = "/login")
    public ResponseDTO<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto) {
        LoginResponseDTO result = loginService.login(dto);
        return ResponseDTO.ok(result);
    }

}
