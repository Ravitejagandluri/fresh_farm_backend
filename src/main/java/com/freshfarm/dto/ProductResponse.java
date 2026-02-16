package com.freshfarm.dto;

import com.freshfarm.entity.Product;
import java.time.Instant;

public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer quantity;
    private String unit;
    private Double discount;
    private Double discountedPrice;
    private String imageUrl;
    private Long farmerId;
    private String farmerName;
    private Instant createdAt;
    private String category;




    public static ProductResponse fromEntity(Product p) {
        ProductResponse r = new ProductResponse();
        r.setId(p.getId());
        r.setName(p.getName());
        r.setCategory(p.getCategory());
        r.setDescription(p.getDescription());
        r.setPrice(p.getPrice());
        r.setQuantity(p.getQuantity());
        r.setDiscount(p.getDiscount());
        r.setDiscountedPrice(p.getDiscountedPrice());
        r.setUnit(p.getUnit());
        r.setImageUrl(p.getImageUrl());
        if (p.getFarmer() != null) {
            r.setFarmerId(p.getFarmer().getId());
            r.setFarmerName(p.getFarmer().getName());
        }
        r.setCreatedAt(p.getCreatedAt());
        return r;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(Double discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Long getFarmerId() { return farmerId; }
    public void setFarmerId(Long farmerId) { this.farmerId = farmerId; }

    public String getFarmerName() { return farmerName; }
    public void setFarmerName(String farmerName) { this.farmerName = farmerName; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
