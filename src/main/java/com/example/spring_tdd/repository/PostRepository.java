package com.example.spring_tdd.repository;

import com.example.spring_tdd.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<PostEntity, Long> {
}
