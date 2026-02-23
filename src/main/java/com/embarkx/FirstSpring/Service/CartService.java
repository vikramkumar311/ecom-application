package com.embarkx.FirstSpring.Service;

import com.embarkx.FirstSpring.dto.CartItemRequest;
import com.embarkx.FirstSpring.dto.CartItemResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import com.embarkx.FirstSpring.model.CartItem;
import com.embarkx.FirstSpring.model.Product;
import com.embarkx.FirstSpring.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.embarkx.FirstSpring.repository.CartItemRepository;
import com.embarkx.FirstSpring.repository.ProductRepository;
import com.embarkx.FirstSpring.repository.UserRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final CartItemRepository cartItemRepository;
    private  final ProductRepository productRepository;
    private final UserRepository userRepository;

    public boolean addToCart(String userId, CartItemRequest request) {
        Optional<Product> productOpt = productRepository.findById(request.getProductId());
        if(productOpt.isEmpty()) {
            return  false;
        }

        Product product = productOpt.get();

        if(product.getStockQuantity() < request.getQuantity()) {
            return  false;
        }

        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
        if(userOpt.isEmpty()) {
            return  false;
        }

        User user = userOpt.get();

        CartItem existingCartItem = cartItemRepository.findByUserAndProduct(user, product);
        if(existingCartItem != null) {
            // update the quantity
            existingCartItem.setQuantity(existingCartItem.getQuantity() + request.getQuantity());
            existingCartItem.setPrice((product.getPrice().multiply(BigDecimal.valueOf(existingCartItem.getQuantity()))));
            cartItemRepository.save(existingCartItem);
        } else {
            // create new cart item
            CartItem cartItem = new CartItem();
            cartItem.setUser(user);
            cartItem.setProduct(product);
            cartItem.setQuantity(request.getQuantity());
            cartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
            cartItemRepository.save(cartItem);
        }
        return  true;
    }

    public boolean deleteItemFromCart(String userId, Long productId) {
        // check user is present or not
        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
        if(userOpt.isEmpty()) {
            return false;
        }

        // check product is present or not
        Optional<Product> productOpt = productRepository.findById(productId);
        if(productOpt.isEmpty()) {
            return  false;
        }

        cartItemRepository.deleteByUserAndProduct(userOpt.get(), productOpt.get());

        return  true;

    }

    public List<CartItemResponse> getCart(String userId) {
        return  userRepository.findById(Long.valueOf(userId))
                .map(user -> cartItemRepository.findByUser(user)
                        .stream()
                        .map(this::mapToResponse)
                        .toList())
                .orElseGet(List::of);
    }


    private CartItemResponse mapToResponse(CartItem cartItem) {
        CartItemResponse cartItemResponse = new CartItemResponse();
        cartItemResponse.setId(cartItem.getId());
//      cartItemResponse.setUserResponse();

//        cartItemResponse.setProductName(cartItem.getProduct().getName());
        cartItemResponse.setProduct(cartItem.getProduct());
        cartItemResponse.setQuantity(cartItem.getQuantity());
        cartItemResponse.setPrice(cartItem.getPrice());

        return cartItemResponse;
    }

    public void clearCart(String userId) {
        userRepository.findById(Long.valueOf(userId)).ifPresent(cartItemRepository::deleteByUser);
    }
}
