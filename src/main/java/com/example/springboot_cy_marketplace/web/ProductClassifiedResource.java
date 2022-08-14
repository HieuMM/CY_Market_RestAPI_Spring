package com.example.springboot_cy_marketplace.web;

import com.example.springboot_cy_marketplace.config.Position;
import com.example.springboot_cy_marketplace.dto.*;
import com.example.springboot_cy_marketplace.services.AmazonClient;
import com.example.springboot_cy_marketplace.services.IProductClassifiedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/productclassified")
public class ProductClassifiedResource {
    @Autowired
    IProductClassifiedService iProductClassifiedService;
    @Autowired
    AmazonClient amazonClient;

    /*
     * @author: HaiPhong
     * @since: 10/06/2022 3:31 CH
     * @description-VN:  Lưu sản phẩm được phân loại theo sản phẩm gốc
     * @description-EN:  Save products sorted by original product
     * @param: productClassifiedDTO
     * @return:
     *
     * */
    @RolesAllowed(Position.SELLER)
    @PostMapping
    public Object saveProductClassified(ProductClassifiedDTO classifiedDTOList) {
        return ResponseDTO.of(iProductClassifiedService.add(classifiedDTOList), "Save product classified ");
    }


    /*
     * @author: HaiPhong
     * @since: 10/06/2022 3:32 CH
     * @description-VN:  Tìm kiếm sản phẩm đã được phân loại
     * @description-EN:  Search for products that have been classified
     * @param:   classified01, classified02, id
     * @return:
     *
     * */
    @GetMapping("/find-by-classifiedname")
    public Object findByClassified1and2(@RequestParam(value = "classified01") String classified01,
                                        @RequestParam(value = "classified02") String classified02,
                                        @RequestParam(value = "idProduct") Long id) {
        return ResponseDTO.of(iProductClassifiedService.findByClassifiedName(classified01, classified02, id), "Find product classified by classified name");
    }


}
