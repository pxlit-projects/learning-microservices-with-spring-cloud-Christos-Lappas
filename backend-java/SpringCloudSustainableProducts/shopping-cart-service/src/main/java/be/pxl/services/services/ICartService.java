package be.pxl.services.services;

import be.pxl.services.domain.dto.CartRequest;
import be.pxl.services.domain.dto.CartResponse;

import java.math.BigDecimal;


public interface ICartService {

    CartResponse getById(Long id);
    CartResponse createCart();
    CartResponse addItemToCart(Long productId, CartRequest cartRequest);
    CartResponse removeItemFromCart(Long productId, CartRequest cartRequest);

    CartResponse order(Long id, BigDecimal total, Boolean ordered);
    void deleteCart(Long id);



}
