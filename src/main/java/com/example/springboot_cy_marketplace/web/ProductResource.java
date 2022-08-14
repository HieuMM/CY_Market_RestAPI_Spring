package com.example.springboot_cy_marketplace.web;

import com.example.springboot_cy_marketplace.config.Position;
import com.example.springboot_cy_marketplace.dto.AdminManageProductDTO;
import com.example.springboot_cy_marketplace.dto.ProductDTO;
import com.example.springboot_cy_marketplace.dto.ResponseDTO;
import com.example.springboot_cy_marketplace.dto.UpdateProductDTO;
import com.example.springboot_cy_marketplace.model.ProductModel;
import com.example.springboot_cy_marketplace.services.IProductService;
import com.example.springboot_cy_marketplace.services.impl.ProductServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
public class ProductResource {
    @Autowired
    IProductService iProductService;
    @Autowired
    ProductServiceImpl productService;

    /*
     * @author: HaiPhong
     * @since: 10/06/2022 3:35 CH
     * @description-VN:  Lấy dang sách toàn bộ sản phẩm
     * @description-EN:  Get a list of all products
     * @param: pageable
     * @return:
     *
     * */
    @GetMapping
    public Object getProduct(Pageable pageable) {
        return ResponseDTO.of(iProductService.findAll(pageable), "Find all product");
    }

    /*
     * @author: HaiPhong
     * @since: 10/06/2022 3:36 CH
     * @description-VN:  Lưu một sản phẩm
     * @description-EN:  Save a product
     * @param: productDTO
     * @return:
     *
     * */
//    @RolesAllowed(Position.ADMIN)
    @PostMapping
    public Object saveProduct(ProductDTO productDTO) {
        return ResponseDTO.of(iProductService.add(productDTO), "Add product");
    }

    /*
     * @author: HaiPhong
     * @since: 10/06/2022 3:38 CH
     * @description-VN:  Chỉnh sửa thông tin của một sản phẩm
     * @description-EN:  Edit information of a product
     * @param:   productDTO
     * @return:
     *
     * */
//    @RolesAllowed(Position.ADMIN)
    @PutMapping
    public Object updateProduct(UpdateProductDTO productDTO) {
        return ResponseDTO.of(iProductService.updateProduct(productDTO), "Update product");
    }

    /*
     * @author: HaiPhong
     * @since: 10/06/2022 3:39 CH
     * @description-VN:  Xóa sản phẩm
     * @description-EN:  Delete product
     * @param: id
     * @return:
     *
     * */
    @RolesAllowed(Position.ADMIN)
    @DeleteMapping("/{id}")
    public Object deleteProduct(@PathVariable(value = "id") Long id) {
        return ResponseDTO.of(iProductService.deleteById(id), "Delete product");
    }

    /*
     * @author: HaiPhong
     * @since: 10/06/2022 3:40 CH
     * @description-VN:  Tìm kiếm một sản phẩm theo id
     * @description-EN:  Search for a product by id
     * @param: id
     * @return:
     *
     * */
    @GetMapping("/find-by-id/{id}")
    public Object findById(@PathVariable(value = "id") Long id) {
        return ResponseDTO.of(iProductService.findById(id), "Find product by id");
    }

    @GetMapping("/find-product-update/{id}")
    public Object findProductUpdate(@PathVariable(value = "id") Long id) {
        return ResponseDTO.of(iProductService.findProductToUpdate(id), "Find product to update by id");
    }

    /*
     * @author: HaiPhong
     * @since: 10/06/2022 3:41 CH
     * @description-VN:  Lấy danh sách sản phẩm theo địa chỉ đăng bán
     * @description-EN:  Get a list of products according to the address posted for sale
     * @param: pageable, id
     * @return:
     *
     * */
    @GetMapping("/findAllByProviderId/{id}")
    public Object findAllByProviderId(Pageable pageable, @PathVariable(value = "id") Long id) {
        return ResponseDTO.of(productService.findAllByProviderId(pageable, id), "findAllByProviderId");
    }

