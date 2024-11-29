package be.pxl.services.services;

import be.pxl.services.client.LogbookClient;
import be.pxl.services.domain.Category;
import be.pxl.services.domain.Product;
import be.pxl.services.domain.Score;
import be.pxl.services.domain.dto.LogRequest;
import be.pxl.services.domain.dto.ProductRequest;
import be.pxl.services.domain.dto.ProductResponse;
import be.pxl.services.repository.ProductRepository;
import be.pxl.services.specification.ProductSpecification;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{
    private final ProductRepository productRepository;
    private final LogbookClient logbookClient;
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    @Override
    public List<ProductResponse> getAllProducts() {
        logger.info("Fetching all products from the database");

        long startTime = System.currentTimeMillis();

        List<Product> products;
        try {
            products = productRepository.findAll();
            logger.debug("Fetched {} products from the database", products.size());
        } catch (Exception e) {
            logger.error("Error occurred while fetching products: {}", e.getMessage(), e);
            throw e;
        }

        List<ProductResponse> productResponses = products.stream()
                .map(product -> mapToProductResponse(product))
                .toList();

        long endTime = System.currentTimeMillis();
        logger.info("Successfully retrieved {} products in {} ms", productResponses.size(), (endTime - startTime));

        return productResponses;
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .category(product.getCategory())
                .score(product.getScore())
                .labels(product.getLabels())
                .build();
    }

    @Override
    public void addProduct(ProductRequest productRequest) {
        logger.info("Starting to add a new product with name: {}", productRequest.getName());

        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .stock(productRequest.getStock())
                .category(productRequest.getCategory())
                .score(productRequest.getScore())
                .labels(productRequest.getLabels())
                .build();

        try {
            productRepository.save(product);
            logger.info("Product with name '{}' successfully added to the database", product.getName());
        } catch (Exception e) {
            logger.error("Failed to add product with name '{}': {}", productRequest.getName(), e.getMessage(), e);
            throw e;
        }

    }

    @Override
    public void removeProduct(Long id) {
        logger.info("Attempting to delete product with ID {}", id);

        Product product;

        try {
            product = productRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Product with ID " + id + " not found"));
            logger.debug("Successfully fetched product with ID {}", id);

            productRepository.delete(product);
            logger.info("Successfully deleted product with ID {}", id);
        } catch (NotFoundException e) {
            logger.warn("Entity not found: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error occurred while deleting product with ID {}: {}", id, e.getMessage(), e);
            throw e;
        }

    }

    @Override
    public ProductResponse getProduct(Long id) {
        logger.info("Getting product...");
        try {
            Product product = productRepository.findById(id).get();
            return mapToProductResponse(product);
        } catch (Exception e) {
            logger.error("Error occurred while getting product", e);
        }

        return null;
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest productRequest) {
        logger.info("Starting update for product with id: {}", id);

        Product product;
        try {
            product = productRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.error("Product with id {} not found", id);
                        return new NotFoundException("Product not found");
                    });
        } catch (Exception e) {
            logger.error("Error occurred while fetching product with id {}: {}", id, e.getMessage(), e);
            throw e;
        }

        StringBuilder updateLog = new StringBuilder();
        updateLog.append("Following changes made for product with id ").append(id).append(": ");

        boolean changesMade = false;

        if (!Objects.equals(product.getName(), productRequest.getName())) {
            updateLog.append("Name changed from '").append(product.getName())
                    .append("' to '").append(productRequest.getName()).append("'. ");
            changesMade = true;
        }
        if (!Objects.equals(product.getDescription(), productRequest.getDescription())) {
            updateLog.append("Description changed from '").append(product.getDescription())
                    .append("' to '").append(productRequest.getDescription()).append("'. ");
            changesMade = true;
        }
        if (!Objects.equals(product.getPrice(), productRequest.getPrice())) {
            updateLog.append("Price changed from ").append(product.getPrice())
                    .append(" to ").append(productRequest.getPrice()).append(". ");
            changesMade = true;
        }
        if (!Objects.equals(product.getStock(), productRequest.getStock())) {
            updateLog.append("Stock changed from ").append(product.getStock())
                    .append(" to ").append(productRequest.getStock()).append(". ");
            changesMade = true;
        }
        if (!Objects.equals(product.getCategory(), productRequest.getCategory())) {
            updateLog.append("Category changed from '").append(product.getCategory())
                    .append("' to '").append(productRequest.getCategory()).append("'. ");
            changesMade = true;
        }
        if (!Objects.equals(product.getScore(), productRequest.getScore())) {
            updateLog.append("Score changed from ").append(product.getScore())
                    .append(" to ").append(productRequest.getScore()).append(". ");
            changesMade = true;
        }

        if (!changesMade) {
            updateLog.append("No changes were made.");
        }

        logger.debug("Updating product fields for id: {}", id);
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription()); // fixed typo
        product.setPrice(productRequest.getPrice());
        product.setStock(productRequest.getStock());
        product.setCategory(productRequest.getCategory());
        product.setScore(productRequest.getScore());

        productRepository.save(product);
        logger.info("Product with id {} successfully updated", id);



        logger.info("Logging changes for product with id: {}", id);
        try {
            LogRequest logRequest = new LogRequest();
            logRequest.setProductId(product.getId());
            logRequest.setChanges(updateLog.toString());
            logbookClient.addLog(logRequest);
        } catch (Exception e) {
            logger.error("Error serializing changes map to JSON: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to log changes", e);
        }

        logger.info("Changes for product with id: {} logged", id);

        return mapToProductResponse(product);
    }

    @Override
    public List<ProductResponse> getFilteredProducts(Category category, Score score, String name, String label) {
        logger.info("Fetching filtered products with criteria - Category: {}, Score: {}, Name: {}, Label: {}",
                category, score, name, label);

        Specification<Product> spec = Specification.where(ProductSpecification.hasCategory(category))
                .and(ProductSpecification.hasScore(score))
                .and(ProductSpecification.hasName(name))
                .and(ProductSpecification.hasLabel(label));

        List<ProductResponse> productResponses;
        try {
            List<Product> filteredProducts = productRepository.findAll(spec);
            productResponses = filteredProducts.stream()
                    .map(product -> mapToProductResponse(product))
                    .toList();
            logger.info("Found {} products matching the filter criteria", productResponses.size());
        } catch (Exception e) {
            logger.error("Error occurred while fetching filtered products: {}", e.getMessage(), e);
            throw e;
        }

        logger.info("Successfully retrieved filtered products");
        return productResponses;
    }

    @Override
    public ProductResponse addLabelToProduct(Long id, String label) {
        logger.info("Attempting to add {} label to product with ID {}",label, id);

        Product product;

        try {
            product = productRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Product with ID " + id + " not found"));
            logger.debug("Successfully fetched product with ID {}", id);

            product.addLabel(label);
            productRepository.save(product);
            logger.info("Successfully added  {} label to product with ID {}", label, id);
        } catch (NotFoundException e) {
            logger.warn("Entity not found: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error occurred while adding {} label to product with ID {}: {}",label, id, e.getMessage(), e);
            throw e;
        }

        return mapToProductResponse(product);

    }

    @Override
    public void removeLabelFromProduct(Long id, String label) {
        logger.info("Attempting to delete {} label from product with ID {}",label, id);

        Product product;

        try {
            product = productRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Product with ID " + id + " not found"));
            logger.debug("Successfully fetched product with ID {}", id);

            product.removeLabel(label);
            productRepository.save(product);
            logger.info("Successfully deleted {} label from product with ID {}", label, id);
        } catch (NotFoundException e) {
            logger.warn("Entity not found: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error occurred while deleting {} label from product with ID {}: {}",label, id, e.getMessage(), e);
            throw e;
        }

    }
}
