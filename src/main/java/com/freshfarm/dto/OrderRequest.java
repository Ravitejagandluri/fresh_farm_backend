package com.freshfarm.dto;

import java.util.List;

public class OrderRequest {
    private List<Long> cartItemIds;

    public OrderRequest() {}

    public List<Long> getCartItemIds() { return cartItemIds; }
    public void setCartItemIds(List<Long> cartItemIds) { this.cartItemIds = cartItemIds; }
}
