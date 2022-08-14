package com.example.springboot_cy_marketplace.services.impl;

import com.example.springboot_cy_marketplace.dto.AddToCartDTO;
import com.example.springboot_cy_marketplace.dto.CartDTO;
import com.example.springboot_cy_marketplace.dto.CartProductDTO;
import com.example.springboot_cy_marketplace.entity.CartEntity;
import com.example.springboot_cy_marketplace.entity.CartItemEntity;
import com.example.springboot_cy_marketplace.entity.ProductClassifiedEntity;
import com.example.springboot_cy_marketplace.entity.UserEntity;
import com.example.springboot_cy_marketplace.repository.ICartRepository;
import com.example.springboot_cy_marketplace.repository.IProductClassifiedRepository;
import com.example.springboot_cy_marketplace.repository.IUserRepository;
import com.example.springboot_cy_marketplace.services.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class CartServiceImpl implements ICartService {

    @Autowired
    private ICartRepository cartRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IProductClassifiedRepository productClassifiedRepository;


    /*
     * @author: Manh Tran
     * @since: 17/06/2022 6:13 SA
     * @description-VN: Thêm sản phẩm vào giỏ hàng.
     * @description-EN: Add product to cart.
     * @param: add - thông tin sản phẩm.
     * @return:
     *
     * */
    public boolean addProductToCart(AddToCartDTO add) {
        // Lấy phân loại mặt hàng có kèm giá - Get classified product with price.
        Optional<ProductClassifiedEntity> productClassified = productClassifiedRepository.findById(add.getProductClassifiedId());

        // Phân loại mặt hàng không hợp lệ - Classified product is invalid.
        if (productClassified.isEmpty()) {
            return false;
        }

        ProductClassifiedEntity product = productClassified.get();
        // Nếu số lượng sản phẩm thêm vào > tồn kho - If the quantity of product added to cart > inventory.
        if(product.getAmount() < add.getQuantity()) {
            return false;
        }
        Optional<CartEntity> checkCart = cartRepository.findByUserEntity_Id(add.getUserId());

        if (checkCart.isPresent()) { // Người dùng đã tạo giỏ hàng - User has created cart.
            CartEntity cartEntity = checkCart.get();
            List<CartItemEntity> cartItems = cartEntity.getItems();

            //Kiểm tra xem sản phẩm đã tồn tại trong giỏ hàng chưa - Check if product is exist in cart.
            boolean isExist = false;
            for (CartItemEntity cartItem : cartItems) {
                if (cartItem.getItem().equals(product) && cartItem.getQuantity() == 0) {
                    isExist = true;
                    cartItem.setQuantity(cartItem.getQuantity() + add.getQuantity());
                    cartEntity.setTotalQuantity(cartEntity.getTotalQuantity() + 1);
                }else if(cartItem.getItem().equals(product) && cartItem.getQuantity() > 0){
                    isExist = true;
                    cartItem.setQuantity(cartItem.getQuantity() + add.getQuantity());
                }
            }

            //Nếu sản phẩm chưa tồn tại thì tăng số lượng mặt hàng lên 1 - If product is not exist then increase quantity of product by 1.
            if (!isExist) {
                CartItemEntity cartItem = new CartItemEntity(product, add.getQuantity());
                cartItem.setCart(cartEntity);
                cartItems.add(cartItem);
                cartEntity.setTotalQuantity(cartEntity.getTotalQuantity() + 1);
            }
            //Lưu lại - Save.
            cartEntity.setItems(cartItems);

            // Tổng giá trị giỏ hàng hiện tại - Current cart total value.
            double totalPrice = cartEntity.getTotalPrice();

            // Cộng thêm giá trị sản phẩm vào giỏ hàng - Add product value to cart.
            cartEntity.setTotalPrice(totalPrice + add.getQuantity() * Double.parseDouble(product.getNewPrice()));
            cartRepository.save(cartEntity);
            return true;
        }

        // Người dùng chưa tạo giỏ hàng - User has not created cart.
        Optional<UserEntity> userEntity = userRepository.findById(add.getUserId());

        // Người dùng không tồn tại - User is not exist.
        if (userEntity.isEmpty()) {
            return false;
        }
        cartRepository.save(new CartEntity(userEntity.get(), new CartItemEntity(product, add.getQuantity())));
        return true;
    }
    //        Optional<UserEntity> userEntity = userRepository.findById(89L);
    //        ProductClassifiedEntity product = this.productClassifiedRepository.findById(8L).get();
    //        cartRepository.save(new CartEntity(userEntity.get(), new CartItemEntity(product, 5)));


    /*
    * @author: Manh Tran
    * @since: 24/06/2022 3:33 CH
    * @description-VN: Thêm nhiều sản phẩm vào giỏ hàng.
    * @description-EN: Add multiple product to cart.
    * @param: adds - Danh sách sản phẩm thêm vào giỏ hàng.
    * @return:
    *
    * */
    public boolean addMultipleProductsToCart(List<AddToCartDTO> adds) {
        for (AddToCartDTO add : adds) {
            boolean result = addProductToCart(add);
            if(!result){
                return false;
            }
        }
        return true;
    }

    /*
     * @author: Manh Tran
     * @since: 17/06/2022 6:13 SA
     * @description-VN: Cập nhật giỏ hàng.
     * @description-EN: Update cart.
     * @param: update - thông tin sản phẩm.
     * @return:
     *
     * */
    public boolean updateCart(AddToCartDTO update) {
        // Lấy phân loại mặt hàng có kèm giá - Get classified product with price.
        Optional<ProductClassifiedEntity> productClassified = productClassifiedRepository.findById(update.getProductClassifiedId());

        // Phân loại mặt hàng không hợp lệ - Classified product is invalid.
        if (productClassified.isEmpty()) {
            return false;
        }

        ProductClassifiedEntity product = productClassified.get();
        // Số lượng sản phẩm thêm vào không được lớn hơn số lượng tồn kho - Quantity of product added to cart is not greater than inventory.
        if (product.getAmount() < update.getQuantity()) {
            return false;
        }
        Optional<CartEntity> checkCart = cartRepository.findByUserEntity_Id(update.getUserId());
        // Người dùng không có giỏ hàng - User has not cart.
        if (checkCart.isEmpty()) {
            return false; // Chuyển qua API thêm sản phẩm vào giỏ hàng - Go to API to add product to cart.
        }

        boolean isExist = false;
        CartEntity cartUpdate = checkCart.get();
        List<CartItemEntity> cartItems = cartUpdate.getItems();
        for (CartItemEntity item : cartItems) {
            if (item.getItem().equals(product)) {
                isExist = true;
                if (update.getQuantity() == 0 && item.getQuantity() > 0) {
                    cartUpdate.setTotalQuantity(cartUpdate.getTotalQuantity() - 1);
                }else if(update.getQuantity() == 0 && item.getQuantity() == 0){
                    return false;
                }
                item.setQuantity(update.getQuantity());
                break;
            }
        }
        if(!isExist){
            return false;
        }
        cartUpdate.setTotalPrice(0D);
        double totalPrice = 0;
        for (CartItemEntity item : cartItems){
            totalPrice += item.getQuantity() * Double.parseDouble(item.getItem().getNewPrice());
        }
        cartUpdate.setTotalPrice(totalPrice);
        cartRepository.save(cartUpdate);
        return true;
    }

    /*
     * @author: Manh Tran
     * @since: 17/06/2022 6:21 SA
     * @description-VN: Lấy thông tin giỏ hàng theo mã người dùng.
     * @description-EN: Get cart information by user id.
     * @param:
     * @return:
     *
     * */
    public CartDTO findByUserId(Long userId) {
        if (this.userRepository.findById(userId).isEmpty()) {
            return null;
        }

        Optional<CartEntity> checkCart = this.cartRepository.findByUserEntity_Id(userId);
        if (checkCart.isEmpty()) {
            return new CartDTO();
        }

        CartEntity cartEntity = checkCart.get();
        return this.entityToDTO(cartEntity);
    }

    public CartDTO entityToDTO(CartEntity cartEntity) {
        CartDTO cartDTO = new CartDTO();
        cartDTO.setId(cartEntity.getId());
        List<CartProductDTO> products = new ArrayList<>();
        List<CartItemEntity> itemList = cartEntity.getItems();
        Collections.reverse(itemList);
        for (CartItemEntity item : itemList) {
            if (item.getQuantity() > 0) {
                CartProductDTO product = CartProductDTO.entityToDTO(item.getItem());
                product.setQuantity(item.getQuantity());
                products.add(product);
            }
        }
        cartDTO.setProductList(products);
        cartDTO.setTotalPrice(cartEntity.getTotalPrice());
        cartDTO.setTotalQuantity(cartEntity.getTotalQuantity());
        return cartDTO;
    }

    /*
     * @author: Manh Tran
     * @since: 17/06/2022 1:32 CH
     * @description-VN: Danh sách sản phẩm chờ thanh toán.
     * @description-EN: List product waiting to pay.
     * @param:
     * @return:
     *
     * */
    @CachePut(value = "wait-list-product", key = "#userId")
    public CartDTO listProductToCheckout(List<AddToCartDTO> waitList, Long userId) {
        CartDTO cartDTO = new CartDTO();
        cartDTO.setTotalPrice(0D);
        cartDTO.setId(ThreadLocalRandom.current().nextLong(1, 10000));
        List<CartProductDTO> products = new ArrayList<>();
        for (AddToCartDTO add : waitList) {
            this.productClassifiedRepository.findById(add.getProductClassifiedId()).ifPresent(productClassified -> {
                CartProductDTO product = CartProductDTO.entityToDTO(productClassified);
                product.setQuantity(add.getQuantity());
                product.setProductClassifiedBy01(productClassified.getProductEntity().getClassifiedBy01());
                product.setProductClassifiedBy02(productClassified.getProductEntity().getClassifiedBy02());
                cartDTO.setTotalPrice(cartDTO.getTotalPrice() + add.getQuantity() * Double.parseDouble(productClassified.getNewPrice()));
                products.add(product);
            });
        }
        cartDTO.setProductList(products);
        cartDTO.setTotalQuantity(waitList.size());
        return cartDTO;
    }

    @Cacheable(value = "wait-list-product", key = "#userId")
    public CartDTO getListProductToCheckout(Long userId){
        return null;
    }

    @Override
    public List<CartDTO> findAll() {
        return null;
    }

    @Override
    public Page<CartDTO> findAll(Pageable page) {
        return null;
    }

    @Override
    public CartDTO findById(Long id) {
        return null;
    }

    @Override
    public CartDTO add(CartEntity dto) {
        return null;
    }

    @Override
    public List<CartDTO> add(List<CartEntity> dto) {
        return null;
    }

    @Override
    public CartDTO update(CartEntity dto) {
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
}
