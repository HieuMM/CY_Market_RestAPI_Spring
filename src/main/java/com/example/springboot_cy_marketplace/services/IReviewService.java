package com.example.springboot_cy_marketplace.services;

import com.example.springboot_cy_marketplace.dto.ReviewDTO;
import com.example.springboot_cy_marketplace.entity.ReviewEntity;
import com.example.springboot_cy_marketplace.model.ReviewModel;

public interface IReviewService extends IBaseService<ReviewModel, ReviewDTO, Long> {
    ReviewEntity add(ReviewModel model);
    ReviewEntity update(ReviewModel model);
}
