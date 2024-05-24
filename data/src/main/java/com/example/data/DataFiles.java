package com.example.data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class DataFiles {

  @Value("classpath:/data/books/Shakespeare.txt")
  private Resource shakespeareWorksResource;

  @Value("classpath:/data/bikes/bikes.json")
  private Resource bikesResource;

  @Value("classpath:/data/pdf/bylaw.pdf")
  private Resource bylawResource;

  public Resource getBylawResource() {
    return bylawResource;
  }

  public Resource getBikesResource() {
    return bikesResource;
  }

  public Resource getShakespeareWorksResource() {
    return shakespeareWorksResource;
  }
}
