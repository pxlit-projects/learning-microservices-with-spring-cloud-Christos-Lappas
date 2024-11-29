package be.pxl.services.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="product")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private int stock;
    @Enumerated(EnumType.STRING)
    private Category category;
    @Enumerated(EnumType.STRING)
    private Score score;
    private String labels;

    public Set<String> getLabelSet() {
        return labels == null || labels.isEmpty()
                ? new HashSet<>()
                : new HashSet<>(Arrays.asList(labels.split(",")));
    }

    public void setLabelSet(Set<String> labelSet) {
        this.labels = String.join(",", labelSet);
    }

    public void addLabel(String label) {
        Set<String> labelSet = getLabelSet();
        labelSet.add(label);
        setLabelSet(labelSet);
    }

    public void removeLabel(String label) {
        Set<String> labelSet = getLabelSet();
        labelSet.remove(label);
        setLabelSet(labelSet);
    }

}
