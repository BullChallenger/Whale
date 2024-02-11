package com.example.whale.domain.shop.service;

import com.example.whale.domain.shop.entity.SellerEntity;
import com.example.whale.domain.shop.entity.ShopEntity;
import com.example.whale.domain.shop.repository.SellerRepository;
import com.example.whale.domain.shop.repository.ShopRepository;
import com.example.whale.domain.user.entity.UserEntity;
import com.example.whale.domain.user.repository.UserRepository;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final SellerRepository sellerRepository;
    private final UserRepository userRepository;
    private final ShopRepository shopRepository;

    public void createSeller(Long userId, Long shopId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("해당 유저를 찾을 수 없습니다.")
        );
        ShopEntity shop = shopRepository.findById(shopId).orElseThrow(
                () -> new EntityNotFoundException("해당 상점에 대한 정보를 찾을 수 없습니다.")
        );

        SellerEntity seller = SellerEntity.of(user, shop);
    }

}
