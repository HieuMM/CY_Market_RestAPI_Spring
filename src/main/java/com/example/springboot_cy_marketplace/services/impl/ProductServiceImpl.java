package com.example.springboot_cy_marketplace.services.impl;

import com.example.springboot_cy_marketplace.dto.*;
import com.example.springboot_cy_marketplace.entity.ProductClassifiedEntity;
import com.example.springboot_cy_marketplace.entity.ProductEntity;
import com.example.springboot_cy_marketplace.entity.StatisticalEntity;
import com.example.springboot_cy_marketplace.model.ProductModel;
import com.example.springboot_cy_marketplace.repository.*;
import com.example.springboot_cy_marketplace.services.AmazonClient;
import com.example.springboot_cy_marketplace.services.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements IProductService {
    @Autowired
    IProductRepository iProductRepository;
    @Autowired
    IProductClassifiedRepository iProductClassifiedRepository;
    @Autowired
    IProductCategoryRepository iProductCategoryRepository;
    @Autowired
    IUserRepository iUserRepository;
    @Autowired
    AmazonClient amazonClient;

    @Autowired
    IStatisticalRepository statisticalRepository;

    @Override
    public List<ProductModel> findAll() {
        return null;
    }

    /*
     * @author: HaiPhong
     * @since: 10/06/2022 4:38 CH
     * @description-VN:  Lấy danh sách toàn bộ sản phẩm
     * @description-EN:  Get a list of all products
     * @param: page
     * @return: Page<ProductModel>
     *
     * */
    @Override
    public Page<ProductModel> findAll(Pageable page) {
        return iProductRepository.findAll(page).map(ProductModel::entityToModel);
    }

    @Override
    public ProductModel findById(Long id) {
        return ProductModel.entityToModel(iProductRepository.findById(id).get());
    }

    /*
     * @author: HaiPhong
     * @since: 10/06/2022 4:39 CH
     * @description-VN:  Thêm mới sản phẩm
     * @description-EN:  Add new product
     * @param: dto
     * @return: ProductModel
     *
     * */
    @Override
    public ProductModel add(ProductDTO dto) {
        ProductEntity productEntity = ProductDTO.dtoToEntity(dto);
        productEntity.setUserEntity(iUserRepository.findById(dto.getIdUserSale()).get());

        if (dto.getCoverImage() != null) {

        }
        productEntity.setCoverImage(amazonClient.uploadFilewithFolder(dto.getCoverImage(), "product"));

        productEntity.setProductCategoryEntity(iProductCategoryRepository.findById(dto.getCategoryId()).get());

        ProductModel productModel = ProductModel.entityToModel(iProductRepository.save(productEntity));
        Long id_productParent = productModel.getId();
        for (ProductClassifiedDTO proDTO : dto.getProductClassifiedDTOList()) {
            ProductClassifiedEntity productClassifiedEntity = ProductClassifiedDTO.dtoToEntity(proDTO);
            productClassifiedEntity.setProductEntity(iProductRepository.findById(id_productParent).get());

            productClassifiedEntity.setImage(amazonClient.uploadFilewithFolder(proDTO.getImage(), "product-classified"));
            productClassifiedEntity.setCarts(null);

            iProductClassifiedRepository.save(productClassifiedEntity);
        }
        return productModel;
    }

    @Override
    public List<ProductModel> add(List<ProductDTO> dto) {
        return null;
    }

    @Override
    public ProductModel update(ProductDTO dto) {
        return null;
    }


    /*
     * @author: HaiPhong
     * @since: 10/06/2022 4:39 CH
     * @description-VN:  Xóa sản phẩm
     * @description-EN:  Delete product
     * @param: id
     * @return:
     *
     * */
    @Override
    public boolean deleteById(Long id) {
        try {
            iProductRepository.deleteById(id);
            Optional<ProductEntity> result = iProductRepository.findById(id);
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
     * @since: 10/06/2022 4:40 CH
     * @description-VN:  Lấy danh sách sản phẩm theo id người bán
     * @description-EN:  Get product list by seller id
     * @param: id_seller, pageable
     * @return: Page<ProductModel>
     *
     * */
    @Override
    public Page<ProductModel> findAllProductByIdSeller(Long id_seller, Pageable pageable) {
        return iProductRepository.findAllByUserEntity(iUserRepository.findById(id_seller).get(), pageable).map(pro -> ProductModel.entityToModel(pro));
    }

    @Override
    public Page<ProductModel> findAllProductByIdNameAndCategory(String search, Long id_category, Pageable pageable) {
        return iProductRepository.searchAllProductNameAndCategoryId(search, id_category, pageable).map((pro -> ProductModel.entityToModel(pro)));
    }

    @Override
    public ProductModel hideProduct(Long id) {
        ProductEntity productEntity = iProductRepository.findById(id).get();
        productEntity.setEnabled(!productEntity.isEnabled());
        iProductRepository.save(productEntity);
        return ProductModel.entityToModel(productEntity);
    }

    @Override
    public UpdateProductDTO findProductToUpdate(Long id) {
        return UpdateProductDTO.entityToDTO(iProductRepository.findById(id).get());
    }

    @Override
    public ProductModel updateProduct(UpdateProductDTO updateProductDTO) {
        ProductEntity productEntity = iProductRepository.findById(updateProductDTO.getId()).get();
        productEntity = UpdateProductDTO.dtoToEntity(updateProductDTO);

        productEntity.setUserEntity(iUserRepository.findById(updateProductDTO.getIdUserSale()).get());
        productEntity.setProductCategoryEntity(iProductCategoryRepository.findById(updateProductDTO.getCategoryId()).get());

        if (updateProductDTO.getCoverImage() != null) {
            productEntity.setCoverImage(amazonClient.uploadFilewithFolder(updateProductDTO.getCoverImage(), "product"));
        } else {
            productEntity.setCoverImage(updateProductDTO.getCoverImageString());
        }


        ProductModel productModel = ProductModel.entityToModel(iProductRepository.save(productEntity));
        Long id_productParent = productModel.getId();

        for (UpdateProductClassifiedDTO proDTO : updateProductDTO.getProductClassifiedDTOList()) {
            ProductClassifiedEntity productClassifiedEntity = new ProductClassifiedEntity();
            if (proDTO.getId() != null) {
                productClassifiedEntity = iProductClassifiedRepository.findById(proDTO.getId()).get();
            }
            productClassifiedEntity = UpdateProductClassifiedDTO.dtoToEnitty(proDTO);
            if (proDTO.getImage() != null) {
                productClassifiedEntity.setImage(amazonClient.uploadFilewithFolder(proDTO.getImage(), "product-classified"));
            } else {
                productClassifiedEntity.setImage(proDTO.getImageString());
            }
            productClassifiedEntity.setProductEntity(iProductRepository.findById(id_productParent).get());

            iProductClassifiedRepository.save(productClassifiedEntity);
        }
        return productModel;
    }

    /*
     * @author: HaiPhong
     * @since: 10/06/2022 4:40 CH
     * @description-VN:  Lấy danh sách sản phẩm theo nơi bán
     * @description-EN:  Get a list of products by place of sale
     * @param: id, page
     * @return: Page<ProductModel>
     *
     * */
    public Page<ProductModel> findAllByProviderId(Pageable page, Long id) {
        return iProductRepository.findAllByProvinceId(id, page).map(pro -> ProductModel.entityToModel(pro));
    }

    /*
     * @author: HaiPhong
     * @since: 10/06/2022 4:41 CH
     * @description-VN:  Lấy danh sách sản phẩm theo giá
     * @description-EN:  Get product list by price
     * @param: page,priceFrom,priceTo
     * @return: Page<ProductModel>
     *
     * */
    public Page<ProductModel> findAllByNewPrice(Pageable page, int priceFrom, int priceTo, Long id_category) {
        return iProductRepository.findAllByNewPrice(page, priceFrom, priceTo, id_category).map(pro -> ProductModel.entityToModel(pro));
    }

    /*
     * @author: HaiPhong
     * @since: 10/06/2022 4:42 CH
     * @description-VN:  Lấy danh sách sản phẩm theo tình trạng
     * @description-EN:  Get product list by status
     * @param:
     * @return:
     *
     * */
    public Page<ProductModel> findAllByStatus(Pageable page, Boolean status, Long id_category) {
        return iProductRepository.findAllByStatus(page, status, id_category).map(pro -> ProductModel.entityToModel(pro));
    }
    /*public Page<ProductModel> findAllByBranch(Pageable page, Long id) {
        return iProductRepository.findAllByBranch(page,id).map(pro -> ProductModel.entityToModel(pro));
    }*/

    /*
     * @author: Manh Tran
     * @since: 13/06/2022 2:51 CH
     * @description-VN: Lấy danh sách sản phẩm của người bán.
     * @description-EN: Get list product of seller.
     * @param:
     * @return:
     *
     * */
    public List<ProductModel> findAllProductsBySellerId(Long sellerId, Pageable pageable) {
        List<ProductEntity> products = this.iProductRepository.findAllByUserEntity_Id(sellerId, pageable);
        return products.stream().map(ProductModel::entityToModel).collect(Collectors.toList());
    }

    /*
     * @author: HaiPhong
     * @since: 15/06/2022 4:06 CH
     * @description-VN:  Tìm kiếm sản phẩm theo tên sản phẩm hoặc tên danh mục sản phẩm
     * @description-EN:  Search products by product name or product category name
     * @param: search,pageable
     * @return:
     *
     * */
    public Page<ProductModel> searchAllProduct(String search, Pageable pageable) {
        return iProductRepository.searchAllProduct(search, pageable).map(ProductModel::entityToModel);
    }

    /*
     * @author: HieuMM
     * @since: 16-Jun-22 10:13 AM
     * @description-VN:  gợi ý sản phẩm
     * @description-EN:  suggestion product
     * @param:
     * */
    public Page<ProductModel> suggestionProduct(Pageable page, String key1, String key2) {
        return iProductRepository.suggestionProduct(page, key1, key2).map(ProductModel::entityToModel);
    }

    /*
     * @author: Manh Tran
     * @since: 29/06/2022 10:12 SA
     * @description-VN: Danh sách sản phẩm theo trạng thái (đang bán, sắp hết hàng, hết hàng, bị khoá).
     * @description-EN: List of products by status (selling, out of stock, sold out, locked).
     * @param: statusId - mã yêu cầu tương ứng (1,2,3,4).
     * @param: pageable - phân trang.
     * @return:
     *
     * */
    public Page<ProductModel> findAllProductByStatus(int statusId, Pageable page) {
        if (statusId == 1) { // Sản phẩm đang bán
            return iProductRepository.findAllByEnabled(true, page).map(ProductModel::entityToModel);
        } else if (statusId == 2) { // Sản phẩm bị khoá
            return iProductRepository.findAllByEnabled(false, page).map(ProductModel::entityToModel);
        } else if (statusId == 3) { // Sản phẩm sắp hết hàng
            return iProductRepository.listProductWithAmount(1, 10, page).map(ProductModel::entityToModel);
        } else if (statusId == 4) { // Sản phẩm hết hàng
            return iProductRepository.listProductWithAmount(0, 0, page).map(ProductModel::entityToModel);
        }
        return null;
    }

    /*
     * @author: Manh Tran
     * @since: 29/06/2022 11:31 SA
     * @description-VN: 10 sản phẩm bán chạy nhất.
     * @description-EN: Top 10 most sold products.
     * @param:
     * @return:
     *
     * */
    public Page<ProductModel> findTopProducts(int top, Pageable pageable) {
        List<ProductModel> result = iProductRepository.topProductsSold(top).stream().map(ProductModel::entityToModel)
                .collect(Collectors.toList());
        final long start = pageable.getOffset();
        final long end = Math.min(start + pageable.getPageSize(), result.size());
        List<ProductModel> pageableWithList = result.subList((int) start, (int) end);
        LocalDate ld = LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        StatisticalEntity statistical = this.statisticalRepository.findByDate(ld.getDayOfMonth(), ld.getMonthValue(), ld.getYear());
        if(statistical == null){
            statistical = new StatisticalEntity();
            statistical.setDay(ld.getDayOfMonth());
            statistical.setMonth(ld.getMonthValue());
            statistical.setYear(ld.getYear());
            statistical.setTotalView(1);
            statisticalRepository.save(statistical);
        }else {
            statistical.setTotalView(statistical.getTotalView() + 1);
            statisticalRepository.save(statistical);
        }
        return new PageImpl<>(pageableWithList, pageable, pageableWithList.size());
    }

    /*
     * @author: Manh Tran
     * @since: 29/06/2022 4:16 CH
     * @description-VN: Các sản phẩm có lượt xem nhiều nhất.
     * @description-EN: Products with most views.
     * @param:
     * @return:
     *
     * */
    public Page<ProductModel> findTopViewedProducts(int top, Pageable pageable) {
        List<ProductModel> result = iProductRepository.topProductsViews(top).stream().map(ProductModel::entityToModel)
                .collect(Collectors.toList());
        for (ProductModel product : result) {
            product.setTotalViews(iProductRepository.countTotalView(product.getId()));
        }
        final long start = pageable.getOffset();
        final long end = Math.min(start + pageable.getPageSize(), result.size());
        List<ProductModel> pageableWithList = result.subList((int) start, (int) end);
        return new PageImpl<>(pageableWithList, pageable, pageableWithList.size());
    }

    /*
     * @author: Manh Tran
     * @since: 30/06/2022 9:09 SA
     * @description-VN: Thống kê chung về sản phẩm.
     * @description-EN: Statistical about products.
     * @param: startDayTime - ngày bắt đầu.
     * @param: endDayTime - ngày kết thúc.
     * @return:
     *
     * */
    public ProductStatisticDTO statisticalProducts(String startDayTime, String endDayTime) {
        ProductStatisticDTO productStatisticDTO = new ProductStatisticDTO();
        productStatisticDTO.setTotalProducts(iProductRepository.count());
        productStatisticDTO.setTotalProductsSold(iProductRepository.countTotalSold());
        productStatisticDTO.setTotalProductsInStock(iProductRepository.countProductInstockAndEnable());
        productStatisticDTO.setTotalProductsLocked(iProductRepository.findAllByEnabled(false, null).getTotalElements());
        productStatisticDTO.setTotalProductsOutOfStock(iProductRepository.countProductOutOfStock());
        productStatisticDTO.setTotalProductsWillBeOutOfStock(iProductRepository.countProductWillBeOutOfStock());
        ProductModel bestViews = ProductModel.entityToModel(iProductRepository.topProductsViews(1).get(0));
        bestViews.setTotalViews(iProductRepository.countTotalView(bestViews.getId()));
        productStatisticDTO.setProductWithMostViews(bestViews);
        productStatisticDTO.setProductWithMostSales(ProductModel.entityToModel(iProductRepository.topProductsSold(1).get(0)));
        return productStatisticDTO;
    }

    /*
     * @author: Manh Tran
     * @since: 30/06/2022 10:51 SA
     * @description-VN: Lọc sản phẩm theo số lượng đã bán.
     * @description-EN: Filter products by amount sold.
     * @param:
     * @return:
     *
     * */
    public Page<ProductModel> filterProductsByAmountSold(int min, int max, Pageable pageable) {
        return iProductRepository.findAllByTotalSoldBetween(min, max, pageable).map(ProductModel::entityToModel);
    }
}

