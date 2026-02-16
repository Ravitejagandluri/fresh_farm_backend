package com.freshfarm.service;

import com.freshfarm.dto.OrderRequest;
import com.freshfarm.dto.OrderResponse;
import com.freshfarm.entity.*;
import com.freshfarm.repository.CartRepository;
import com.freshfarm.repository.OrderRepository;
import com.freshfarm.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, CartRepository cartRepository,
                        UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public List<OrderResponse> placeOrder(OrderRequest request, Long buyerId) {
        User buyer = userRepository.findById(buyerId).orElseThrow(() -> new RuntimeException("User not found"));
        List<OrderResponse> result = new ArrayList<>();
        if (request.getCartItemIds() == null || request.getCartItemIds().isEmpty()) {
            return result;
        }
        for (Long cartId : request.getCartItemIds()) {
            Cart cart = cartRepository.findById(cartId)
                    .orElseThrow(() -> new RuntimeException("Cart item not found: " + cartId));
            if (!cart.getBuyer().getId().equals(buyerId)) {
                throw new RuntimeException("Not authorized for cart item: " + cartId);
            }
            Product product = cart.getProduct();
            int qty = cart.getQuantity();
            double total = product.getPrice() != null ? product.getPrice() * qty : 0;

            Order order = new Order();
            order.setBuyer(buyer);
            order.setProduct(product);
            order.setQuantity(qty);
            order.setTotalPrice(total);
            order.setStatus(OrderStatus.IN_PROGRESS);
            order = orderRepository.save(order);
            result.add(OrderResponse.fromEntity(order));
            cartRepository.delete(cart);
        }
        return result;
    }

    public List<OrderResponse> getMyOrders(Long buyerId) {
        return orderRepository.findByBuyerId(buyerId).stream()
                .map(OrderResponse::fromEntity)
                .toList();
    }

    public List<OrderResponse> getFarmerOrders(Long farmerId) {
        return orderRepository.findByProductFarmerId(farmerId).stream()
                .map(OrderResponse::fromEntity)
                .toList();
    }

    @Transactional
    public OrderResponse completeOrder(Long orderId, Long farmerId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        if (order.getProduct().getFarmer() == null || !order.getProduct().getFarmer().getId().equals(farmerId)) {
            throw new RuntimeException("Not authorized to complete this order");
        }
        order.setStatus(OrderStatus.COMPLETED);
        order = orderRepository.save(order);
        return OrderResponse.fromEntity(order);
    }
}
