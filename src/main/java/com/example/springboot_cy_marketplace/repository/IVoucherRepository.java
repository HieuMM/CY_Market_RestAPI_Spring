package com.example.springboot_cy_marketplace.repository;

import com.example.springboot_cy_marketplace.entity.VoucherEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IVoucherRepository extends JpaRepository<VoucherEntity, Long> {
    @Query(value = "SELECT v.codeVoucher FROM VoucherEntity v")
    List<String> findAllCode();

    @Override
    Page<VoucherEntity> findAll(Pageable pageable);

}

