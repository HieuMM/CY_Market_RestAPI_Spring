package com.example.springboot_cy_marketplace.services.impl;

import com.example.springboot_cy_marketplace.dto.ProductClassifiedDTO;
import com.example.springboot_cy_marketplace.entity.ProductClassifiedEntity;
import com.example.springboot_cy_marketplace.model.ProductClassifiedModel;
import com.example.springboot_cy_marketplace.repository.IProductClassifiedRepository;
import com.example.springboot_cy_marketplace.repository.IProductRepository;
import com.example.springboot_cy_marketplace.services.AmazonClient;
import com.example.springboot_cy_marketplace.services.IProductClassifiedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductClassifiedServiceImpl implements IProductClassifiedService {
    @Autowired
    IProductClassifiedRepository iProductClassifiedRepository;
    @Autowired
    IProductRepository iProductRepository;
    @Autowired
    AmazonClient amazonClient;

    @Override
    public List<ProductClassifiedModel> findAll() {
        return null;
    }

    @Override
    public Page<ProductClassifiedModel> findAll(Pageable page) {
        return null;
    }

    @Override
    public ProductClassifiedModel findById(Long id) {
        return null;
    }

    /*
     * @author: HaiPhong
     * @since: 10/06/2022 4:36 CH
     * @description-VN:  Thêm mới sản phẩm đã phân loại
     * @description-EN:  Newly added classified products
     * @param: dto
     * @return: ProductClassifiedModel
     *
     * */
    @Override
    public ProductClassifiedModel add(ProductClassifiedDTO dto) {
        ProductClassifiedEntity entity = ProductClassifiedDTO.dtoToEntity(dto);
        entity.setImage(amazonClient.uploadFilewithFolder(dto.getImage(), "product-classified"));
        entity.setProductEntity(iProductRepository.findById(dto.getIdProduct()).get());
        entity.setCarts(null);
        return ProductClassifiedModel.entityToModel(iProductClassifiedRepository.save(entity));
    }

    @Override
    public List<ProductClassifiedModel> add(List<ProductClassifiedDTO> dto) {
        List<ProductClassifiedModel> classifiedModelList = new ArrayList<>();
        for (ProductClassifiedDTO productClassifiedDTO: dto) {
            ProductClassifiedEntity entity = ProductClassifiedDTO.dtoToEntity(productClassifiedDTO);
            entity.setImage(amazonClient.uploadFilewithFolder(productClassifiedDTO.getImage(), "product-classified"));
            entity.setProductEntity(iProductRepository.findById(productClassifiedDTO.getIdProduct()).get());
            iProductClassifiedRepository.save(entity);
            classifiedModelList.add(ProductClassifiedModel.entityToModel(iProductClassifiedRepository.save(entity)));
        }

        return classifiedModelList;

    }

    @Override
    public ProductClassifiedModel update(ProductClassifiedDTO dto) {
        return null;
    }

    @Override
    public boolean deleteById(Long id) {
        return false;
    }

    @Override
    public boolean deleteByIds(List<Long> id) {
        return false;
    }


    /*
     * @author: HaiPhong
     * @since: 10/06/2022 4:36 CH
     * @description-VN:  Tìm kiếm sản phẩm đã phân loại
     * @description-EN:  Search for classified products
     * @param:   classified01,classified02,id
     * @return:  ProductClassifiedModel
     *
     * */
    @Override
    public ProductClassifiedModel findByClassifiedName(String classified01, String classified02, Long id) {
        return ProductClassifiedModel.entityToModel(iProductClassifiedRepository.findByClassifiedName(classified01, classified02, id));
    }
}
