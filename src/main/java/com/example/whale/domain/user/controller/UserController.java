package com.example.whale.domain.user.controller;

import com.example.whale.domain.common.controller.BaseController;
import com.example.whale.global.util.AuthenticationUtil;
import com.example.whale.domain.common.dto.ResponseDTO;
import com.example.whale.domain.user.model.AuthenticationUser;
import com.example.whale.domain.user.dto.FindUserDTO.FindUserResponseDTO;
import com.example.whale.domain.user.dto.SignUpDTO.SignUpRequestDTO;
import com.example.whale.domain.user.dto.UpdateUserDTO.UpdateUserRequestDTO;
import com.example.whale.domain.user.dto.UpdateUserDTO.UpdateUserResponseDTO;
import com.example.whale.domain.user.service.UserService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
