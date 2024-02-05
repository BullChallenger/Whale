package com.example.whale.domain.article.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.whale.domain.article.entity.ArticleEntity;

public interface ArticleRepository extends JpaRepository<ArticleEntity, Long> {

}