    /*
     * @author: HaiPhong
     * @since: 10/06/2022 3:42 CH
     * @description-VN:  Lấy danh sách sản phẩm theo giá mới
     * @description-EN:  Get product list by new price
     * @param: page, priceFrom, priceTo
     * @return:
     *
     * */
    @GetMapping("/findAllByNewPrice/{idCategory}/{priceFrom}/{priceTo}")
    public Object findAllByNewPrice(Pageable page, @PathVariable(value = "priceFrom") int priceFrom, @PathVariable(value = "priceTo") int priceTo, @PathVariable(value = "idCategory") Long id_category) {
        return ResponseDTO.of(productService.findAllByNewPrice(page, priceFrom, priceTo, id_category), "findAllByNewPrice");
    }

    /*
     * @author: HaiPhong
     * @since: 10/06/2022 3:43 CH
     * @description-VN:  Lấy danh sách sản phẩm theo trạng thái của sản phẩm
     * @description-EN:  Get product list by product's status
     * @param: page, status
     * @return:
     *
     * */
    @GetMapping("/findAllByStatus/{idCategory}/{status}")
    public Object findAllByStatus(Pageable page, @PathVariable(value = "status") Boolean status, @PathVariable(value = "idCategory") Long id_category) {
        return ResponseDTO.of(productService.findAllByStatus(page, status, id_category), "findAllByStatus");
    }

    /*
     * @author: HieuMM
     * @since: 16-Jun-22 1:19 PM
     * @description-VN: gợi ý sản phẩm tương tự
     * @description-EN: suggestion product
     * @param:
     * */
    @GetMapping("/suggestionProduct/{id}")
    public Object suggestionProduct(Pageable pageable, @PathVariable(value = "id") Long id) {
        ProductModel product = iProductService.findById(id);
        String name = product.getName();
        String[] words = name.split("\\s");
        String key1 = words[0];
        String key2 = words[1];
        return ResponseDTO.of(productService.suggestionProduct(pageable, key1, key2), "suggestion product");
    }

    /*
     * @author: Manh Tran
     * @since: 13/06/2022 2:42 CH
     * @description-VN: Lấy danh sách sản phẩm theo mã người bán.
     * @description-EN: Get list product by seller id.
     * @param: id - Mã người bán, pageable - Phân trang.
     * @return:
     *
     * */
    @GetMapping(value = "/find-all-products-by-seller-id")
    public Object findAllProductsBySellerId(@RequestParam(value = "id") Long sellerId, Pageable pageable) {
        return ResponseDTO.show(200, "Find all products by seller id", this.productService.findAllProductsBySellerId(sellerId, pageable));
    }

    /*
     * @author: HaiPhong
     * @since: 15/06/2022 4:06 CH
     * @description-VN:  Tìm kiếm sản phẩm theo tên sản phẩm hoặc tên danh mục sản phẩm
     * @description-EN:  Search products by product name or product category name
     * @param: search,pageable
     * @return:
     *
     * */
    @GetMapping("/home-search")
    public Object searchProduct(@RequestParam(value = "search") String search, Pageable pageable) {
        return ResponseDTO.of(productService.searchAllProduct(search, pageable), "Search product");
    }

    /*
     * @author: HaiPhong
     * @since: 22/06/2022 9:39 SA
     * @description-VN:  Tìm kiếm sản phẩm trong một danh mục sản phẩm
     * @description-EN:  Search for products in a product category
     * @param: id_category, pageable
     * @return:
     *
     * */
    @GetMapping("/search-in-category")
    public Object findProdcutByNameInProductCategory(
            @RequestParam(value = "search") String search,
            @RequestParam(value = "id_category") int id_category, Pageable pageable) {
        return ResponseDTO.of(productService.findAllProductByIdNameAndCategory(search, (long) id_category, pageable), "Search product in product category");
    }

