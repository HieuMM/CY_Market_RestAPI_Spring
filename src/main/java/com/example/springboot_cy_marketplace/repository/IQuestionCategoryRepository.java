package com.example.springboot_cy_marketplace.repository;

import com.example.springboot_cy_marketplace.dto.QuestionDTO;
import com.example.springboot_cy_marketplace.entity.QuestionCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IQuestionCategoryRepository extends JpaRepository<QuestionCategoryEntity, Long> {
    @Query(nativeQuery = true)
    List<QuestionDTO> getQuestionsByCategoryId(Long id);
}
