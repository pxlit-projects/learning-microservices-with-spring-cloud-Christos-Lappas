package be.pxl.services.services;

import be.pxl.services.domain.Cart;
import be.pxl.services.domain.dto.CartResponse;
import be.pxl.services.repository.ICartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {

    private final ICartRepository shoppingCartRepository;


    @Override
    public CartResponse getByCustomerId(Long customerId) {
        // TODO: Haal producten op uit product catalog
        return mapToCartResponse(shoppingCartRepository.findByCustomerId(customerId).get());
    }

    private CartResponse mapToCartResponse(Cart cart) {
        return CartResponse.builder()
                .customerId(cart.getCustomerId())
                .items(cart.getItems())
                .build();
    }

    @Override
    public CartResponse createCart(Long customerId) {
        Cart cart = new Cart();
        cart.setCustomerId(customerId);
        shoppingCartRepository.save(cart);
        return mapToCartResponse(cart);
    }

    @Override
    public CartResponse addItemToCart(Long customerId, Long productId) {
        Cart cart = shoppingCartRepository.findByCustomerId(customerId).get();
        // TODO: Haal producten op uit product catalog
        return null;
    }

    @Override
    public CartResponse removeItemFromCart(Long customerId, Long productId) {
        Cart cart = shoppingCartRepository.findByCustomerId(customerId).get();
        // TODO: Haal producten op uit product catalog
        return null;
    }
}
