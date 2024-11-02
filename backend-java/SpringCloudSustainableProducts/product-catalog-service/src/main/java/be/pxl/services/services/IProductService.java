package be.pxl.services.services;

import be.pxl.services.domain.Category;
import be.pxl.services.domain.Score;
import be.pxl.services.domain.dto.ProductRequest;
import be.pxl.services.domain.dto.ProductResponse;

import java.util.List;

public interface IProductService {
    List<ProductResponse> getAllProducts();
    void addProduct(ProductRequest productRequest);

    ProductResponse getProduct(Long id);
    ProductResponse updateProduct(Long id, ProductRequest productRequest);

    List<ProductResponse> getProductsByCategory(Category category);
    List<ProductResponse> getProductsByScore(Score score);

    List<ProductResponse> getFilteredProducts(Category category, Score score,String name);
}
