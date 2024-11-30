package be.pxl.services.domain.dto;

import be.pxl.services.domain.Product;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {
    private Long id;
    private String customer;
    private List<Product> items = new ArrayList<>();
    private BigDecimal total;
    private boolean ordered;
}
