package be.pxl.services.services;

import be.pxl.services.domain.Category;
import be.pxl.services.domain.Score;
import be.pxl.services.domain.dto.ProductRequest;
import be.pxl.services.domain.dto.ProductResponse;

import java.util.List;

public interface IProductService {
    List<ProductResponse> getAllProducts();
    void addProduct(ProductRequest productRequest);
    void removeProduct(Long id);
    ProductResponse getProduct(Long id);
    ProductResponse updateProduct(Long id, ProductRequest productRequest);
    List<ProductResponse> getFilteredProducts(Category category, Score score,String name, String label);
    ProductResponse addLabelToProduct(Long id, String label);
    void removeLabelFromProduct(Long id, String label);
}
