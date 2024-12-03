package be.pxl.services.specification;

import be.pxl.services.domain.Category;
import be.pxl.services.domain.Product;
import be.pxl.services.domain.Score;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProductSpecification {
    public static Specification<Product> hasCategory(Category category) {
        return (root, query, criteriaBuilder) ->
                category == null ? null : criteriaBuilder.equal(root.get("category"), category);
    }

    public static Specification<Product> hasScore(Score score) {
        return (root, query, criteriaBuilder) ->
                score == null ? null : criteriaBuilder.equal(root.get("score"), score);
    }

    public static Specification<Product> hasName(String name) {
        return (root, query, criteriaBuilder) ->
                (name == null || name.isEmpty()) ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Product> hasLabel(String label) {
        return (root, query, criteriaBuilder) ->
                (label == null || label.isEmpty()) ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("labels")), "%" + label.toLowerCase() + "%");
    }

    public static Specification<Product> hasPriceBelow(BigDecimal maxPrice) {
        return (root, query, criteriaBuilder) ->
                maxPrice == null ? null : criteriaBuilder.between(root.get("price"), BigDecimal.ZERO, maxPrice);
    }
}
