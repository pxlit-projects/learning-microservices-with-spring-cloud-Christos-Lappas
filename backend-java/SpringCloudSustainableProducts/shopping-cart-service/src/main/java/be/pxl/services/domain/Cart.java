package be.pxl.services.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="cart")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String customer = "customer";
    @Transient
    private List<Product> items = new ArrayList<>();
    private BigDecimal total;
    private boolean ordered;

    public void addItem(Product item) {
        items.add(item);
    }

    public void removeItem(Product item) {
        items.remove(item);
    }
}
