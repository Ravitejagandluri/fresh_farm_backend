package com.freshfarm.repository;

import com.freshfarm.entity.Cart;
import com.freshfarm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByBuyer(User buyer);
    List<Cart> findByBuyerId(Long buyerId);
    Optional<Cart> findByBuyerIdAndProductId(Long buyerId, Long productId);
}
