package com.freshfarm.controller;

import com.freshfarm.dto.OrderRequest;
import com.freshfarm.dto.OrderResponse;
import com.freshfarm.entity.User;
import com.freshfarm.entity.Role;
import com.freshfarm.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/place")
    public ResponseEntity<List<OrderResponse>> placeOrder(@RequestBody OrderRequest request,
                                                          @AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        List<OrderResponse> orders = orderService.placeOrder(request, user.getId());
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/my")
    public ResponseEntity<List<OrderResponse>> getMyOrders(@AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        if (user.getRole() == Role.FARMER) {
            return ResponseEntity.ok(orderService.getFarmerOrders(user.getId()));
        }
        return ResponseEntity.ok(orderService.getMyOrders(user.getId()));
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<OrderResponse> completeOrder(@PathVariable Long id,
                                                      @AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        OrderResponse response = orderService.completeOrder(id, user.getId());
        return ResponseEntity.ok(response);
    }
}
