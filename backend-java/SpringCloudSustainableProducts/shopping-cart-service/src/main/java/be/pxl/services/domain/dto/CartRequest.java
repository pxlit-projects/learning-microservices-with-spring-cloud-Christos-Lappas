package be.pxl.services.domain.dto;

import be.pxl.services.domain.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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
public class CartRequest {
    private Long id;
    private String customer;
    private List<Product> items = new ArrayList<>();
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal total;
    private boolean ordered;

    @JsonIgnore
    public void addItem(Product item) {
        items.add(item);
    }
    @JsonIgnore
    public void removeItem(Product item) {
        items.remove(item);
    }
}
