package be.pxl.services.services;

import be.pxl.services.client.ProductClient;
import be.pxl.services.domain.Cart;
import be.pxl.services.domain.Product;
import be.pxl.services.domain.dto.CartResponse;
import be.pxl.services.repository.CartRepository;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
                .customer(cart.getCustomer())
                .items(cart.getItems())
                .total(cart.getTotal())
                .ordered(cart.isOrdered())
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
    public CartResponse addItemToCart(Long id, Long productId) {
        logger.info("Adding product ID {} to cart ID {}", productId, id);

        Cart cart;
        Product product;

        try {
            cart = shoppingCartRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Cart with ID " + id + " not found"));
            logger.debug("Successfully fetched cart with ID {}", id);

            product = mapToProduct(productClient.getProduct(productId));
            logger.debug("Successfully fetched product details for product ID {}", productId);

            cart.addItem(product);
            logger.info("Added product ID {} to cart ID {}", productId, id);

            shoppingCartRepository.save(cart);
            logger.info("Updated cart ID {} saved successfully", id);
        } catch (NotFoundException e) {
            logger.warn("Entity not found: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error occurred while adding product ID {} to cart ID {}: {}",
                    productId, id, e.getMessage(), e);
            throw e;
        }

        return mapToCartResponse(cart);
    }

    @Override
    public CartResponse removeItemFromCart(Long id, Long productId) {
        logger.info("Removing product ID {} from cart ID {}", productId, id);

        Cart cart;
        Product product;

        try {
            cart = shoppingCartRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Cart with ID " + id + " not found"));
            logger.debug("Successfully fetched cart with ID {}", id);

            product = mapToProduct(productClient.getProduct(productId));
            logger.debug("Successfully fetched product details for product ID {}", productId);

            if (!cart.getItems().contains(product)) {
                logger.warn("Product ID {} not found in cart ID {}, cannot remove", productId, id);
                throw new IllegalArgumentException("Product not found in cart");
            }

            cart.removeItem(product);
            logger.info("Removed product ID {} from cart ID {}", productId, id);

            shoppingCartRepository.save(cart);
            logger.info("Updated cart ID {} saved successfully", id);
        } catch (NotFoundException e) {
            logger.warn("Entity not found: {}", e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid operation: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error occurred while removing product ID {} from cart ID {}: {}",
                    productId, id, e.getMessage(), e);
            throw e;
        }

        return mapToCartResponse(cart);
    }

    @Override
    public CartResponse order(Long id, Boolean ordered) {
        logger.info("Updating order status for cart ID {}: ordered = {}", id, ordered);

        Cart cart;

        try {
            cart = shoppingCartRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Cart with ID " + id + " not found"));
            logger.debug("Successfully fetched cart with ID {}", id);

            boolean previousOrderStatus = cart.isOrdered();
            cart.setOrdered(ordered);
            logger.info("Cart ID {}: order status updated from {} to {}", id, previousOrderStatus, ordered);

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
