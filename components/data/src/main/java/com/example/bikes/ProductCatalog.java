package com.example.bikes;

import com.example.data.DataFiles;
import com.example.model.Product;
import org.springframework.stereotype.Service;

@Service
public class ProductCatalog {

  private final Product bikes[];

  public ProductCatalog(DataFiles dataFiles) {
    this.bikes = dataFiles.getProducts();
  }

  public Product[] getBikes() {
    return bikes;
  }
}
