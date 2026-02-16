package com.freshfarm.repository;

import com.freshfarm.entity.Product;
import com.freshfarm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByFarmer(User farmer);
    List<Product> findByFarmerId(Long farmerId);
}
