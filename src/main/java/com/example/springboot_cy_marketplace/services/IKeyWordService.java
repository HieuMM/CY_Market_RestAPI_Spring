package com.example.springboot_cy_marketplace.services;

import com.example.springboot_cy_marketplace.dto.KeyWordDTO;
import com.example.springboot_cy_marketplace.entity.KeyWordEntity;
import com.example.springboot_cy_marketplace.model.KeyWordModel;



public interface IKeyWordService extends IBaseService<KeyWordModel, KeyWordDTO, Long> {
    KeyWordEntity add(KeyWordModel model);
}

