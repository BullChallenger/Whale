package com.example.whale.domain.shop.entity;

import com.example.whale.domain.common.entity.PersistableWrapper;
import com.example.whale.domain.user.entity.UserEntity;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SellerEntity extends PersistableWrapper {

    @Id
    private String id; // ${userId}_${shopId}

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private ShopEntity shop;

    @Builder
    public SellerEntity(UserEntity user, ShopEntity shop) {
        this.id = generateSellerId(user.getId(), shop.getShopId());
        this.user = user;
        this.shop = shop;
    }

    public static SellerEntity of(UserEntity user, ShopEntity shop) {
        return SellerEntity.builder()
                .user(user)
                .shop(shop)
                .build();
    }

    private String generateSellerId(Long userId, Long shopId) {
        return userId + "_" + shopId;
    }

}
