package com.example.whale.domain.user.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.whale.domain.user.dto.FindUserDTO.FindUserResponseDTO;
import com.example.whale.domain.user.dto.SignUpDTO.SignUpRequestDTO;
import com.example.whale.domain.user.dto.UpdateUserDTO.UpdateUserRequestDTO;
import com.example.whale.domain.user.dto.UpdateUserDTO.UpdateUserResponseDTO;
import com.example.whale.domain.user.entity.UserEntity;
import com.example.whale.domain.user.repository.UserRepository;
import com.example.whale.global.constant.Role;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp(SignUpRequestDTO dto) {
            userRepository.save(UserEntity.of(
                    dto.getEmail(),
                    dto.getUsername(),
                    dto.getNickname(),
                    passwordEncoder.encode(dto.getPassword()),
                    List.of(Role.USER)
                )
            );
    }

    public FindUserResponseDTO findUserById(Long userId) {
        UserEntity findUser = findUserByUserId(userId);

        return FindUserResponseDTO.from(findUser);
    }

    public void deleteUserById(Long userId) {
        UserEntity findUser = findUserByUserId(userId);
        findUser.setIsDeleted(true);
        userRepository.save(findUser);
    }

    public UpdateUserResponseDTO updateUserInfo(Long userId, UpdateUserRequestDTO dto) {
        UserEntity findUser = findUserByUserId(userId);

        if (dto.getEmail() != null) {
            findUser.updateEmail(dto.getEmail());
        }

        if (dto.getUsername() != null) {
            findUser.updateUsername(dto.getUsername());
        }

        if (dto.getNickname() != null) {
            findUser.updateNickname(dto.getNickname());
        }

        UserEntity updatedUser = userRepository.save(findUser);
        return UpdateUserResponseDTO.from(updatedUser);
    }

    private UserEntity findUserByUserId(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("해당 유저를 찾을 수 없습니다.")
        );
    }

}
