package com.example.springboot_cy_marketplace.repository;

import com.example.springboot_cy_marketplace.entity.OrderEntity;
import com.example.springboot_cy_marketplace.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface IOrderRepository extends JpaRepository<OrderEntity, Long> {
    Optional<OrderEntity> findByPaymentToken(String paymentToken);

    Page<OrderEntity> findByUserEntity_Id(Long userId, Pageable pageable);

    Page<OrderEntity> findByStatusAndUserEntity_Id(String status, Long userId, Pageable pageable);

    Page<OrderEntity> findAll(Pageable pageable);

    @Query(value = "SELECT o FROM OrderEntity o WHERE CONCAT(o.id, '') = ?1 OR o.totalPrice = ?1 OR o.shippingFee = ?1 " +
            "OR o.paymentMethod LIKE CONCAT('%', ?1, '%') OR o.provinceName LIKE CONCAT('%', ?1, '%') " +
            "OR o.cityName LIKE CONCAT('%', ?1, '%') OR o.districtName LIKE CONCAT('%', ?1, '%') " +
            "OR o.homeAddress LIKE CONCAT('%', ?1, '%') OR o.userEntity.id = ?1 OR o.reviewEntity.id = ?1 " +
            "OR o.userEntity.fullName LIKE CONCAT('%', ?1, '%') OR o.userEntity.email LIKE CONCAT('%', ?1, '%') " +
            "OR o.userEntity.phoneNumber LIKE CONCAT('%', ?1, '%')")
    Page<OrderEntity> findByKeyword(String keyword, Pageable pageable);

    Page<OrderEntity> findByStatus(String status, Pageable pageable);

    Page<OrderEntity> findByCreateDateBetween(Date startDate, Date endDate, Pageable pageable);

    Page<OrderEntity> findByPaymentMethod(String paymentMethod, Pageable pageable);

    @Query(value = "SELECT COUNT(o.id) FROM orders o WHERE o.create_date BETWEEN ?1 AND ?2 AND o.status = ?3", nativeQuery = true)
    long countByStatusAndTime(String from, String to, String status);

    @Query(value = "SELECT COUNT(o.id) FROM orders o WHERE o.create_date BETWEEN ?1 AND ?2 AND o.payment_method = ?3", nativeQuery = true)
    long countByPaymentMethodAndTime(String from, String to, String paymentMethod);

    @Query(value = "SELECT SUM(o.total_price) FROM orders o WHERE o.create_date BETWEEN ?1 AND ?2", nativeQuery = true)
    Double sumRevenueByTime(String from, String to);

    @Query(value = "SELECT SUM(o.total_price) + SUM(o.shipping_fee) - SUM(o.total_discount) FROM orders o WHERE o.create_date BETWEEN ?1 AND ?2", nativeQuery = true)
    Double sumRevenueByTimeUpdate(String from, String to);
    @Query(value = "SELECT SUM(o.profit) FROM orders o WHERE o.create_date BETWEEN ?1 AND ?2", nativeQuery = true)
    Double sumProfitByTime(String from, String to);

    @Query(value = "SELECT SUM(o.total_price) - SUM(o.total_discount) - SUM(o.cost_price) FROM orders o WHERE o.create_date BETWEEN ?1 AND ?2", nativeQuery = true)
    Double sumProfitByTimeUpdate(String from, String to);

    @Query(value = "SELECT COUNT(*) FROM orders o WHERE o.create_date LIKE CONCAT('%', ?1, '%')", nativeQuery = true)
    Long countNewOdersOfDay(String day);

    @Query(value = "SELECT * FROM orders WHERE orders.create_date BETWEEN ?1 AND ?2", nativeQuery = true)
    Page<OrderEntity> adminReportRevenue(String from, String to, Pageable pageable);


    @Query("SELECT u FROM OrderEntity u where u.createDate between :startDate and :endDate")
    List<OrderEntity> listOrderInTime(@Param("startDate") java.sql.Date startDate, @Param("endDate") java.sql.Date endDate);



}
