package be.pxl.services.controller;

import be.pxl.services.domain.Category;
import be.pxl.services.domain.Score;
import be.pxl.services.domain.dto.ProductRequest;
import be.pxl.services.domain.dto.ProductResponse;
import be.pxl.services.services.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {
    private final IProductService productService;
    @GetMapping
    public ResponseEntity getProducts() {
        return new ResponseEntity(productService.getAllProducts(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity getProduct(@PathVariable Long id) {
        return new ResponseEntity(productService.getProduct(id), HttpStatus.OK);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addProduct(@RequestBody ProductRequest productRequest) {
        productService.addProduct(productRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteProduct(@PathVariable Long id) {
        productService.removeProduct(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateProduct(@PathVariable Long id, @RequestBody ProductRequest productRequest) {
        return new ResponseEntity(productService.updateProduct(id, productRequest), HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity searchProducts(
            @RequestParam(required = false) Category category,
            @RequestParam(required = false) Score score,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String label,
            @RequestParam(required = false) BigDecimal maxPrice
    ) {

        return new ResponseEntity(productService.getFilteredProducts(category, score, name, label, maxPrice), HttpStatus.OK);
    }

    @PostMapping("/{id}/labels")
    public ResponseEntity<ProductResponse> addLabelToProduct(
            @PathVariable Long id,
            @RequestParam String label
    ) {
        return new ResponseEntity(productService.addLabelToProduct(id, label), HttpStatus.OK);
    }

    @DeleteMapping("/{id}/labels")
    public ResponseEntity removeLabelFromProduct(
            @PathVariable Long id,
            @RequestParam String label
    ) {

        return new ResponseEntity(productService.removeLabelFromProduct(id, label), HttpStatus.OK);
    }


}
