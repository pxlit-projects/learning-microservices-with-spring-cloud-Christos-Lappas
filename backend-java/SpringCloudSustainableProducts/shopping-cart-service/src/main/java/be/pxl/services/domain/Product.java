package be.pxl.services.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;

public class Product {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
//    private Integer quantity;
    private Category category;
    private Score score;
}
