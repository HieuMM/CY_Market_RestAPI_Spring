package com.example.springboot_cy_marketplace.services;

import com.example.springboot_cy_marketplace.dto.BannerDTO;
import com.example.springboot_cy_marketplace.model.BannerModel;

public interface IBannerService extends IBaseService<BannerModel, BannerDTO,Long> {
    BannerDTO findBannerById(Long id);
}
