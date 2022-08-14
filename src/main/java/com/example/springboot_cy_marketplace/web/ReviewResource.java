package com.example.springboot_cy_marketplace.web;

import com.example.springboot_cy_marketplace.dto.ResponseDTO;
import com.example.springboot_cy_marketplace.dto.ReviewDTO;
import com.example.springboot_cy_marketplace.entity.ReviewEntity;
import com.example.springboot_cy_marketplace.model.ReviewModel;
import com.example.springboot_cy_marketplace.repository.IReviewRepository;
import com.example.springboot_cy_marketplace.services.impl.ProductStatisticalService;
import com.example.springboot_cy_marketplace.services.impl.ReviewServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/review")
public class ReviewResource {
    @Autowired
    ReviewServiceImpl reviewService;

    /*
     * @author: HieuMM
     * @since: 10-Jun-22 11:22 AM
     * @description-VN:  hàm này để add review vào product
     * @description-EN:  this function to add review to product
     * @param: reviewDTO
     * @return:
     *
     * */
  /*  @RolesAllowed(Position.BUYER)*/
    @PostMapping(value = "/add")
    public Object addReview(ReviewDTO reviewDTO) {
        ReviewModel model = reviewService.sendReviewToModel(reviewDTO);
        ReviewEntity reviewEntity = reviewService.add(model);
        if (reviewEntity == null) {
            return ResponseDTO.of(null, "Add review");
        } else {
            return ResponseDTO.of(ReviewDTO.entityToDto(reviewEntity), "Add review");
        }
    }
    /*
    * @author: HieuMM
    * @since: 21-Jun-22 9:49 AM
    * @description-VN:  Hiển thị tất cả đánh giá của từng sản phẩm.
    * @description-EN:  Show all review of every product.
    * @param:
    * */
    @GetMapping("/findAllReview/{id}")
    public Object findAllReview(Pageable pageable, @PathVariable(value = "id") Long id) {
        return ResponseDTO.of(reviewService.findAllReview(pageable, id), "findAllByProductId");
    }
    /*
    * @author: HieuMM
    * @since: 21-Jun-22 10:25 AM
    * @description-VN:  Hiển thị trung bình rate
    * @description-EN:average Rate of every product
    * @param:
    * */
    @Autowired
    IReviewRepository reviewRepository;
    @GetMapping("/averageRate/{id}")
    public Object averageRate(@PathVariable(value = "id") Long id) {
        return ResponseDTO.of(reviewService.averageRate(id), "avg rate of review product");
    }
}
