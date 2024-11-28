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

    @GetMapping("/{id}")
    public ResponseEntity getCart(@PathVariable Long id) {
        return new ResponseEntity(cartService.getById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity createCart() {
        return new ResponseEntity(cartService.createCart(), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/add/{productId}")
    public ResponseEntity addItemToCart(@PathVariable Long customerId, @PathVariable Long productId) {
        return new ResponseEntity(cartService.addItemToCart(customerId, productId), HttpStatus.OK);
    }

    @PostMapping("/{id}/remove/{productId}")
    public ResponseEntity removeItemFromCart(@PathVariable Long customerId, @PathVariable Long productId) {
        return new ResponseEntity(cartService.removeItemFromCart(customerId, productId), HttpStatus.OK);
    }

    @PatchMapping("/{id}/order")
    public ResponseEntity orderCart(@PathVariable Long id, @RequestBody Boolean ordered) {
        return new ResponseEntity(cartService.order(id, ordered), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteCart(@PathVariable Long id) {
        cartService.deleteCart(id);
        return new ResponseEntity(HttpStatus.OK);
    }

}
