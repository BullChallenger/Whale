package com.example.whale.domain.product.model;

import java.util.Set;

public class ProductGroup {

    private final String productGroupId;
    private Set<Product> products;

    public ProductGroup(String productGroupId) {
        this.productGroupId = productGroupId;
    }

}
