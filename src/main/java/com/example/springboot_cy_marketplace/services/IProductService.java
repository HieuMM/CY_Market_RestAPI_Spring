package com.example.springboot_cy_marketplace.services;

import com.example.springboot_cy_marketplace.dto.ProductDTO;
import com.example.springboot_cy_marketplace.dto.UpdateProductDTO;
import com.example.springboot_cy_marketplace.jwt.payload.request.UpdateAddressProfileRequest;
import com.example.springboot_cy_marketplace.model.ProductModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IProductService extends IBaseService<ProductModel, ProductDTO,Long>{
    Page<ProductModel> findAllProductByIdSeller(Long id_seller, Pageable pageable);
    Page<ProductModel> findAllProductByIdNameAndCategory(String search, Long id_category, Pageable pageable);

    ProductModel hideProduct(Long id);

    UpdateProductDTO findProductToUpdate(Long id);

    ProductModel updateProduct(UpdateProductDTO updateProductDTO);
}
