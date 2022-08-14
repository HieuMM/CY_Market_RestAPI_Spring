package com.example.springboot_cy_marketplace.services;

import com.example.springboot_cy_marketplace.dto.ProductCategoryDTO;
import com.example.springboot_cy_marketplace.model.ProductCategoryModel;
import com.example.springboot_cy_marketplace.model.ProductModel;

public interface IProductCategoryService extends IBaseService<ProductCategoryDTO, ProductCategoryModel, Long> {
    ProductCategoryModel hideProductCategory(Long id);
}
