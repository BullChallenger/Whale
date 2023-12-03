package com.example.whale.controller;

import com.example.whale.constant.Role;
import com.example.whale.domain.UserEntity;
import com.example.whale.dto.SignUpDTO.SignUpRequestDTO;
import com.example.whale.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping(value = "/signUp")
    public ResponseEntity signUp(@RequestBody SignUpRequestDTO dto) {
        userRepository.save(UserEntity.of(dto.getEmail(),
                                          dto.getUsername(),
                                          dto.getNickname(),
                                          passwordEncoder.encode(dto.getPassword()),
                                          Role.USER));

        return ResponseEntity.ok("성공");
    }

    @GetMapping(value = "/find/{userId}")
    public ResponseEntity findUserById(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(userRepository.findById(userId));
    }
}
