package com.freshfarm.service;

import com.freshfarm.dto.ProductRequest;
import com.freshfarm.dto.ProductResponse;
import com.freshfarm.entity.Product;
import com.freshfarm.entity.User;
import com.freshfarm.repository.ProductRepository;
import com.freshfarm.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ProductService(ProductRepository productRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ProductResponse addProduct(ProductRequest request, Long farmerId) {
        User farmer = userRepository.findById(farmerId).orElseThrow(() -> new RuntimeException("Farmer not found"));
        Product product = new Product();
        product.setName(request.getName());
        product.setCategory(request.getCategory());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity() != null ? request.getQuantity() : 0);
        product.setUnit(request.getUnit());
        product.setImageUrl(request.getImageUrl());
        product.setFarmer(farmer);
        product.setFarmerName(farmer.getName());

        product.setDiscount(null);
        product.setDiscountedPrice(request.getPrice());
        product = productRepository.save(product);
        return ProductResponse.fromEntity(product);
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(ProductResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> getByFarmerId(Long farmerId) {
        return productRepository.findByFarmerId(farmerId).stream()
                .map(ProductResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteProduct(Long id, Long farmerId) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        if (!product.getFarmer().getId().equals(farmerId)) {
            throw new RuntimeException("Not authorized to delete this product");
        }
        productRepository.delete(product);
    }

    public Product getEntityById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Transactional
    public ProductResponse updateProduct(Long productId, ProductRequest request, Long farmerId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (!product.getFarmer().getId().equals(farmerId)) {
            throw new RuntimeException("Not authorized to edit this product");
        }

        // Update fields
        product.setName(request.getName());
        product.setCategory(request.getCategory());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setImageUrl(request.getImageUrl());
        product.setUnit(request.getUnit()); // if unit exists
        product.setDiscount(request.getDiscount());

        // ✅ BUSINESS LOGIC HERE
        Double discount = request.getDiscount();
        Double price = request.getPrice();

        if (discount != null && discount > 0 && price != null) {

            if (discount > 90) {
                throw new RuntimeException("Discount cannot exceed 90%");
            }

            double discountedPrice = price - (price * discount / 100);

            product.setDiscount(discount);
            product.setDiscountedPrice(Math.round(discountedPrice * 100.0) / 100.0);

        } else {
            product.setDiscount(null);
            product.setDiscountedPrice(price);
        }

        product = productRepository.save(product);

        return ProductResponse.fromEntity(product);
    }

}
