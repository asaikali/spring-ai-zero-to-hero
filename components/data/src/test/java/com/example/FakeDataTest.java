package com.example;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.data.DataFiles;
import com.example.model.Customer;
import com.example.model.Order;
import com.example.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class FakeDataTest {

  @Test
  public void loadCustomers(@Autowired DataFiles dataFiles) {
    Customer[] customers = dataFiles.getCustomers();
    assertThat(customers).hasSize(10);
  }

  @Test
  public void loadProducts(@Autowired DataFiles dataFiles) {
    Product[] products = dataFiles.getProducts();
    assertThat(products).hasSize(25);
  }

  @Test
  public void loadOrders(@Autowired DataFiles dataFiles) {
    Order[] orders = dataFiles.getOrders();
    assertThat(orders).hasSize(6);
  }
}
