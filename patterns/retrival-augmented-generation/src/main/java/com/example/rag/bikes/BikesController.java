package com.example.rag.bikes;

import com.example.data.DataFiles;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vector/")
public class BikesController {
  private final Logger logger = LoggerFactory.getLogger(BikesController.class);

  private final DataFiles dataFiles;
  private final ChatClient chatClient;

  public BikesController(ChatClient.Builder builder, DataFiles dataFiles) throws IOException {
    this.chatClient = builder.build();
    this.dataFiles = dataFiles;
  }

  @GetMapping("/md")
  public void markdown2() throws IOException {
    var bikesJson = this.dataFiles.getBikesResource().getContentAsString(StandardCharsets.UTF_8);
    var mapper = new ObjectMapper();

    // Parse the JSON string into a list of bikes
    List<Map<String, Object>> bikes =
        mapper.readValue(bikesJson, new TypeReference<List<Map<String, Object>>>() {});

    // Iterate over each bike in the list
    for (int i = 0; i < bikes.size(); i++) {
      Map<String, Object> bike = bikes.get(i);

      // Extract the required fields from the bike object
      String name = (String) bike.get("name");
      String shortDescription = (String) bike.get("shortDescription");
      String description = (String) bike.get("description");

      // Create the filename with the pattern xx-name.md
      String filename = String.format("%02d-%s.md", i + 1, name.replaceAll("\\s+", "-"));

      // Write the fields to the file as raw bytes
      try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
        writer.write("# " + name + "\n\n");
        writer.write(shortDescription + "\n");
        writer.write(description + "\n");
      }
    }
  }
}
