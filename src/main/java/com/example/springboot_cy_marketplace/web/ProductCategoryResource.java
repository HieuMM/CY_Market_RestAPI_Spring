package com.example.springboot_cy_marketplace.web;

import com.example.springboot_cy_marketplace.config.Position;
import com.example.springboot_cy_marketplace.dto.AddProductCategoryDTO;
import com.example.springboot_cy_marketplace.dto.ProductCategoryDTO;
import com.example.springboot_cy_marketplace.dto.ResponseDTO;
import com.example.springboot_cy_marketplace.model.ProductCategoryModel;
import com.example.springboot_cy_marketplace.services.impl.ProductCategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("/api/v1/product-category")
public class ProductCategoryResource {
    @Autowired
    ProductCategoryServiceImpl productCategoryService;

    /*
    * @author: Manh Tran
    * @since: 13/06/2022 9:55 SA
    * @description-VN: Lấy danh sách câu hỏi.
    * @description-EN: Get list of questions.
    * @param: pageable - Phân trang.
    * @return:
    *
    * */
    @GetMapping(value = "/find-all")
    public Object findAll(Pageable pageable){
        if(pageable == null){
            List<ProductCategoryDTO> findAll = this.productCategoryService.findAll();
            return ResponseDTO.show(200, "Find all product category", findAll);
        }else {
            Page<ProductCategoryDTO> findAll = this.productCategoryService.findAll(pageable);
            return ResponseDTO.show(200, "Find all product category with pagination", findAll);
        }
    }

    /*
    * @author: Manh Tran
    * @since: 13/06/2022 9:56 SA
    * @description-VN: Tìm danh mục sản phẩm theo id.
    * @description-EN: Find product category by id.
    * @param: id - Mã danh mục sản phẩm.
    * @return:
    *
    * */
    @GetMapping(value = "/find-by-id")
    public Object findById(@RequestParam(value = "id") Long id){
        ProductCategoryDTO result = this.productCategoryService.findById(id);
        if(result == null){
            return ResponseDTO.show(400, "Find product category by id", null);
        }else {
            return ResponseDTO.show(200, "Find product category by id", result);
        }
    }

    /*
    * @author: Manh Tran
    * @since: 13/06/2022 9:57 SA
    * @description-VN: Thêm danh mục sản phẩm.
    * @description-EN: Add product category.
    * @param: request - Thông tin danh mục sản phẩm.
    * @return:
    *
    * */
    @RolesAllowed(Position.SELLER)
    @PostMapping(value = "/add")
    public Object add(@RequestBody AddProductCategoryDTO request){
        ProductCategoryDTO result = this.productCategoryService.add(request);
        if(result != null){
            return ResponseDTO.show(200, "Add new product category", result);
        }else {
            return ResponseDTO.show(400, "Add new product category", null);
        }
    }

    /*
    * @author: Manh Tran
    * @since: 13/06/2022 9:57 SA
    * @description-VN: Thêm nhiều danh mục sản phẩm 1 lúc.
    * @description-EN: Add many product category at once.
    * @param: dtoList - Danh sách các danh mục sản phẩm.
    * @return:
    *
    * */
    @RolesAllowed(Position.SELLER)
    @PostMapping(value = "/add-list")
    public Object addList(@RequestBody List<ProductCategoryDTO> dtoList){
        Boolean result = this.productCategoryService.addListCategory(dtoList);
        if(result){
            return ResponseDTO.show(200, "Add list new product categories", dtoList);
        }else {
            return ResponseDTO.show(400, "Add list new product categories", null);
        }
    }

    /*
    * @author: Manh Tran
    * @since: 13/06/2022 9:58 SA
    * @description-VN: Cập nhật danh mục sản phẩm.
    * @description-EN: Update product category.
    * @param: dto - Thông tin danh mục sản phẩm.
    * @return:
    *
    * */
    @RolesAllowed(Position.SELLER)
    @PostMapping(value = "/update")
    public Object update(@RequestBody ProductCategoryDTO dto){
        ProductCategoryDTO result = this.productCategoryService.update(dto);
        if(result != null){
            return ResponseDTO.show(200, "Update product category", result);
        }else {
            return ResponseDTO.show(400, "Update product category", null);
        }
    }

