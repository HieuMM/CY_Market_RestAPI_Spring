package com.example.springboot_cy_marketplace.services;

import com.example.springboot_cy_marketplace.entity.CityEntity;
import com.example.springboot_cy_marketplace.model.CityModel;

public interface ICityService extends IBaseService<CityEntity,CityModel,Long> {
    CityModel findByID(Long id);
}
