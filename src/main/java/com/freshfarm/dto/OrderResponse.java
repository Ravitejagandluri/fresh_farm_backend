package com.freshfarm.dto;

import com.freshfarm.entity.Order;
import com.freshfarm.entity.OrderStatus;

import java.time.Instant;

public class OrderResponse {
    private Long id;
    private Long productId;
    private String productName;
    private Integer quantity;
    private Double totalPrice;
    private String status;
    private Instant createdAt;
    private String buyerName;

    public static OrderResponse fromEntity(Order o) {
        OrderResponse r = new OrderResponse();
        r.setId(o.getId());
        r.setQuantity(o.getQuantity());
        r.setTotalPrice(o.getTotalPrice());
        r.setStatus(o.getStatus() != null ? o.getStatus().name() : null);
        r.setCreatedAt(o.getCreatedAt());
        if (o.getBuyer() != null) {
            r.setBuyerName(o.getBuyer().getName());
        }
        if (o.getProduct() != null) {
            r.setProductId(o.getProduct().getId());
            r.setProductName(o.getProduct().getName());
        }
        return r;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public String getBuyerName() { return buyerName; }
    public void setBuyerName(String buyerName) { this.buyerName = buyerName; }
}
