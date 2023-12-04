package com.example.whale.service;

import com.example.whale.constant.Role;
import com.example.whale.domain.UserEntity;
import com.example.whale.dto.FindUserDTO.FindUserResponseDTO;
import com.example.whale.dto.SignUpDTO.SignUpRequestDTO;
import com.example.whale.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.sql.SQLDataException;
import java.sql.SQLException;

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
        UserEntity findUser = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("해당 유저를 찾을 수 없습니다.")
        );

        return FindUserResponseDTO.from(findUser);
    }

}
