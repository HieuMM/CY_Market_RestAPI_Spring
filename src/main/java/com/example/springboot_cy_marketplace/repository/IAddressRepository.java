package com.example.springboot_cy_marketplace.repository;

import com.example.springboot_cy_marketplace.entity.AddressEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IAddressRepository extends JpaRepository<AddressEntity, Long> {
    AddressEntity findByCityAndDistrictAndWardAndAddress(String city, String district, String ward, String address);
    Page<AddressEntity> findAllByUserEntityList_id(Pageable pageable,Long id);

    List<AddressEntity> findAllByUserEntityList_id(Long idUser);
}
