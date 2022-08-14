package com.example.springboot_cy_marketplace.services.impl;

import com.example.springboot_cy_marketplace.dto.AddProductCategoryDTO;
import com.example.springboot_cy_marketplace.dto.ProductCategoryDTO;
import com.example.springboot_cy_marketplace.entity.ProductCategoryEntity;
import com.example.springboot_cy_marketplace.entity.ProductEntity;
import com.example.springboot_cy_marketplace.model.ProductCategoryModel;
import com.example.springboot_cy_marketplace.model.ProductModel;
import com.example.springboot_cy_marketplace.repository.IProductCategoryRepository;
import com.example.springboot_cy_marketplace.repository.IProductRepository;
import com.example.springboot_cy_marketplace.services.IProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ProductCategoryServiceImpl implements IProductCategoryService {
    @Autowired
    IProductCategoryRepository productCategoryRepository;

    @Autowired
    IProductRepository productRepository;

    /*
    * @author: Manh Tran
    * @since: 13/06/2022 10:09 SA
    * @description-VN: Lấy danh sách danh mục sản phẩm, không có phân trang.
    * @description-EN: Get list of product category, no pagination.
    * @param:
    * @return:
    *
    * */
    @Override
    public List<ProductCategoryDTO> findAll() {
        List<ProductCategoryModel> models = productCategoryRepository.findAll().stream().map(this::entityToModel).collect(Collectors.toList());
        return models.stream().map(ProductCategoryModel::modelToDTO).collect(Collectors.toList());
    }

    /*
    * @author: Manh Tran
    * @since: 13/06/2022 10:10 SA
    * @description-VN: Lấy danh sách danh mục sản phẩm, có phân trang.
    * @description-EN: Get list of product category, pagination.
    * @param: page - phân trang.
    * @return:
    *
    * */
    @Override
    public Page<ProductCategoryDTO> findAll(Pageable page) {
        List<ProductCategoryModel> models = productCategoryRepository.findAll(page).stream().map(this::entityToModel).collect(Collectors.toList());
        List<ProductCategoryDTO> dtos = models.stream().map(ProductCategoryModel::modelToDTO).collect(Collectors.toList());
        return new PageImpl<>(dtos);
    }

    /*
    * @author: Manh Tran
    * @since: 13/06/2022 10:11 SA
    * @description-VN: Tìm danh mục sản phẩm theo id.
    * @description-EN: Find product category by id.
    * @param: id - Mã danh mục sản phẩm muốn tìm.
    * @return:
    *
    * */
    @Override
    public ProductCategoryDTO findById(Long id) {
        Optional<ProductCategoryEntity> optionalProductCategoryEntity = productCategoryRepository.findById(id);
        return optionalProductCategoryEntity.map(productCategoryEntity -> ProductCategoryModel.modelToDTO(this.entityToModel(productCategoryEntity))).orElse(null);
    }

    /*
    * @author: Manh Tran
    * @since: 13/06/2022 10:11 SA
    * @description-VN: Thêm danh mục sản phẩm mới.
    * @description-EN: Add new product category.
    * @param: request - Thông tin danh mục sản phẩm mới.
    * @return:
    *
    * */
    public ProductCategoryDTO add(AddProductCategoryDTO request) {
        if(this.productCategoryRepository.findByNameIgnoreCase(request.getName()) != null){
            return null;
        }
        if(this.findById(request.getParentId()) == null){
            return null;
        }
        ProductCategoryModel model = ProductCategoryDTO.dtoToModel(AddProductCategoryDTO.requestToDTO(request));
        ProductCategoryEntity result = this.productCategoryRepository.save(this.modelToEntity(model));
        if(result != null){
            model.setParentName(this.productCategoryRepository.findById(request.getParentId()).get().getName());
            model.setParentAvatarUrl(this.productCategoryRepository.findById(request.getParentId()).get().getAvatarUrl());
            return ProductCategoryModel.modelToDTO(model);
        }
        return null;
    }

    /*
    * @author: Manh Tran
    * @since: 13/06/2022 10:12 SA
    * @description-VN: Thêm nhiều danh mục sản phẩm cùng lúc.
    * @description-EN: Add multiple product category at once.
    * @param: dtoList - Danh sách thông tin danh mục sản phẩm mới.
    * @return:
    *
    * */
    public boolean addListCategory(List<ProductCategoryDTO> dtoList){
        for(ProductCategoryDTO dto : dtoList){
            if(this.productCategoryRepository.findByNameIgnoreCase(dto.getName()) != null){
                return false;
            }
            if(dto.getParentId() != null){
                if(this.findById(dto.getParentId()) == null){
                    return false;
                }
            }
        }
        for(ProductCategoryDTO dto : dtoList){
            ProductCategoryModel model = ProductCategoryDTO.dtoToModel(dto);
            ProductCategoryEntity result = this.productCategoryRepository.save(this.modelToEntity(model));
            if(result == null){
                return false;
            }
        }
        return true;
    }

    /*
    * @author: Manh Tran
    * @since: 13/06/2022 10:12 SA
    * @description-VN: Cập nhật danh mục sản phẩm.
    * @description-EN: Update product category.
    * @param: dto - Thông tin danh mục sản phẩm mới.
    * @return:
    *
    * */
    public ProductCategoryDTO update(ProductCategoryDTO dto){
        if(dto.getId() == null){
            return null;
        }
        if(this.productCategoryRepository.findByNameIgnoreCase(dto.getName()) != null){
            return null;
        }
        ProductCategoryDTO result = this.findById(dto.getId());
        if(result == null){
            return null;
        }
        boolean isParent = true;
        Optional<ProductCategoryEntity> parentEntity = Optional.empty();
        if(dto.getParentId() != null){
            isParent = false;
            parentEntity = this.productCategoryRepository.findById(dto.getParentId());
            if(parentEntity.isEmpty()){
                return null;
            }
        }
        ProductCategoryModel model = ProductCategoryDTO.dtoToModel(result);
        model.setName(dto.getName());
        model.setAvatarUrl(dto.getAvatarUrl());
        if(!isParent){
            model.setParentId(dto.getParentId());
            model.setParentName(parentEntity.get().getName());
            model.setParentAvatarUrl(parentEntity.get().getAvatarUrl());
        }
        ProductCategoryEntity saveResult = this.productCategoryRepository.save(this.modelToEntity(model));
        if(saveResult != null){
                return ProductCategoryModel.modelToDTO(model);
        }else {
            return null;
        }
    }

    /*
    * @author: Manh Tran
    * @since: 13/06/2022 10:12 SA
    * @description-VN: Xoá danh mục sản phẩm theo mã.
    * @description-EN: Delete product category by id.
    * @param: id - Mã danh mục sản phẩm muốn xoá.
    * @return:
    *
    * */
    @Override
    public boolean deleteById(Long id) {
        if(this.findById(id) == null){
            return false;
        }
        List<ProductCategoryDTO> childCategories = this.findAllChildCategoryByParentId(id);
        this.updateChildCategory(childCategories);
        this.updateProductBeforeDeleteProductCategory(id);
        this.productCategoryRepository.deleteById(id);
        return this.findById(id) == null;
    }

    /*
    * @author: Manh Tran
    * @since: 13/06/2022 10:13 SA
    * @description-VN: Xoá nhiều danh mục sản phẩm cùng lúc.
    * @description-EN: Delete multiple product category at once.
    * @param: ids - Danh sách mã danh mục sản phẩm muốn xoá.
    * @return:
    *
    * */
    @Override
    public boolean deleteByIds(List<Long> ids) {
        for(Long id : ids){
            if(this.findById(id) == null){
                return false;
            }
            this.productCategoryRepository.deleteById(id);
            if(this.findById(id) != null){
                return false;
            }
        }
        return false;
    }

    /*
    * @author: Manh Tran
    * @since: 13/06/2022 10:13 SA
    * @description-VN: Cập nhật lại danh mục cha thành null của các danh mục con (khi xoá danh mục cha).
    * @description-EN: Update child category parent id to null when delete parent category.
    * @param: childList - Danh sách danh mục con muốn cập nhật.
    * @return:
    *
    * */
    public void updateChildCategory(List<ProductCategoryDTO> childList){
        if(childList == null || childList.size() == 0){
            return;
        }
        for(ProductCategoryDTO child : childList){
            child.setParentId(347L);
            this.productCategoryRepository.save(this.modelToEntity(ProductCategoryDTO.dtoToModel(child)));
        }
    }

    public void updateProductBeforeDeleteProductCategory(Long id_productCategory){
        List<ProductEntity> productEntityList = productRepository.findAllByProductCategoryEntity(productCategoryRepository.findById(id_productCategory).get());
        ProductCategoryEntity productCategoryEntity = productCategoryRepository.findById(347L).get();
        for (ProductEntity pro : productEntityList) {
                pro.setProductCategoryEntity(productCategoryEntity);
                productRepository.save(pro);
            }

    }

    /*
    * @author: Manh Tran
    * @since: 13/06/2022 10:14 SA
    * @description-VN: Tìm tất cả danh mục cha, có phân trang.
    * @description-EN: Find all parent category, without pagination.
    * @param:
    * @return:
    *
    * */
    public List<ProductCategoryDTO> findAllParentCategory(){
        List<ProductCategoryEntity> parentCategoryList = this.productCategoryRepository.findAllByParentCategoryIsNull();
        List<ProductCategoryModel> models = parentCategoryList.stream().map(this::entityToModel).collect(Collectors.toList());
        return models.stream().map(ProductCategoryModel::modelToDTO).collect(Collectors.toList());
    }

    /*
    * @author: Manh Tran
    * @since: 13/06/2022 10:15 SA
    * @description-VN: Tìm tất cả danh mục cha, có phân trang.
    * @description-EN: Find all parent category, with pagination.
    * @param: pageable - Thông tin phân trang.
    * @return:
    *
    * */
    public Page<ProductCategoryDTO> findAllParentCategory(Pageable pageable){
        List<ProductCategoryModel> models = this.productCategoryRepository.findAllByParentCategoryIsNull(pageable)
                .stream().map(this::entityToModel).collect(Collectors.toList());
        List<ProductCategoryDTO> dtos = models.stream().map(ProductCategoryModel::modelToDTO).collect(Collectors.toList());
        return new PageImpl<>(dtos);
    }

    /*
    * @author: Manh Tran
    * @since: 13/06/2022 10:16 SA
    * @description-VN: Tìm tất cả danh mục con theo mã danh mục cha, không có phân trang.
    * @description-EN: Find all child category by parent id, without pagination.
    * @param: id - Mã danh mục cha.
    * @return:
    *
    * */
    public List<ProductCategoryDTO> findAllChildCategoryByParentId(Long id){
        if(this.findById(id) == null){
            return null;
        }
        List<ProductCategoryEntity> childCategory = this.productCategoryRepository.findAllByParentCategoryId(id);
        List<ProductCategoryModel> models = childCategory.stream().map(this::entityToModel).collect(Collectors.toList());
        return models.stream().map(ProductCategoryModel::modelToDTO).collect(Collectors.toList());
    }

    /*
    * @author: Manh Tran
    * @since: 13/06/2022 10:16 SA
    * @description-VN: Tìm tất cả danh mục con theo mã danh mục cha, có phân trang.
    * @description-EN: Find all child category by parent id, with pagination.
    * @param: id - Mã danh mục cha, pageable - Thông tin phân trang.
    * @return:
    *
    * */
    public Page<ProductCategoryDTO> findAllChildCategoryByParentId(Long id, Pageable pageable){
        if(this.findById(id) == null){
            return null;
        }
        Page<ProductCategoryEntity> childCategory = this.productCategoryRepository.findAllByParentCategoryId(id, pageable);
        List<ProductCategoryModel> models = childCategory.stream().map(this::entityToModel).collect(Collectors.toList());
        return new PageImpl<>(models.stream().map(ProductCategoryModel::modelToDTO).collect(Collectors.toList()));
    }

    /*
    * @author: Manh Tran
    * @since: 13/06/2022 10:17 SA
    * @description-VN: Lấy danh sách sản phẩm theo mã danh mục cha/con, có phân trang.
    * @description-EN: Get list product by category id, with pagination.
    * @param: categoryId - Mã danh mục, pageable - Thông tin phân trang.
    * @return:
    *
    * */
    public Page<ProductModel> findAllProducts(Long categoryId, Pageable pageable){
        //Kiểm tra xem id có hợp lệ hay không
        Optional<ProductCategoryEntity> productCategoryEntityOptional = this.productCategoryRepository.findById(categoryId);
        if(productCategoryEntityOptional.isEmpty()){
            return null;
        }
        //Là danh mục cha
        if(productCategoryEntityOptional.get().getParentCategory() == null){
            //Từ mã danh mục cha lấy ra các danh mục con
            List<ProductCategoryEntity> childCategories = this.productCategoryRepository.findAllByParentCategoryId(categoryId);
            //Là danh mục cha nhưng không có danh mục con
            //Sản phẩm chỉ lưu theo mã danh mục con -> không có dữ liệu trả về
            if(childCategories == null || childCategories.size() == 0){
                return null;
            }
            List<ProductEntity> totalProducts = new ArrayList<>();
            for(ProductCategoryEntity child : childCategories){
                List<ProductEntity> products = this.productRepository.findAllByProductCategoryEntity(child);
                totalProducts = Stream.concat(totalProducts.stream(), products.stream()).collect(Collectors.toList());
            }
            List<ProductModel> models = totalProducts.stream().map(ProductModel::entityToModel).collect(Collectors.toList());
            //Trả về danh sách các sản phẩm thuộc danh mục đó
            final long start = pageable.getOffset();
            final long end = Math.min((start + pageable.getPageSize()), models.size());
            return new PageImpl<>(models.subList((int) start, (int)end), pageable, models.size());
        }else { //Là danh mục con
            List<ProductEntity> childProducts = this.productRepository.findAllByProductCategoryEntity(productCategoryEntityOptional.get());
            List<ProductModel> result = childProducts.stream().map(ProductModel::entityToModel).collect(Collectors.toList());
            final long start = pageable.getOffset();
            final long end = Math.min((start + pageable.getPageSize()), result.size());
            return new PageImpl<>(result.subList((int) start, (int)end), pageable, childProducts.size());
        }
    }

    /*
    * @author: Manh Tran
    * @since: 13/06/2022 10:18 SA
    * @description-VN: Chuyển entity về model.
    * @description-EN: Convert entity to model.
    * @param: entity - Entity muốn chuyển về model.
    * @return:
    *
    * */
    public ProductCategoryModel entityToModel(ProductCategoryEntity entity){
        ProductCategoryModel model = new ProductCategoryModel();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setAvatarUrl(entity.getAvatarUrl());
        if(entity.getParentCategory() != null) {
            model.setParentId(entity.getParentCategory().getId());
            model.setParentName(entity.getParentCategory().getName());
            model.setParentAvatarUrl(entity.getParentCategory().getAvatarUrl());
        }
        return model;
    }

    /*
    * @author: Manh Tran
    * @since: 13/06/2022 10:18 SA
    * @description-VN: Chuyển model về entity.
    * @description-EN: Convert model to entity.
    * @param: model - Model muốn chuyển về entity.
    * @return:
    *
    * */
    public ProductCategoryEntity modelToEntity(ProductCategoryModel model){
        ProductCategoryEntity entity = new ProductCategoryEntity();
        entity.setId(model.getId());
        entity.setName(model.getName());
        entity.setAvatarUrl(model.getAvatarUrl());
        if(model.getParentId() != null) {
            entity.setParentCategory(this.productCategoryRepository.findById(model.getParentId()).orElse(null));
        }
        return entity;
    }

    @Override
    public ProductCategoryDTO add(ProductCategoryModel dto) {
        return null;
    }

    @Override
    public List<ProductCategoryDTO> add(List<ProductCategoryModel> dto) {
        return null;
    }

    @Override
    public ProductCategoryDTO update(ProductCategoryModel dto) {
        return null;
    }

    @Override
    public ProductCategoryModel hideProductCategory(Long id) {
        ProductCategoryEntity productCategoryEntity = productCategoryRepository.findById(id).get();
        productCategoryRepository.save(productCategoryEntity);
        return ProductCategoryModel.entityToModel(productCategoryEntity);
    }

    public Object countCategoryParent() {
        int sum= productCategoryRepository.categoryParent();
        return sum;
    }

    public Object countCategoryChild(Long id) {
        int sum= productCategoryRepository.categoryChild(id);
        return sum;
    }
}
