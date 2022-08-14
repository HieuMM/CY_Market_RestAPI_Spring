package com.example.springboot_cy_marketplace.repository;

import com.example.springboot_cy_marketplace.entity.DistrictEntity;
import com.example.springboot_cy_marketplace.entity.WardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IWardRepository extends JpaRepository<WardEntity,Long> {
    /*
    * @author: HieuMM
    * @since: 10-Jun-22 11:39 AM
    * @description:danh sách xã theo huyện
    * @update:
    * */
    List<WardEntity> findByDistrict(DistrictEntity districtName);
}
