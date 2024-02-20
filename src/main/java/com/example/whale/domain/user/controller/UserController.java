package com.example.whale.domain.user.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.whale.domain.common.controller.BaseController;
import com.example.whale.domain.common.dto.ResponseDTO;
import com.example.whale.domain.user.dto.FindUserDTO.FindUserResponseDTO;
import com.example.whale.domain.user.dto.SignUpDTO.SignUpRequestDTO;
import com.example.whale.domain.user.dto.UpdateUserDTO.UpdateUserRequestDTO;
import com.example.whale.domain.user.dto.UpdateUserDTO.UpdateUserResponseDTO;
import com.example.whale.domain.user.model.AuthenticationUser;
import com.example.whale.domain.user.service.UserService;
import com.example.whale.global.util.AuthenticationUtil;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Api(tags = "UserController")
@RequestMapping(value = "/api/users")
public class UserController extends BaseController {

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

    @PatchMapping(value = "/update")
    public ResponseDTO<UpdateUserResponseDTO> updateUserInfo(@RequestBody UpdateUserRequestDTO dto,
                                                             Authentication authentication) {
        AuthenticationUser principal = AuthenticationUtil.convertAuthentication(authentication);
        return ResponseDTO.ok(userService.updateUserInfo(principal.getId(), dto));
    }

    @DeleteMapping(value = "/delete/{userId}")
    public ResponseDTO<Void> deleteUserById(@PathVariable("userId") Long userId) {
        userService.deleteUserById(userId);
        return ResponseDTO.ok();
    }

}
