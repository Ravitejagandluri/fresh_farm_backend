package com.freshfarm.dto;

import com.freshfarm.entity.Cart;

public class CartResponse {
    private Long id;
    private Long productId;
    private String productName;
    private Double productPrice;
    private String productImageUrl;
    private Integer quantity;
    private Double subtotal;

    public static CartResponse fromEntity(Cart c) {
        CartResponse r = new CartResponse();
        r.setId(c.getId());
        r.setQuantity(c.getQuantity());
        if (c.getProduct() != null) {
            r.setProductId(c.getProduct().getId());
            r.setProductName(c.getProduct().getName());
            r.setProductPrice(c.getProduct().getPrice());
            r.setProductImageUrl(c.getProduct().getImageUrl());
            r.setSubtotal(c.getProduct().getPrice() != null && c.getQuantity() != null
                    ? c.getProduct().getPrice() * c.getQuantity() : 0.0);
        }
        return r;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public Double getProductPrice() { return productPrice; }
    public void setProductPrice(Double productPrice) { this.productPrice = productPrice; }

    public String getProductImageUrl() { return productImageUrl; }
    public void setProductImageUrl(String productImageUrl) { this.productImageUrl = productImageUrl; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Double getSubtotal() { return subtotal; }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }
}
