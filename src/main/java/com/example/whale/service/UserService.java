package com.example.whale.service;

import com.example.whale.constant.Role;
import com.example.whale.domain.UserEntity;
import com.example.whale.dto.user.FindUserDTO.FindUserResponseDTO;
import com.example.whale.dto.user.SignUpDTO.SignUpRequestDTO;
import com.example.whale.dto.user.UpdateUserDTO.UpdateUserRequestDTO;
import com.example.whale.dto.user.UpdateUserDTO.UpdateUserResponseDTO;
import com.example.whale.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp(SignUpRequestDTO dto) {
            userRepository.save(UserEntity.of(dto.getEmail(),
                    dto.getUsername(),
                    dto.getNickname(),
                    passwordEncoder.encode(dto.getPassword()),
                    Role.USER));
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

    public UpdateUserResponseDTO updateUserInfo(UpdateUserRequestDTO dto) {
        UserEntity findUser = findUserByUserId(dto.getUserId());

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
