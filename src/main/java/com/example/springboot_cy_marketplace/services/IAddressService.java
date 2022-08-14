package com.example.springboot_cy_marketplace.services;

import com.example.springboot_cy_marketplace.entity.AddressEntity;
import com.example.springboot_cy_marketplace.model.AddressModel;

public interface IAddressService extends IBaseService<AddressEntity, AddressModel, Long> {
    AddressEntity findAddressEntity(String city, String district, String ward, String address);
}
