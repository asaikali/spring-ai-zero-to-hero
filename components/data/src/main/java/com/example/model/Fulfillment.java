// Fulfillment.java
package com.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Fulfillment {
    @JsonProperty("packages")
    private List<ShipmentPackage> packages;

    public List<ShipmentPackage> getPackages() {
        return packages;
    }

    public Fulfillment setPackages(List<ShipmentPackage> packages) {
        this.packages = packages;
        return this;
    }
}