    /*
     * @author: HaiPhong
     * @since: 22/06/2022 9:38 SA
     * @description-VN:  Thay đổi trạng thái ẩn hiện của sản phẩm
     * @description-EN:  Change the hidden status of the product
     * @param: id
     * @return:
     *
     * */
    @RolesAllowed(Position.ADMIN)
    @PostMapping("/hide-product/{id}")
    public Object hideProduct(@PathVariable(value = "id") Long id) {
        return ResponseDTO.of(productService.hideProduct(id), "Hide product");
    }

    /*
     * @author: Manh Tran
     * @since: 29/06/2022 10:12 SA
     * @description-VN: Danh sách sản phẩm theo trạng thái (đang bán, bị khoá, sắp hết hàng, hết hàng).
     * @description-EN: List of products by status (selling, locked, out of stock, sold out).
     * @param: statusId - mã yêu cầu tương ứng (1,2,3,4).
     * @param: pageable - phân trang.
     * @return:
     *
     * */
    @GetMapping("/find-all-products-by-status")
    public Object findAllProductsByStatus(@RequestParam(value = "statusId") int statusId, Pageable pageable) {
        Page<ProductModel> result = productService.findAllProductByStatus(statusId, pageable);
        int code = result != null ? (result.getTotalElements() > 0 ? 200 : 404) : 400;
        return ResponseDTO.show(code, "Find all products by status", code == 200 ? result : null);
    }

    /*
    * @author: Manh Tran
    * @since: 29/06/2022 11:28 SA
    * @description-VN: Các sản phẩm bán chạy nhất.
    * @description-EN: List most sold products.
    * @param: top - số lượng sản phẩm muốn lấy.
    * @return:
    *
    * */
    @GetMapping("/find-top-products")
    public Object findTopSoldProducts(@RequestParam(value = "top") int top, Pageable pageable) {
        Page<ProductModel> result = productService.findTopProducts(top, pageable);
        int code = result != null ? (result.getTotalElements() > 0 ? 200 : 404) : 400;
        return ResponseDTO.show(code,"Find top sold products", code == 200 ? result : null);
    }

    /*
    * @author: Manh Tran
    * @since: 29/06/2022 4:16 CH
    * @description-VN: Các sản phẩm có lượt xem nhiều nhất.
    * @description-EN: Products with most views.
    * @param:
    * @return:
    *
    * */
    @GetMapping("/find-top-viewed-products")
    public Object findTopViewedProducts(@RequestParam(value = "top")int top, Pageable pageable) {
        Page<ProductModel> result = productService.findTopViewedProducts(top, pageable);
        int code = result != null ? (result.getTotalElements() > 0 ? 200 : 404) : 400;
        return ResponseDTO.show(code,"Find top viewed products", code == 200 ? result : null);
    }

    /*
    * @author: Manh Tran
    * @since: 30/06/2022 9:09 SA
    * @description-VN: Thống kê chung về sản phẩm.
    * @description-EN: Statistical about products.
    * @param:
    * @return:
    *
    * */
    @GetMapping("/statistical-products")
    public Object statisticalProducts(@RequestParam(value = "startDayTime", required = false) String startDayTime,
                                      @RequestParam(value = "endDayTime", required = false) String endDayTime) {
        return ResponseDTO.of(productService.statisticalProducts(startDayTime, endDayTime), "Statistical products");
    }

    /*
     * @author: Manh Tran
     * @since: 30/06/2022 10:51 SA
     * @description-VN: Lọc sản phẩm theo số lượng đã bán.
     * @description-EN: Filter products by amount sold.
     * @param:
     * @return:
     *
     * */
    @GetMapping("/filter-products-by-amount-sold")
    public Object filterProductsByAmountSold(@RequestParam(value = "min", required = false) int min,
                                             @RequestParam(value = "max", required = false) int max,
                                             Pageable pageable) {
        Page<ProductModel> result = productService.filterProductsByAmountSold(min, max, pageable);
        int code = result != null ? (result.getTotalElements() > 0 ? 200 : 404) : 400;
        return ResponseDTO.show(code,"Filter products by amount sold", code == 200 ? result : null);
    }

}
