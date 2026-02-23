package com.embarkx.FirstSpring.Service;

import com.embarkx.FirstSpring.dto.ProductRequest;
import com.embarkx.FirstSpring.dto.ProductResponse;
import lombok.RequiredArgsConstructor;
import com.embarkx.FirstSpring.model.Product;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import com.embarkx.FirstSpring.repository.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;


    public String createProduct(ProductRequest productRequest) {
        Product product = new Product();
        updateProductFromRequest(product, productRequest);
        productRepository.save(product);
        return "Product added successfully";
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findByActiveTrue().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public  boolean updateProduct(Long productId, ProductRequest productRequest) {
        return productRepository.findById(productId)
                .map(existingProduct -> {
                    updateProductFromRequest(existingProduct, productRequest);
                    productRepository.save(existingProduct);
                    return true;
                }).orElse(false);
    }

    public String deleteProduct(Long productId) {
        return  productRepository.findById(productId)
                .map(existingProduct -> {
                    existingProduct.setActive(false);
                    productRepository.save(existingProduct);
                    return "Product deleted successfully";
                }).orElse("Product not exist");
    }

    public ProductResponse getProductById(Long productId) {
        return productRepository.findById(productId)
                .map(this::mapToResponse)
                .orElse(null);
    }

    private ProductResponse mapToResponse(Product product) {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(product.getId());
        productResponse.setName(product.getName());
        productResponse.setDescription(product.getDescription());
        productResponse.setActive(product.isActive());
        productResponse.setPrice(product.getPrice());
        productResponse.setStockQuantity(product.getStockQuantity());
        productResponse.setImageUrl(product.getImageUrl());
        productResponse.setCategory(product.getCategory());

        return  productResponse;
    }

    private void updateProductFromRequest(Product product, ProductRequest productRequest) {
        if(productRequest.getId() != null) {
            product.setId(productRequest.getId());
        }
         if(productRequest.getName() != null) {
             product.setName(productRequest.getName());
        }
         if(productRequest.getDescription() != null) {
             product.setDescription(productRequest.getDescription());
        }
         if(productRequest.getActive() != null) {
             product.setActive(productRequest.getActive());
        }
         if(productRequest.getStockQuantity() != null) {
             product.setStockQuantity(productRequest.getStockQuantity());
        }
         if(productRequest.getImageUrl() != null) {
             product.setImageUrl(productRequest.getImageUrl());
        }
        if(productRequest.getCategory() != null) {
             product.setCategory(productRequest.getCategory());
        }
        if(productRequest.getPrice() != null) {
             product.setPrice(productRequest.getPrice());
        }

    }


    public List<ProductResponse> searchProducts(String keyword) {
        return productRepository.searchProduct(keyword).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
}
