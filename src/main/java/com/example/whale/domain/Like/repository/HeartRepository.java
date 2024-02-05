package com.example.whale.domain.Like.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.whale.domain.Like.entity.LikeEntity;

public interface HeartRepository extends JpaRepository<LikeEntity, Long> {

}
