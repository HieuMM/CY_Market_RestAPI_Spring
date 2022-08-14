package com.example.springboot_cy_marketplace.repository;

import com.example.springboot_cy_marketplace.entity.CityEntity;
import com.example.springboot_cy_marketplace.entity.DistrictEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IDistrictRepository extends JpaRepository<DistrictEntity,Long> {
    /*
    * @author: HieuMM
    * @since: 10-Jun-22 11:39 AM
    * @description:danh sách huyện theo thành phố
    * @update:
    * */
    List<DistrictEntity> findByCity(CityEntity cityName);
}
