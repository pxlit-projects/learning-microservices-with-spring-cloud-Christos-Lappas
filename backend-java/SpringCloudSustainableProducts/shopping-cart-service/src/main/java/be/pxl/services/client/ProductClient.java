package be.pxl.services.client;

import be.pxl.services.domain.Product;
import jakarta.ws.rs.Path;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "product-catalog-service")
public interface ProductClient {

    @GetMapping("/api/product")
    List<Product> getAllProduct();

    @GetMapping("/api/product/{id}")
    Product getProduct(@PathVariable Long id);
}
