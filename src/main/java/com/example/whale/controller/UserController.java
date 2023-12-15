package com.example.whale.controller;

import com.example.whale.dto.user.FindUserDTO.FindUserResponseDTO;
import com.example.whale.dto.ResponseDTO;
import com.example.whale.dto.user.SignUpDTO.SignUpRequestDTO;
import com.example.whale.dto.user.UpdateUserDTO.UpdateUserRequestDTO;
import com.example.whale.dto.user.UpdateUserDTO.UpdateUserResponseDTO;
import com.example.whale.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping(value = "/update")
    public ResponseDTO<UpdateUserResponseDTO> updateUserInfo(@RequestBody UpdateUserRequestDTO dto) {
        return ResponseDTO.ok(userService.updateUserInfo(dto));
    }

    @DeleteMapping(value = "/delete/{userId}")
    public ResponseDTO<Void> deleteUserById(@PathVariable("userId") Long userId) {
        userService.deleteUserById(userId);
        return ResponseDTO.ok();
    }

}
