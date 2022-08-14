package com.example.springboot_cy_marketplace.services.impl;

import com.example.springboot_cy_marketplace.dto.ReviewDTO;
import com.example.springboot_cy_marketplace.entity.OrderEntity;
import com.example.springboot_cy_marketplace.entity.ProductEntity;
import com.example.springboot_cy_marketplace.entity.ReviewEntity;
import com.example.springboot_cy_marketplace.model.ReviewModel;
import com.example.springboot_cy_marketplace.repository.IOrderRepository;
import com.example.springboot_cy_marketplace.repository.IProductRepository;
import com.example.springboot_cy_marketplace.repository.IReviewRepository;
import com.example.springboot_cy_marketplace.repository.IUserRepository;
import com.example.springboot_cy_marketplace.services.AmazonClient;
import com.example.springboot_cy_marketplace.services.IReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements IReviewService {
    @Autowired
    IReviewRepository reviewRepository;
    @Autowired
    IProductRepository productRepository;
    @Autowired
    IUserRepository userRepository;

    @Autowired
    IOrderRepository orderRepository;
    @Autowired
    AmazonClient amazonClient;

    @Autowired
    ProductStatisticalService productStatisticalService;

    @Value("${avatar.default.url}")
    @Override
    public List<ReviewModel> findAll() {
        return null;
    }

    @Override
    public Page<ReviewModel> findAll(Pageable page) {
        return null;
    }

    @Override
    public ReviewModel findById(Long id) {
        return null;
    }

    @Override
    public ReviewModel add(ReviewDTO dto) {
        return null;
    }

    @Override
    public List<ReviewModel> add(List<ReviewDTO> dto) {
        return null;
    }

    @Override
    public ReviewModel update(ReviewDTO dto) {
        return null;
    }


    /*
     * @author: HaiPhong
     * @since: 10/06/2022 4:52 CH
     * @description-VN:  Xóa đánh giá
     * @description-EN:  Delete review
     * @param: id
     * @return:
     *
     * */
    @Override
    public boolean deleteById(Long id) {
        try {
            reviewRepository.deleteById(id);
            Optional<ReviewEntity> result = reviewRepository.findById(id);
            return result.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean deleteByIds(List<Long> id) {
        return false;
    }

    /*
     * @author: HaiPhong
     * @since: 10/06/2022 4:52 CH
     * @description-VN:  Thêm mới đánh giá sản phẩm
     * @description-EN:  Add new product reviews
     * @param:   model
     * @return:  ReviewEntity
     *
     * */
    @Override
    public ReviewEntity add(ReviewModel model) {
        ReviewEntity reviewEntity = toEntity(model);
        return reviewRepository.save(reviewEntity);
    }

    @Override
    public ReviewEntity update(ReviewModel model) {
        return null;
    }

    /*
     * @author: HieuMM
     * @since: 10-Jun-22 11:03 AM
     * @description-VN:  chuyển ReviewModel thành ReviewEntity
     * @description-EN:  convert ReviewModel to ReviewEntity
     * @param: model
     * @return: ReviewEntity
     *
     * */
    private ReviewEntity toEntity(ReviewModel model) {
        ReviewEntity reviewEntity = new ReviewEntity();
        reviewEntity.setId(model.getId());
        reviewEntity.setImage(model.getImage() == null ? "" : model.getImage());
        reviewEntity.setRate(model.getRate());
        reviewEntity.setReviewtext(model.getReviewtext());
        Optional<ProductEntity> product = productRepository.findById(model.getProductId());
        if (product.isPresent()) {
            reviewEntity.setProduct(product.get());
        } else {
            reviewEntity.setProduct(new ProductEntity());
        }
        try {
            reviewEntity.setUserEntity(userRepository.findById(model.getUserId()).get());
            OrderEntity orderEntity = orderRepository.findById(model.getOrderId()).orElse(null);
            reviewEntity.setOrderEntity(orderEntity);
            assert orderEntity != null;
            orderEntity.setReviewStatus(true);
            orderRepository.save(orderEntity);
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return reviewEntity;
    }


    /*
     * @author: HieuMM
     * @since: 10-Jun-22 11:04 AM
     * @description-VN:  chuyển ReviewDTO thành ReviewModel
     * @description-EN:  convert ReviewDTO to ReviewModel
     * @param:
     * @return:
     *
     * */
    public ReviewModel sendReviewToModel(ReviewDTO reviewDTO) {
        ReviewModel model = new ReviewModel();
        model.setId(reviewDTO.getId());
        model.setReviewtext(reviewDTO.getReviewtext());
        if (reviewDTO.getImage() != null) {
            String avatarUrl = amazonClient.uploadFilewithFolder(reviewDTO.getImage(), "review");
            model.setImage(avatarUrl);
        } else {
            model.setImage(null);
        }

        model.setRate(reviewDTO.getRate());
        model.setProductId(reviewDTO.getProductId());
        model.setUserId(reviewDTO.getUserId());
        model.setOrderId(reviewDTO.getOrderId());
        return model;
    }

    /*
     * @author: HieuMM
     * @since: 21-Jun-22 9:49 AM
     * @description-VN:  Hiển thị tất cả đánh giá của từng sản phẩm
     * @description-EN:  Show all review of every product
     * @param:
     * */
    public Page<ReviewModel> findAllReview(Pageable page, Long id) {
        productStatisticalService.updateView(id);
        Pageable page2 = PageRequest.of(page.getPageNumber(), page.getPageSize(), Sort.by(Sort.Direction.DESC, "id"));
        return reviewRepository.findAllByProduct_Id(page2, id).map(ReviewModel::entityToModel);
    }

    /*
     * @author: HieuMM
     * @since: 21-Jun-22 10:25 AM
     * @description-VN:  Hiển thị trung bình rate
     * @description-EN:average Rate of every product
     * @param:
     * */
    public Object averageRate(Long id) {
        float sum = reviewRepository.sumRate(id);
        float count = reviewRepository.countReview(id);
        float avg = sum / count;
        return avg;
    }
}

