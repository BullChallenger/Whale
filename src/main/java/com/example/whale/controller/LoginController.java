package com.example.whale.controller;

import com.example.whale.dto.user.LoginDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class LoginController {

    @PostMapping(value = "/login")
    public void login(@RequestBody LoginDTO.LoginRequestDTO dto) {

    }

}
