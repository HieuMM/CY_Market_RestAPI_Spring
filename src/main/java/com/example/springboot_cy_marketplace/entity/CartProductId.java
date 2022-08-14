package com.example.springboot_cy_marketplace.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/*
* @author: Manh Tran
* @since: 13/06/2022 3:38 CH
* @description-VN: Tạo khoá phức hợp cho bảng trung gian.
* @description-EN: Map the composite Primary Key which belongs to the intermediary join table.
* @param:
* @return:
*
* */
@Embeddable
public class CartProductId implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "cart_id")
    private Long cartId;

    @Column(name = "product_id")
    private Long productId;

    public CartProductId() {
    }

    public CartProductId(Long cartId, Long productId) {
        this.cartId = cartId;
        this.productId = productId;
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartProductId)) return false;
        CartProductId that = (CartProductId) o;
        return cartId.equals(that.cartId) && productId.equals(that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cartId, productId);
    }
}
