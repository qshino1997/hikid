package com.example.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Integer product_id;

    private String name;

    @Column(name = "made_in")
    private String made_in;

    @Column(name = "product_weight")
    private Integer product_weight;

    private Integer price;

    @Column(name = "appropriate_age_start")
    private Integer appropriate_age_start;

    @Column(name = "appropriate_age_end")
    private Integer appropriate_age_end;

    private String warning;

    private String instructions;

    @Column(name = "storage_instructions")
    private String storage_instructions;

    private Integer stock;

    @OneToOne(
            mappedBy = "product",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private ProductImage image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manufacturer_id")
    private Manufacturer manufacturer;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return product_id != null && product_id.equals(product.product_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product_id);
    }
}
