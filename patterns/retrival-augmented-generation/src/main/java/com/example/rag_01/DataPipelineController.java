package com.example.rag_01;

import com.example.data.DataFiles;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rag/01")
public class DataPipelineController {

  private final DataFiles dataFiles;
  private final VectorStore vectorStore;

  public DataPipelineController(VectorStore vectorStore, DataFiles dataFiles) {
    this.dataFiles = dataFiles;
    this.vectorStore = vectorStore;
  }

  @GetMapping("load")
  String load() {

    return "load";
  }
}
