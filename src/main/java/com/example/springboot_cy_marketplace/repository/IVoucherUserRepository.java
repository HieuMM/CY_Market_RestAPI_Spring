package com.example.springboot_cy_marketplace.repository;

import com.example.springboot_cy_marketplace.entity.VoucherUserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IVoucherUserRepository extends JpaRepository<VoucherUserEntity, Long> {
    Page<VoucherUserEntity> findAllByUserEntity_Id(Pageable pageable,Long id);
    @Query("select count(v) from VoucherUserEntity v where v.userEntity.id = ?1 and v.voucherEntity.id = ?2")
    int countByVoucherEntityAndAndUserEntity(Long userId,Long voucherId);

    @Query("select v from VoucherUserEntity v where v.voucherEntity.id = ?1")
    List<VoucherUserEntity> findAllByVoucher(Long id);
}

