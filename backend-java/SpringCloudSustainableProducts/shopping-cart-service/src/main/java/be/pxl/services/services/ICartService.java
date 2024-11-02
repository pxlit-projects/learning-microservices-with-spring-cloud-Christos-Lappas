package be.pxl.services.services;

import be.pxl.services.domain.dto.CartResponse;


public interface ICartService {

    CartResponse getByCustomerId(Long customerId);
    CartResponse createCart(Long customerId);
    CartResponse addItemToCart(Long customerId, Long productId);
    CartResponse removeItemFromCart(Long customerId, Long productId);



}
