package com.embarkx.FirstSpring.controller;

import com.embarkx.FirstSpring.Service.CartService;
import com.embarkx.FirstSpring.dto.CartItemRequest;
import com.embarkx.FirstSpring.dto.CartItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<String> addToCart(
            @RequestHeader("X-User-ID") String userId,
            @RequestBody CartItemRequest request) {
        boolean added = cartService.addToCart(userId, request);
        if (added) {
            return ResponseEntity.ok("Item added to cart successfully");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to add item to cart");
    }

    @GetMapping
    public ResponseEntity<List<CartItemResponse>> getCart(@RequestHeader("X-User-ID") String userId) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<String> deleteItemFromCart(
            @RequestHeader("X-User-ID") String userId,
            @PathVariable Long productId) {
        boolean deleted = cartService.deleteItemFromCart(userId, productId);
        if (deleted) {
            return ResponseEntity.ok("Item removed from cart successfully");
        }
        return ResponseEntity.notFound().build();
    }
}
