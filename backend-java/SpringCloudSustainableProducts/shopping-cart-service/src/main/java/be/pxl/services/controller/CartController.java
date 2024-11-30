package be.pxl.services.controller;

import be.pxl.services.domain.dto.CartRequest;
import be.pxl.services.domain.dto.OrderCartRequest;
import be.pxl.services.services.CartService;
import be.pxl.services.services.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

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

    @PostMapping("/product/add/{productId}")
    public ResponseEntity addItemToCart(@PathVariable Long productId, @RequestBody CartRequest cartRequest) {
        return new ResponseEntity(cartService.addItemToCart(productId, cartRequest), HttpStatus.OK);
    }

    @PostMapping("/product/remove/{productId}")
    public ResponseEntity removeItemFromCart(@PathVariable Long productId, @RequestBody CartRequest cartRequest) {
        return new ResponseEntity(cartService.removeItemFromCart(productId, cartRequest), HttpStatus.OK);
    }

    @PatchMapping("/{id}/order")
    public ResponseEntity orderCart(@PathVariable Long id, @RequestBody OrderCartRequest orderCartRequest) {
        return new ResponseEntity(cartService.order(id, orderCartRequest.getTotal(), orderCartRequest.getOrdered()), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteCart(@PathVariable Long id) {
        cartService.deleteCart(id);
        return new ResponseEntity(HttpStatus.OK);
    }

}
