package com.example.springboot_cy_marketplace.services;

import com.example.springboot_cy_marketplace.dto.QuestionDTO;
import com.example.springboot_cy_marketplace.entity.QuestionEntity;
import com.example.springboot_cy_marketplace.model.QuestionModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IQuestionService extends IBaseService<QuestionModel, QuestionDTO,Long> {
    QuestionEntity update(QuestionModel model);
    QuestionEntity add(QuestionModel model);
    Page<QuestionModel> findQuestionNotYetAnswered(String status, Pageable pageable);
}
