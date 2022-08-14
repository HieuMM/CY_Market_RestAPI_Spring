package com.example.springboot_cy_marketplace.web;

import com.example.springboot_cy_marketplace.config.Position;
import com.example.springboot_cy_marketplace.dto.AddToCartDTO;
import com.example.springboot_cy_marketplace.dto.CartDTO;
import com.example.springboot_cy_marketplace.dto.ResponseDTO;
import com.example.springboot_cy_marketplace.services.impl.CartServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/cart")
public class CartResource {
    @Autowired
    private CartServiceImpl cartService;

    /*
     * @author: Manh Tran
     * @since: 17/06/2022 5:35 SA
     * @description-VN: Thêm sản phẩm vào giỏ hàng.
     * @description-EN: Add product to cart.
     * @param:
     * @return:
     *
     * */
    @RolesAllowed(Position.BUYER)
    @PostMapping(value = "/add")
    public Object addToCart(@RequestBody AddToCartDTO add) {
        boolean result = cartService.addProductToCart(add);
        return ResponseDTO.show(result ? 200 : 400, "Add product to cart", result ? true : null);
    }

    /*
    * @author: Manh Tran
    * @since: 24/06/2022 3:32 CH
    * @description-VN: Thêm nhiều sản phẩm vào giỏ hàng.
    * @description-EN: Add multiple products to cart.
    * @param:
    * @return:
    *
    * */
    @RolesAllowed(Position.BUYER)
    @PostMapping(value = "/add-multiple")
    public Object addMultipleToCart(@RequestBody List<AddToCartDTO> adds) {
        boolean result = cartService.addMultipleProductsToCart(adds);
        return ResponseDTO.show(result ? 200 : 400, "Add multiple products to cart",
                result ? true : null);
    }
    /*
     * @author: Manh Tran
     * @since: 17/06/2022 5:35 SA
     * @description-VN: Cập nhật giỏ hàng.
     * @description-EN:
     * @param:
     * @return:
     *
     * */
    @RolesAllowed(Position.BUYER)
    @PostMapping(value = "/update")
    public Object updateCart(@RequestBody AddToCartDTO update) {
        boolean result = cartService.updateCart(update);
        return ResponseDTO.show(result ? 200 : 400, "Update cart",
                result ? true : null);
    }

    /*
     * @author: Manh Tran
     * @since: 17/06/2022 5:34 SA
     * @description-VN: Lấy thông tin giỏ hàng theo mã người dùng.
     * @description-EN: Get
     * @param:
     * @return:
     *
     * */
    @GetMapping("/find-by-user-id")
    public Object findByUserId(@RequestParam(value = "userId") Long userId) {
        CartDTO cartDTO = cartService.findByUserId(userId);
        return ResponseDTO.show(cartDTO == null ? 400 : 200, "Get cart by user id", cartDTO);
    }

    /*
     * @author: Manh Tran
     * @since: 17/06/2022 1:29 CH
     * @description-VN: Thêm danh sách sản phẩm chờ thanh toán.
     * @description-EN: Add list product to cart.
     * @param:
     * @return:
     *
     * */
    @PostMapping("/list-product-to-checkout")
    public Object listProductToCheckout(@RequestBody List<AddToCartDTO> waitList) {
        CartDTO cartDTO = cartService.listProductToCheckout(waitList, waitList.get(0).getUserId());
        return ResponseDTO.show(cartDTO == null ? 400 : 200, "List product to checkout", cartDTO);
    }

    /*
     * @author: Manh Tran
     * @since: 17/06/2022 3:11 CH
     * @description-VN: Lấy danh sách sản phẩm chờ thanh toán.
     * @description-EN:
     * @param:
     * @return:
     *
     * */
    @GetMapping(value = "/list-product-to-checkout")
    public Object getListProductToCheckout(@RequestParam(value = "userId") Long userId) {
        CartDTO listProductToCheckout = this.cartService.getListProductToCheckout(userId);
        return ResponseDTO.show(listProductToCheckout == null ? 400 : 200, "Get list product to checkout",
                listProductToCheckout);
    }
}
