package be.pxl.services.controller;

import be.pxl.services.services.CartService;
import be.pxl.services.services.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final ICartService cartService;

    @GetMapping("/{customerId}")
    public ResponseEntity getCart(@PathVariable Long customerId) {
        return new ResponseEntity(cartService.getByCustomerId(customerId), HttpStatus.OK);
    }

    @PostMapping("/{customerId}")
    public ResponseEntity createCart(@PathVariable Long customerId) {
        return new ResponseEntity(cartService.createCart(customerId), HttpStatus.OK);
    }

    @PostMapping("/{customerId}/add/{productId}")
    public ResponseEntity addItemToCart(@PathVariable Long customerId, @PathVariable Long productId) {
        return new ResponseEntity(cartService.addItemToCart(customerId, productId), HttpStatus.OK);
    }

    @PostMapping("/{customerId}/remove/{productId}")
    public ResponseEntity removeItemFromCart(@PathVariable Long customerId, @PathVariable Long productId) {
        return new ResponseEntity(cartService.removeItemFromCart(customerId, productId), HttpStatus.OK);
    }

}
