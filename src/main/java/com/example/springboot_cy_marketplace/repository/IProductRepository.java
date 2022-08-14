package com.example.springboot_cy_marketplace.repository;

import com.example.springboot_cy_marketplace.entity.ProductCategoryEntity;
import com.example.springboot_cy_marketplace.entity.ProductEntity;
import com.example.springboot_cy_marketplace.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IProductRepository extends JpaRepository<ProductEntity,Long> {
    Page<ProductEntity> findAllByUserEntity(UserEntity userEntity,Pageable pageable);
    List<ProductEntity> findAllByProductCategoryEntity(ProductCategoryEntity productCategoryEntity);


    /*
    * @author: HieuMM
    * @since: 10-Jun-22 11:36 AM
    * @description:filter nơi bán
    * @update:
    * */
    Page<ProductEntity> findAllByProvinceId(Long id, Pageable pageable);

    /*
    * @author: HieuMM
    * @since: 10-Jun-22 11:37 AM
    * @description://filter giá (khoảng giá)
    * @update:
    * */
//    @Query("SELECT u FROM ProductEntity u WHERE u.minNewPrice > :priceFrom and u.maxNewPrice < :priceTo  and u.productCategoryEntity.id = :id_category")
//    Page<ProductEntity> findAllByNewPrice (Pageable pageable, @Param("priceFrom") int priceFrom, @Param("priceTo") int priceTo, @Param("id_category") Long id_category);

    @Query("SELECT u FROM ProductEntity u \n " +
            "JOIN ProductCategoryEntity prct ON u.productCategoryEntity.id = prct.id \n " +
            "WHERE " +
            "u.minNewPrice > :priceFrom \n " +
            "AND \n " +
            "u.maxNewPrice < :priceTo \n " +
            "AND \n " +
            "prct.parentCategory.id = :id_category")
    Page<ProductEntity> findAllByNewPrice (Pageable pageable, @Param("priceFrom") int priceFrom, @Param("priceTo") int priceTo, @Param("id_category") Long id_category);


    /*
    * @author: HieuMM
    * @since: 10-Jun-22 11:37 AM
    * @description://filter tình trạng
    * @update:
    * */
    @Query("SELECT u FROM ProductEntity u \n " +
            "JOIN ProductCategoryEntity prct ON u.productCategoryEntity.id = prct.id \n " +
            "WHERE " +
            "u.status = :status \n " +
            "AND \n " +
            "prct.parentCategory.id = :id_category")
    Page<ProductEntity> findAllByStatus (Pageable pageable,@Param("status") Boolean status, @Param("id_category") Long id_category);
    //filter theo branch
   /* @Query("SELECT u FROM ProductEntity u WHERE u.branchId = :branch ")
    Page<ProductEntity> findAllByBranch (Pageable pageable, @Param("branch") Long branch);*/

    /*
    * @author: HieuMM
    * @since: 16-Jun-22 8:56 AM
    * @description-VN:  gợi ý sản phẩm
    * @description-EN: suggestion product
    * @param:
    * */
    @Query("select p from ProductEntity p where  p.name like %:key1%  or p.name like %:key2%")
    Page<ProductEntity> suggestionProduct(Pageable pageable, @Param("key1") String key1, @Param("key2") String key2);

    /*
    * @author: Manh Tran
    * @since: 13/06/2022 2:52 CH
    * @description-VN: Lấy danh sách sản phẩm của người bán.
    * @description-EN: Get list of product of seller.
    * @param: id - Mã người bán.
    * @return:
    *
    * */
    List<ProductEntity> findAllByUserEntity_Id(Long id, Pageable pageable);

    @Query("SELECT pr FROM ProductEntity pr JOIN ProductCategoryEntity prct ON pr.productCategoryEntity.id = prct.id \n" +
            "WHERE \n" +
            "pr.name LIKE %:search% \n" +
            "OR \n" +
            "prct.name LIKE %:search%")
    Page<ProductEntity> searchAllProduct(@Param(value = "search") String search, Pageable pageable);

    @Query("SELECT pr FROM ProductEntity pr WHERE\n " +
            "pr.name LIKE %:search% \n " +
            "and pr.productCategoryEntity.id = :id_category ")
    Page<ProductEntity> searchAllProductNameAndCategoryId(@Param(value = "search") String search,
                                                          @Param(value = "id_category") Long id_category, Pageable pageable);

    @Query("SELECT p FROM ProductEntity p WHERE p.totalAmount BETWEEN ?1 AND ?2")
    Page<ProductEntity> listProductWithAmount(int amountStart, int amountEnd, Pageable pageable);

    Page<ProductEntity> findAllByStatus(Boolean status, Pageable pageable);

    Page<ProductEntity> findAllByEnabled(Boolean enabled, Pageable pageable);

    @Query(value = "SELECT * FROM product p ORDER BY p.total_sold DESC LIMIT ?1", nativeQuery = true)
    List<ProductEntity> topProductsSold(int top);

    @Query(value = "SELECT * FROM product p WHERE p.id IN (SELECT s.product_id FROM product_statistical s GROUP BY s.product_id " +
            "ORDER BY SUM(s.total_view) DESC) LIMIT ?1", nativeQuery = true)
    List<ProductEntity> topProductsViews(int top);

    @Query(value = "SELECT SUM(s.total_view) FROM product_statistical s WHERE s.product_id = ?1", nativeQuery = true)
    long countTotalView(Long id);

    @Query(value = "SELECT SUM(p.total_sold) FROM product p", nativeQuery = true)
    long countTotalSold();

    @Query(value = "SELECT COUNT(DISTINCT p.id) " +
            "FROM product p INNER JOIN product_classified c " +
            "WHERE p.id = c.product_id " +
            "AND p.enabled = 1 " +
            "AND (SELECT SUM(c.amount) FROM product_classified c WHERE c.product_id = p.id) > 10 ", nativeQuery = true)
    long countProductInstockAndEnable();

    @Query(value = "SELECT COUNT(DISTINCT p.id) " +
            "FROM product p INNER JOIN product_classified c ON p.id = c.product_id " +
            "WHERE p.enabled = 1 " +
            "AND (SELECT SUM(c.amount) FROM product_classified c WHERE c.product_id = p.id) BETWEEN 1 AND 10", nativeQuery = true)
    long countProductWillBeOutOfStock();

    @Query(value = "SELECT * FROM product " +
            "WHERE product.enabled = 1 AND product.id IN (SELECT product_classified.product_id " +
            "FROM product_classified INNER JOIN product ON product_classified.product_id = product.id " +
            "GROUP BY product_classified.product_id HAVING SUM(product_classified.amount) BETWEEN 1 AND 10)", nativeQuery = true)
    Page<ProductEntity> listProductWillBeOutOfStock(Pageable pageable);
    @Query(value = "SELECT COUNT(DISTINCT p.id) " +
            "FROM product p INNER JOIN product_classified c " +
            "WHERE p.id = c.product_id " +
            "AND p.enabled = 1 " +
            "AND (SELECT SUM(c.amount) FROM product_classified c WHERE c.product_id = p.id) = 0 ", nativeQuery = true)
    long countProductOutOfStock();

    Page<ProductEntity> findAllByTotalSoldBetween(int min, int max, Pageable pageable);

    List<ProductEntity> findAllByEnabled(Boolean status);
} 
