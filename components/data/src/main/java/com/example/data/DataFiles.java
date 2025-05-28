package com.example.data;

import com.example.model.Customer;
import com.example.model.Order;
import com.example.model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class DataFiles {

  @Value("classpath:/data/books/Shakespeare.txt")
  private Resource shakespeareWorksResource;

  @Value("classpath:/data/bikes/bikes.json")
  private Resource bikesResource;

  @Value("classpath:/data/bikes/customers.json")
  private Resource customersResource;

  @Value("classpath:/data/bikes/orders.json")
  private Resource ordersResource;

  @Value("classpath:/data/pdf/bylaw.pdf")
  private Resource bylawResource;

  private final ObjectMapper objectMapper;

  public DataFiles(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public Resource getBylawResource() {
    return bylawResource;
  }

  public Resource getBikesResource() {
    return bikesResource;
  }

  public Resource getShakespeareWorksResource() {
    return shakespeareWorksResource;
  }

  public Product[] getProducts() {
    try {
      return objectMapper.readValue(bikesResource.getInputStream(), Product[].class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public Customer[] getCustomers() {
    try {
      return objectMapper.readValue(customersResource.getInputStream(), Customer[].class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public Order[] getOrders() {
    try {
      return objectMapper.readValue(ordersResource.getInputStream(), Order[].class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
