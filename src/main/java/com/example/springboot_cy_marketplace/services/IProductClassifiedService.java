package com.example.springboot_cy_marketplace.services;

import com.example.springboot_cy_marketplace.dto.ProductClassifiedDTO;
import com.example.springboot_cy_marketplace.model.ProductClassifiedModel;

public interface IProductClassifiedService extends IBaseService<ProductClassifiedModel, ProductClassifiedDTO,Long>{
    ProductClassifiedModel findByClassifiedName(String classified01,String classified02,Long id);
}
