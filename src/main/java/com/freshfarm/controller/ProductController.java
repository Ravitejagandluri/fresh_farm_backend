package com.freshfarm.controller;

import com.freshfarm.entity.Product;
import com.freshfarm.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin("*")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Add Product (Farmer)
    @PostMapping
    public Product addProduct(@RequestBody Product product) {
        return productService.addProduct(product);
    }

    // Get All Products (Buyer)
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }
}
