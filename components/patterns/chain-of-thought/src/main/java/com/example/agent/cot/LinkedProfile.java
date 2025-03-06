package com.example.agent.cot;

import java.util.List;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.core.io.Resource;

public class LinkedProfile {
  private List<Document> documents;

  public LinkedProfile(Resource profile) {
    TikaDocumentReader tikaDocumentReader = new TikaDocumentReader(profile);
    this.documents = tikaDocumentReader.read();
  }

  String getProfileAsString() {
    return this.documents.stream().map(Document::getText).reduce("", String::concat);
  }
}