    /*
    * @author: Manh Tran
    * @since: 13/06/2022 9:59 SA
    * @description-VN: Xoá danh mục sản phẩm theo mã.
    * @description-EN: Delete product category by id.
    * @param: id - Mã danh mục sản phẩm.
    * @return:
    *
    * */
//    @RolesAllowed(Position.ADMIN)
    @DeleteMapping(value = "/delete-by-id")
    public Object deleteById(@RequestParam(value = "id")Long id){
        Boolean result = this.productCategoryService.deleteById(id);
        if(result){
            return ResponseDTO.show(200, "Delete product category by id", id);
        }else {
            return ResponseDTO.show(400, "Delete product category by id", null);
        }
    }

    /*
    * @author: Manh Tran
    * @since: 13/06/2022 9:59 SA
    * @description-VN: Xoá nhiều danh mục sản phẩm cùng một lúc.
    * @description-EN: Delete many product category at once.
    * @param: ids - Danh sách mã danh mục sản phẩm.
    * @return:
    *
    * */
    @RolesAllowed(Position.ADMIN)
    @DeleteMapping(value = "/delete-by-list-id")
    public Object deleteById(@RequestBody List<Long> ids){
        Boolean result = this.productCategoryService.deleteByIds(ids);
        if(result){
            return ResponseDTO.show(200, "Delete product category by list id", ids);
        }else {
            return ResponseDTO.show(400, "Delete product category by list id", null);
        }
    }

    @PostMapping("/hide-category/{id}")
    public Object hideCategory(@PathVariable(value = "id") Long id){
        return ResponseDTO.of(productCategoryService.hideProductCategory(id),"Hide category");
    }

    /*
    * @author: Manh Tran
    * @since: 13/06/2022 9:59 SA
    * @description-VN: Tìm tất cả danh mục cha
    * @description-EN: Find all parent category.
    * @param: pageable - Phân trang
    * @return:
    *
    * */
    @GetMapping(value = "/find-all-parent")
    public Object findAllParentCategory(Pageable pageable){
        if(pageable == null) {
            return ResponseDTO.show(200, "Find all parent categories",
                    this.productCategoryService.findAllParentCategory());
        }else {
            return ResponseDTO.show(200, "Find all parent categories with pagination",
                    this.productCategoryService.findAllParentCategory(pageable));
        }
    }

    /*
    * @author: Manh Tran
    * @since: 13/06/2022 10:00 SA
    * @description-VN: Tìm tất cả danh mục con.
    * @description-EN: Find all child category.
    * @param: parentId - Mã danh mục cha, pageable - Phân trang.
    * @return:
    *
    * */
    @GetMapping(value = "/find-all-child")
    public Object findAllChildCategory(@RequestParam(value = "parentId")Long parentId, Pageable pageable){
        if(pageable == null){
            return ResponseDTO.show(200, "Find all child categories by parent category id",
                    this.productCategoryService.findAllChildCategoryByParentId(parentId));
        }else {
            return ResponseDTO.show(200, "Find all child categories by parent category id",
                    this.productCategoryService.findAllChildCategoryByParentId(parentId, pageable));
        }
    }

    /*
    * @author: Manh Tran
    * @since: 13/06/2022 10:00 SA
    * @description-VN: Tìm tất cả sản phẩm theo mã danh mục cha hoặc con
    * @description-EN: Find all product by category id.
    * @param: id - Mã danh mục, pageable - Phân trang.
    * @return:
    *
    * */
    @GetMapping(value = "/find-all-products")
    public Object findAllProducts(@RequestParam(value = "id") Long id, Pageable pageable){
        return ResponseDTO.show(200, "Find all products by category id", this.productCategoryService.findAllProducts(id, pageable));
    }
/*
* @author: HieuMM
* @since: 29-Jun-22 11:01 AM
* @description-VN:  Danh muc cha cua san pham
* @description-EN:
* @param:
* */
    @GetMapping("/total-parent-category")
    public Object totalPaCate() {
        return ResponseDTO.of(productCategoryService.countCategoryParent(), "Category parent");
    }
    /*
    * @author: HieuMM
    * @since: 29-Jun-22 11:02 AM
    * @description-VN:  Danh muc con cua danh muc cha
    * @description-EN:
    * @param:
    * */
    @GetMapping("/total-parent-category/{id}")
    public Object averageRate(@PathVariable(value = "id") Long id) {
        return ResponseDTO.of(productCategoryService.countCategoryChild(id), "Category child follow parent");
    }
}

