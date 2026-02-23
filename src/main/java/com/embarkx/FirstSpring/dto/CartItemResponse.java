package com.embarkx.FirstSpring.dto;

import lombok.Data;
import com.embarkx.FirstSpring.model.Product;

import java.math.BigDecimal;

@Data
public class CartItemResponse {
    private Long id;
//    private UserResponse userResponse;
//    private ProductResponse productResponse;
//    private String productName;
    private Product product;
    private Integer quantity;
    private BigDecimal price;
}
