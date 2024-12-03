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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {
    private final IProductService productService;
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @GetMapping
    public ResponseEntity getProducts() {
        logger.info("Received GET request at /api/product");

        try {
            logger.debug("Calling ProductService.getAllProducts()");
            List<ProductResponse> products = productService.getAllProducts();
            logger.info("Successfully retrieved {} products", products.size());

            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error occurred while processing GET request at /api/products: {}", e.getMessage(), e);
            return new ResponseEntity<>("Failed to fetch products", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity getProduct(@PathVariable Long id) {
        logger.info("Received GET request at /api/product/{}", id);

        try {
            logger.debug("Calling ProductService.getProduct() with id: {}", id);
            ProductResponse product = productService.getProduct(id);
            logger.info("Successfully retrieved product with id: {}", id);

            return new ResponseEntity<>(product, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error occurred while processing GET request at /api/product/{}: {}", id, e.getMessage(), e);
            return new ResponseEntity<>("Failed to fetch product with id: " + id, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addProduct(@RequestBody ProductRequest productRequest) {
        logger.info("Received POST request at /api/product to add a new product");

        try {
            logger.debug("ProductRequest received: {}", productRequest);
            productService.addProduct(productRequest);
            logger.info("Successfully added product: {}", productRequest.getName());
        } catch (Exception e) {
            logger.error("Error occurred while adding product: {}", e.getMessage(), e);
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteProduct(@PathVariable Long id) {
        logger.info("Received DELETE request at /api/product/{} to remove a product", id);

        try {
            logger.debug("Calling ProductService.removeProduct() with id: {}", id);
            productService.removeProduct(id);
            logger.info("Successfully removed product with id: {}", id);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error occurred while deleting product with id: {}: {}", id, e.getMessage(), e);
            return new ResponseEntity<>("Failed to delete product with id: " + id, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity updateProduct(@PathVariable Long id, @RequestBody ProductRequest productRequest) {
        logger.info("Received PUT request at /api/product/{} to update product", id);

        try {
            logger.debug("ProductRequest received: {}", productRequest);
            logger.debug("Calling ProductService.updateProduct() with id: {}", id);

            ProductResponse updatedProduct = productService.updateProduct(id, productRequest);
            logger.info("Successfully updated product with id: {}", id);

            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error occurred while updating product with id: {}: {}", id, e.getMessage(), e);
            return new ResponseEntity<>("Failed to update product with id: " + id, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/filter")
    public ResponseEntity searchProducts(
            @RequestParam(required = false) Category category,
            @RequestParam(required = false) Score score,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String label,
            @RequestParam(required = false) BigDecimal maxPrice
    ) {

        logger.info("Received GET request at /api/product/filter to search products");
        logger.debug("Filter parameters - Category: {}, Score: {}, Name: {}, Label: {}, MaxPrice: {}",
                category, score, name, label, maxPrice);

        try {
            logger.debug("Calling ProductService.getFilteredProducts() with provided filters");
            List<ProductResponse> filteredProducts = productService.getFilteredProducts(category, score, name, label, maxPrice);

            logger.info("Successfully fetched filtered products");
            return new ResponseEntity<>(filteredProducts, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error occurred while searching products with filters: {}, {}, {}, {}, {}: {}",
                    category, score, name, label, maxPrice, e.getMessage(), e);
            return new ResponseEntity<>("Failed to fetch filtered products", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{id}/labels")
    public ResponseEntity addLabelToProduct(
            @PathVariable Long id,
            @RequestParam String label
    ) {
        logger.info("Received POST request at /api/product/{}/labels to add label", id);
        logger.debug("Label to be added: {}", label);

        try {
            ProductResponse updatedProduct = productService.addLabelToProduct(id, label);
            logger.info("Successfully added label '{}' to product with id: {}", label, id);

            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error occurred while adding label '{}' to product with id: {}: {}", label, id, e.getMessage(), e);
            return new ResponseEntity<>("Failed to add label to product", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}/labels")
    public ResponseEntity removeLabelFromProduct(
            @PathVariable Long id,
            @RequestParam String label
    ) {

        logger.info("Received DELETE request at /api/product/{}/labels to remove label", id);
        logger.debug("Label to be removed: {}", label);

        try {
            ProductResponse product = productService.removeLabelFromProduct(id, label);
            logger.info("Successfully removed label '{}' from product with id: {}", label, id);

            return new ResponseEntity<>(product, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error occurred while removing label '{}' from product with id: {}: {}", label, id, e.getMessage(), e);
            return new ResponseEntity<>("Failed to remove label from product", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
