package com.example.springboot_cy_marketplace.model;
import com.example.springboot_cy_marketplace.entity.ReviewEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewModel {
    private Long id;
    private int rate;
    private String image;
    private String reviewtext;
    private Long productId;
    private Long userId;
    private Long orderId;
    private UserModel userModel;

    public static ReviewModel entityToModel(ReviewEntity object){
        return ReviewModel.builder()
                .id(object.getId())
                .image(object.getImage())
                .rate(object.getRate())
                .reviewtext(object.getReviewtext())
                .productId(object.getProduct().getId())
                .userId(object.getUserEntity().getId())
                .orderId(object.getOrderEntity() == null ? null : object.getOrderEntity().getId())
                .userModel(object.getUserEntity() != null ? UserModel.entityToModel(object.getUserEntity()) : null )
                .build();
    }


}
