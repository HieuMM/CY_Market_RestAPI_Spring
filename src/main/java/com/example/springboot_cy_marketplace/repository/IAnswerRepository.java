package com.example.springboot_cy_marketplace.repository;

import com.example.springboot_cy_marketplace.entity.AnswerEntity;
import com.example.springboot_cy_marketplace.entity.QuestionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAnswerRepository extends JpaRepository<AnswerEntity,Long> {
    Page<AnswerEntity> findAllByQuestionEntity(QuestionEntity questionEntity, Pageable pageable);
}
