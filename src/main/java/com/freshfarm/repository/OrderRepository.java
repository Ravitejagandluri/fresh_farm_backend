package com.freshfarm.repository;

import com.freshfarm.entity.Order;
import com.freshfarm.entity.OrderStatus;
import com.freshfarm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByBuyer(User buyer);
    List<Order> findByBuyerId(Long buyerId);
    List<Order> findByProductFarmerId(Long farmerId);
    List<Order> findByProductFarmerIdAndStatus(Long farmerId, OrderStatus status);
}
