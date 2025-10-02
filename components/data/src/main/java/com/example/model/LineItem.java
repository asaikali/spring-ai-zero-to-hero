// LineItem.java
package com.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public class LineItem {
  @JsonProperty("product_id")
  private int productId;

  private int quantity;

  @JsonProperty("unit_price")
  private BigDecimal unitPrice;

  @JsonProperty("total_price")
  private BigDecimal totalPrice;

  public int getProductId() {
    return productId;
  }

  public LineItem setProductId(int productId) {
    this.productId = productId;
    return this;
  }

  public int getQuantity() {
    return quantity;
  }

  public LineItem setQuantity(int quantity) {
    this.quantity = quantity;
    return this;
  }

  public BigDecimal getUnitPrice() {
    return unitPrice;
  }

  public LineItem setUnitPrice(BigDecimal unitPrice) {
    this.unitPrice = unitPrice;
    return this;
  }

  public BigDecimal getTotalPrice() {
    return totalPrice;
  }

  public LineItem setTotalPrice(BigDecimal totalPrice) {
    this.totalPrice = totalPrice;
    return this;
  }
}
