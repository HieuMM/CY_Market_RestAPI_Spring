package com.example.springboot_cy_marketplace.services;

import com.example.springboot_cy_marketplace.dto.AnswerDTO;
import com.example.springboot_cy_marketplace.model.AnswerModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IAnswerService extends IBaseService<AnswerModel, AnswerDTO,Long> {
    Page<AnswerModel> getAllByIdQuestion(Long id_question, Pageable pageable);
}
