package be.pxl.services.controller;

import be.pxl.services.domain.dto.CartRequest;
import be.pxl.services.domain.dto.CartResponse;
import be.pxl.services.domain.dto.OrderCartRequest;
import be.pxl.services.services.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final ICartService cartService;
    private static final Logger logger = LoggerFactory.getLogger(CartController.class);


    @GetMapping("/{id}")
    public ResponseEntity getCart(@PathVariable Long id) {
        logger.info("Received GET request at /api/cart/{} to retrieve cart", id);

        try {
            logger.debug("Fetching cart with ID: {}", id);
            CartResponse cartResponse = cartService.getById(id);

            logger.info("Successfully fetched cart with ID: {}", id);
            return new ResponseEntity<>(cartResponse, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error occurred while retrieving cart with ID: {}: {}", id, e.getMessage(), e);
            return new ResponseEntity<>("Failed to fetch cart", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity createCart() {
        logger.info("Received POST request at /api/cart to create a new cart");

        try {
            logger.debug("Creating a new cart...");
            var newCart = cartService.createCart();

            logger.info("Successfully created a new cart with ID: {}", newCart.getId());
            return new ResponseEntity<>(newCart, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error occurred while creating a new cart: {}", e.getMessage(), e);
            return new ResponseEntity<>("Failed to create cart", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/product/add/{productId}")
    public ResponseEntity addItemToCart(@PathVariable Long productId, @RequestBody CartRequest cart) {
        logger.info("Received POST request at /api/carts/product/add/{} to add item to cart", productId);

        try {
            logger.debug("Request body: {}", cart);
            CartResponse updatedCart = cartService.addItemToCart(productId, cart);

            logger.info("Successfully added product with ID: {} to cart", productId);
            return new ResponseEntity<>(updatedCart, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error occurred while adding product with ID: {} to cart: {}", productId, e.getMessage(), e);
            return new ResponseEntity<>("Failed to add item to cart", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/product/remove/{productId}")
    public ResponseEntity removeItemFromCart(@PathVariable Long productId, @RequestBody CartRequest cartRequest) {
        logger.info("Received POST request at /api/cart/product/remove/{} to remove item from cart", productId);

        try {
            logger.debug("Request body: {}", cartRequest);
            CartResponse updatedCart = cartService.removeItemFromCart(productId, cartRequest);

            logger.info("Successfully removed product with ID: {} from the cart", productId);
            return new ResponseEntity<>(updatedCart, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error occurred while removing product with ID: {} from cart: {}", productId, e.getMessage(), e);
            return new ResponseEntity<>("Failed to remove item from cart", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{id}/order")
    public ResponseEntity orderCart(@PathVariable Long id, @RequestBody OrderCartRequest orderCartRequest) {
        logger.info("Received PATCH request at /api/cart/{}/order to order cart", id);

        try {
            logger.debug("Request body: Total={}, Ordered={}", orderCartRequest.getTotal(), orderCartRequest.getOrdered());
            CartResponse updatedCart = cartService.order(id, orderCartRequest.getTotal(), orderCartRequest.getOrdered());

            logger.info("Successfully updated cart with ID: {} to ordered status: {}", id, orderCartRequest.getOrdered());
            return new ResponseEntity<>(updatedCart, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error occurred while ordering cart with ID: {}: {}", id, e.getMessage(), e);
            return new ResponseEntity<>("Failed to order cart", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteCart(@PathVariable Long id) {
        logger.info("Received DELETE request at /api/cart/{} to delete cart", id);

        try {
            cartService.deleteCart(id);
            logger.info("Successfully deleted cart with ID: {}", id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error occurred while deleting cart with ID: {}: {}", id, e.getMessage(), e);
            return new ResponseEntity<>("Failed to delete cart", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
