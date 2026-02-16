package com.freshfarm.controller;

import com.freshfarm.dto.ProductRequest;
import com.freshfarm.dto.ProductResponse;
import com.freshfarm.entity.User;
import com.freshfarm.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@RequestBody ProductRequest request,
                                        @AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(401).body(java.util.Map.of("error", "Not authenticated. Please log in as a farmer."));
        }
        ProductResponse response = productService.addProduct(request, user.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/farmer/{farmerId}")
    public ResponseEntity<List<ProductResponse>> getByFarmer(@PathVariable Long farmerId) {
        return ResponseEntity.ok(productService.getByFarmerId(farmerId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id,
                                             @AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        productService.deleteProduct(id, user.getId());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductRequest request,
            @AuthenticationPrincipal User user) {

        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        ProductResponse response =
                productService.updateProduct(id, request, user.getId());

        return ResponseEntity.ok(response);
    }

}
