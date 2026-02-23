package com.embarkx.FirstSpring.Service;

import com.embarkx.FirstSpring.dto.CartItemResponse;
import com.embarkx.FirstSpring.dto.OrderItemDTO;
import com.embarkx.FirstSpring.dto.OrderResponse;
import lombok.RequiredArgsConstructor;
import com.embarkx.FirstSpring.model.CartItem;
import com.embarkx.FirstSpring.model.Order;
import com.embarkx.FirstSpring.model.OrderItem;
import com.embarkx.FirstSpring.model.OrderStatus;
import com.embarkx.FirstSpring.model.User;
import org.springframework.stereotype.Service;
import com.embarkx.FirstSpring.repository.OrderRepository;
import com.embarkx.FirstSpring.repository.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;
    private final CartService cartService;
    private final OrderRepository orderRepository;

    public Optional<OrderResponse> createOrder(String userId) {
        // validete for the user
        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
        if(userOpt.isEmpty()) {
            return Optional.empty();
        }
        User user = userOpt.get();

        // validate for the cart item
        List<CartItemResponse> cartItems = cartService.getCart(userId);

        if(cartItems.isEmpty()) {
            return  Optional.empty();
        }

        // calculate the totalPrice
        BigDecimal totalPrice = cartItems.stream()
                .map(CartItemResponse::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // create order
        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(totalPrice);
        List<OrderItem> orderItems = cartItems.stream()
                .map(item -> new OrderItem(
                        null,
                        item.getProduct(),
                        item.getQuantity(),
                        item.getPrice(),
                        order
                ))
                .toList();

        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);

        // clear cart
        cartService.clearCart(userId);

        return  Optional.of(mapToOrderReponse(order));
    }

    private OrderResponse mapToOrderReponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getItems().stream()
                        .map(orderItem -> new OrderItemDTO(
                                orderItem.getId(),
                                orderItem.getProduct().getId(),
                                orderItem.getQuantity(),
                                orderItem.getPrice(),
                                orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity()))
                        )).toList(),
                order.getCreatedAt()
        );
    }
}
