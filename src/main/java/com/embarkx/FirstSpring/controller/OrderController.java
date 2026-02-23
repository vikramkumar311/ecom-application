package com.embarkx.FirstSpring.controller;

import com.embarkx.FirstSpring.Service.OrderService;
import com.embarkx.FirstSpring.dto.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestHeader("X-User-ID") String userId) {
        Optional<OrderResponse> orderResponse = orderService.createOrder(userId);
        return orderResponse
                .map(response -> new ResponseEntity<>(response, HttpStatus.CREATED))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }
}
