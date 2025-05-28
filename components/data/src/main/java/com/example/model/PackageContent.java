// PackageContent.java
package com.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PackageContent {
    @JsonProperty("product_id")
    private int productId;
    private int quantity;

    public int getProductId() {
        return productId;
    }

    public PackageContent setProductId(int productId) {
        this.productId = productId;
        return this;
    }

    public int getQuantity() {
        return quantity;
    }

    public PackageContent setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }
}
