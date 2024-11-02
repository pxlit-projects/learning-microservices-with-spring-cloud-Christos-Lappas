package be.pxl.services.services;

import be.pxl.services.domain.Category;
import be.pxl.services.domain.Product;
import be.pxl.services.domain.Score;
import be.pxl.services.domain.dto.ProductRequest;
import be.pxl.services.domain.dto.ProductResponse;
import be.pxl.services.repository.ProductRepository;
import be.pxl.services.specification.ProductSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{
    private final ProductRepository productRepository;
    @Override
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(product -> mapToProductResponse(product)).toList();
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .category(product.getCategory())
                .score(product.getScore())
                .build();
    }

    @Override
    public void addProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .stock(productRequest.getStock())
                .category(productRequest.getCategory())
                .score(productRequest.getScore())
                .build();

        productRepository.save(product);

    }

    @Override
    public ProductResponse getProduct(Long id) {
        Product product = productRepository.findById(id).get();
        return mapToProductResponse(product);
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest productRequest) {
        Product product = productRepository.findById(id).get();

        product.setName(productRequest.getName());
        product.setDescription(product.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setStock(productRequest.getStock());
        product.setCategory(productRequest.getCategory());
        product.setScore(productRequest.getScore());

        productRepository.save(product);

        return mapToProductResponse(product);
    }

    @Override
    public List<ProductResponse> getProductsByCategory(Category category) {
        List<Product> products = productRepository.findProductsByCategory(category);
        return products.stream().map(product -> mapToProductResponse(product)).toList();
    }

    @Override
    public List<ProductResponse> getProductsByScore(Score score) {
        List<Product> products = productRepository.findProductsByScore(score);
        return products.stream().map(product -> mapToProductResponse(product)).toList();
    }

    @Override
    public List<ProductResponse> getFilteredProducts(Category category, Score score, String name) {
        Specification<Product> spec = Specification.where(ProductSpecification.hasCategory(category))
                .and(ProductSpecification.hasScore(score))
                .and(ProductSpecification.hasName(name));

        return productRepository.findAll(spec).stream().map(product -> mapToProductResponse(product)).toList();
    }
}
