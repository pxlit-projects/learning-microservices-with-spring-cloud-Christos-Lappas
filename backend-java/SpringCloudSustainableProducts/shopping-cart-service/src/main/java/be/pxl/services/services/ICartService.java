package be.pxl.services.services;

import be.pxl.services.domain.dto.CartResponse;


public interface ICartService {

    CartResponse getById(Long id);
    CartResponse createCart();
    CartResponse addItemToCart(Long customerId, Long productId);
    CartResponse removeItemFromCart(Long customerId, Long productId);

    CartResponse order(Long id, Boolean ordered);
    void deleteCart(Long id);



}
