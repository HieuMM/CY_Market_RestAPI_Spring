package com.example.springboot_cy_marketplace.dto;

import com.example.springboot_cy_marketplace.model.ProductModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProductStatisticDTO {
    // Tổng số lượng sản phẩm (cả đang bán và bị khoá)
    private long totalProducts;
    // Tổng số lượng sản phẩm đã bán
    private long totalProductsSold;
    // Tổng số lượng sản phẩm còn hàng và không bị khoá.
    private long totalProductsInStock;
    // Tổng số lượng sản phẩm bị khoá
    private long totalProductsLocked;
    // Tổng số lượng sản phẩm hết hàng
    private long totalProductsOutOfStock;
    // Tổng số lượng sản phẩm sắp hết hàng
    private long totalProductsWillBeOutOfStock;
    // Sản phẩm có lượt xem nhiều nhất mọi thời đại
    private ProductModel productWithMostViews;
    // Sản phẩm bán chạy nhất mọi thời đại
    private ProductModel productWithMostSales;
}
