package com.freshfarm.service;

import com.freshfarm.dto.CartRequest;
import com.freshfarm.dto.CartResponse;
import com.freshfarm.entity.Cart;
import com.freshfarm.entity.Product;
import com.freshfarm.entity.User;
import com.freshfarm.repository.CartRepository;
import com.freshfarm.repository.ProductRepository;
import com.freshfarm.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository, UserRepository userRepository,
                       ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public CartResponse addToCart(CartRequest request, Long buyerId) {
        User buyer = userRepository.findById(buyerId).orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        int qty = request.getQuantity() != null && request.getQuantity() > 0 ? request.getQuantity() : 1;

        Cart cart = cartRepository.findByBuyerIdAndProductId(buyerId, product.getId()).orElse(null);
        if (cart != null) {
            cart.setQuantity(cart.getQuantity() + qty);
            cart = cartRepository.save(cart);
        } else {
            cart = new Cart();
            cart.setBuyer(buyer);
            cart.setProduct(product);
            cart.setQuantity(qty);
            cart = cartRepository.save(cart);
        }
        return CartResponse.fromEntity(cart);
    }

    public List<CartResponse> getMyCart(Long buyerId) {
        return cartRepository.findByBuyerId(buyerId).stream()
                .map(CartResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public void removeFromCart(Long cartId, Long buyerId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        if (!cart.getBuyer().getId().equals(buyerId)) {
            throw new RuntimeException("Not authorized to remove this cart item");
        }
        cartRepository.delete(cart);
    }

    public Cart getCartEntity(Long cartId, Long buyerId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        if (!cart.getBuyer().getId().equals(buyerId)) {
            throw new RuntimeException("Not authorized");
        }
        return cart;
    }
}
