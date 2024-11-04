package be.pxl.services.services;

import be.pxl.services.client.ProductClient;
import be.pxl.services.domain.Cart;
import be.pxl.services.domain.Product;
import be.pxl.services.domain.dto.CartResponse;
import be.pxl.services.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Arrays.stream;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {

    private final CartRepository shoppingCartRepository;
    private final ProductClient productClient;


    @Override
    public CartResponse getByCustomerId(Long customerId) {
        return mapToCartResponse(shoppingCartRepository.findByCustomerId(customerId).get());
    }

    private CartResponse mapToCartResponse(Cart cart) {
        return CartResponse.builder()
                .customerId(cart.getCustomerId())
                .items(cart.getItems())
                .build();
    }

    private Product mapToProduct(Product product) {
        return Product.builder()
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .category(product.getCategory())
                .score(product.getScore())
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
        Product product = mapToProduct(productClient.getProduct(productId));
        cart.addItem(product);
        shoppingCartRepository.save(cart);
        return mapToCartResponse(cart);
    }

    @Override
    public CartResponse removeItemFromCart(Long customerId, Long productId) {
        Cart cart = shoppingCartRepository.findByCustomerId(customerId).get();
        Product product = mapToProduct(productClient.getProduct(productId));
        cart.removeItem(product);
        shoppingCartRepository.save(cart);
        return mapToCartResponse(cart);
    }
}
