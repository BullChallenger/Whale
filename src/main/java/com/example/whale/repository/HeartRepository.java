package com.example.whale.repository;

import com.example.whale.domain.HeartEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeartRepository extends JpaRepository<HeartEntity, Long> {

}
