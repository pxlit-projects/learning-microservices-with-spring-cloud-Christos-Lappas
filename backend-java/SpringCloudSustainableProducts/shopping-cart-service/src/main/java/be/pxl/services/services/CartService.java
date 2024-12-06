package be.pxl.services.services;

import be.pxl.services.client.ProductClient;
import be.pxl.services.domain.Cart;
import be.pxl.services.domain.Product;
import be.pxl.services.domain.dto.CartRequest;
import be.pxl.services.domain.dto.CartResponse;
import be.pxl.services.repository.CartRepository;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {
    private final CartRepository shoppingCartRepository;
    private final ProductClient productClient;
    private static final Logger logger = LoggerFactory.getLogger(CartService.class);

    @Override
    public CartResponse getById(Long id) {
        logger.info("Fetching shopping cart with ID: {}", id);

        CartResponse cartResponse;
        try {
            cartResponse = mapToCartResponse(
                    shoppingCartRepository.findById(id)
                            .orElseThrow(() -> new NotFoundException("Cart with ID " + id + " not found"))
            );
            logger.info("Successfully retrieved shopping cart with ID: {}", id);
        } catch (NotFoundException e) {
            logger.warn("Shopping cart with ID '{}' not found: {}", id, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error occurred while fetching shopping cart with ID '{}': {}", id, e.getMessage(), e);
            throw e;
        }

        return cartResponse;
    }

    private CartResponse mapToCartResponse(Cart cart) {
        return CartResponse.builder()
                .id(cart.getId())
                .customer(cart.getCustomer())
                .items(cart.getItems())
                .total(cart.getTotal())
                .ordered(cart.isOrdered())
                .build();
    }

    private CartResponse mapToCartResponse(CartRequest cart) {
        return CartResponse.builder()
                .id(cart.getId())
                .customer(cart.getCustomer())
                .items(cart.getItems())
                .total(cart.getTotal())
                .ordered(cart.isOrdered())
                .build();
    }

    private Product mapToProduct(Product product) {
        return Product.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .category(product.getCategory())
                .score(product.getScore())
                .quantity(1)
                .build();
    }

    @Override
    public CartResponse createCart() {
        logger.info("Starting the creation of a new shopping cart");

        CartResponse cartResponse;
        try {
            Cart cart = new Cart();
            shoppingCartRepository.save(cart);
            logger.info("Successfully created a new shopping cart with ID: {}", cart.getId());

            cartResponse = mapToCartResponse(cart);
        } catch (Exception e) {
            logger.error("Error occurred while creating a new shopping cart: {}", e.getMessage(), e);
            throw e;
        }

        return cartResponse;
    }

    @Override
    @Transactional
    public CartResponse addItemToCart(Long productId, CartRequest cartRequest) {
        logger.info("Adding product ID {} to cart with ID {}", productId, cartRequest.getId());

        Cart cart;
        Product product;

        try {
            cart = shoppingCartRepository.findById(cartRequest.getId())
                    .orElseThrow(() -> new NotFoundException("Cart with ID " + cartRequest.getId() + " not found"));
            logger.debug("Successfully fetched cart with ID {}", cartRequest.getId());

            product = mapToProduct(productClient.getProduct(productId));
            logger.debug("Successfully fetched product details for product ID {}", productId);

            Optional<Product> productInCart = cartRequest.getItems().stream()
                    .filter(p -> p.getId().equals(productId))
                    .findFirst();

            if (productInCart.isPresent()) {
                productInCart.get().setQuantity(productInCart.get().getQuantity() + 1);
            } else {
                cartRequest.addItem(product);
            }
            logger.info("Added product ID {} to cart ID {}", productId, cartRequest.getId());

        } catch (NotFoundException e) {
            logger.warn("Entity not found: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error occurred while adding product ID {} to cart ID {}: {}",
                    productId, cartRequest.getId(), e.getMessage(), e);
            throw e;
        }

        return mapToCartResponse(cartRequest);
    }

    @Override
    public CartResponse removeItemFromCart(Long productId, CartRequest cartRequest) {
        logger.info("Removing product ID {} from cart ID {}", productId, cartRequest.getId());

        Cart cart;
        Product product;

        try {
            cart = shoppingCartRepository.findById(cartRequest.getId())
                    .orElseThrow(() -> new NotFoundException("Cart with ID " + cartRequest.getId() + " not found"));
            logger.debug("Successfully fetched cart with ID {}", cartRequest.getId());

            product = cartRequest.getItems().stream()
                    .filter(p -> p.getId().equals(productId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            logger.debug("Successfully fetched product details for product ID {}", productId);

            if (!cartRequest.getItems().contains(product)) {
                logger.warn("Product ID {} not found in cart ID {}, cannot remove", productId, cartRequest.getId());
                throw new IllegalArgumentException("Product not found in cart");
            }

            if (product.getQuantity() > 1) {
                product.setQuantity(product.getQuantity() - 1);
            } else {
                cartRequest.removeItem(product);
            }

            logger.info("Removed product ID {} from cart ID {}", productId, cartRequest.getId());

        } catch (NotFoundException e) {
            logger.warn("Entity not found: {}", e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid operation: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error occurred while removing product ID {} from cart ID {}: {}",
                    productId, cartRequest.getId(), e.getMessage(), e);
            throw e;
        }

        return mapToCartResponse(cartRequest);
    }

    @Override
    public CartResponse order(Long id, BigDecimal total, Boolean ordered) {
        logger.info("Updating order status for cart ID {}: total = {}, ordered = {}", id, total, ordered);

        Cart cart;

        try {
            cart = shoppingCartRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Cart with ID " + id + " not found"));
            logger.debug("Successfully fetched cart with ID {}", id);

            boolean previousOrderStatus = cart.isOrdered();
            cart.setOrdered(ordered);
            logger.info("Cart ID {}: order status updated from {} to {}", id, previousOrderStatus, ordered);

            cart.setTotal(total);
            logger.info("Cart ID {}: total price updated to {}", id, total);

            shoppingCartRepository.save(cart);
            logger.info("Cart ID {} saved successfully with new order status: {}", id, ordered);
        } catch (NotFoundException e) {
            logger.warn("Entity not found: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error occurred while updating order status for cart ID {}: {}", id, e.getMessage(), e);
            throw e;
        }

        return mapToCartResponse(cart);
    }

    @Override
    public void deleteCart(Long id) {
        logger.info("Attempting to delete cart with ID {}", id);

        Cart cart;

        try {
            cart = shoppingCartRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Cart with ID " + id + " not found"));
            logger.debug("Successfully fetched cart with ID {}", id);

            shoppingCartRepository.delete(cart);
            logger.info("Successfully deleted cart with ID {}", id);
        } catch (NotFoundException e) {
            logger.warn("Entity not found: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error occurred while deleting cart with ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }
}
