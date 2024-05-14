package com.example.embed_03;

import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentReader;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.JsonReader;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.ParagraphPdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/embed/03")
public class DocumentController {

  private final Logger logger = LoggerFactory.getLogger(DocumentController.class);
  private final EmbeddingClient embeddingClient;

  @Value("classpath:/data/books/Shakespeare.txt")
  private Resource shakespeareWorksResource;

  @Value("classpath:/data/bikes/bikes.json")
  private Resource bikesResource;

  @Value("classpath:/data/pdf/bylaw.pdf")
  private Resource bylawResource;

  public DocumentController(EmbeddingClient embeddingClient) throws IOException {
    this.embeddingClient = embeddingClient;
  }

  @GetMapping("bikes")
  public String bikeJsonToDocs() {
    DocumentReader reader =
        new JsonReader(bikesResource, "name", "price", "shortDescription", "description");
    List<Document> documents = reader.get();
    Document document = documents.getFirst();
    List<Double> embedding = this.embeddingClient.embed(document);

    return """
                Input file was parsed into %s documents
                Embedding for example document computed has %s dimensions
                document id is %s
                document metadata is %s
                document embedding is %s
                Example contents after the dashed line below
                ---
                %s
                """
        .formatted(
            documents.size(),
            embedding.size(),
            document.getId(),
            document.getMetadata(),
            document.getEmbedding(),
            document.getContent());
  }

  @GetMapping("works")
  public String getShakespeareWorks() {
    DocumentReader reader = new TextReader(shakespeareWorksResource);
    List<Document> documents = reader.get();
    TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();
    List<Document> chunks = tokenTextSplitter.apply(documents);
    Document document = chunks.getFirst();
    List<Double> embedding = this.embeddingClient.embed(document);

    return """
                Input file was parsed into %s documents
                The document was too big and it was split into %s chunks
                Embedding for example document computed has %s dimensions
                document id is %s
                document metadata is %s
                document embedding is %s
                Example contents after the dashed line below
                ---
                %s
                """
        .formatted(
            documents.size(),
            chunks.size(),
            embedding.size(),
            document.getId(),
            document.getMetadata(),
            document.getEmbedding(),
            document.getContent());
  }

  @GetMapping("bylaw")
  public String getBylaw() {
    PagePdfDocumentReader pdfReader =
        new PagePdfDocumentReader(
            bylawResource,
            PdfDocumentReaderConfig.builder()
                .withPageExtractedTextFormatter(
                    ExtractedTextFormatter.builder()
                        .withNumberOfBottomTextLinesToDelete(3)
                        .withNumberOfTopPagesToSkipBeforeDelete(1)
                        .build())
                .withPagesPerDocument(1)
                .build());
    List<Document> documents = pdfReader.get();

    var pdfToDocsSummary =
        """
            Input pdf read from %s
            Each page of the pdf was turned into a Document object
            %s total Document objects were created
            document id is %s
            document metadata is %s
            first document contents between the two dashed lines below
            ---
            %s
            ---
            """
            .formatted(
                bylawResource.getFilename(),
                documents.getFirst().getId(),
                documents.getFirst().getMetadata(),
                documents.size(),
                documents.getFirst().getContent());

    TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();
    List<Document> chunks = tokenTextSplitter.apply(documents);
    Document document = documents.getFirst();
    List<Double> embedding = this.embeddingClient.embed(document);

    var chuckSummary =
        """
                \nPDF page per doc might be too big we split each doc into chunks
                %s chunk documents were created
                Embedding for example first chunk computed has %s dimensions
                document id is %s
                document metadata is %s
                document embedding is %s
                Example contents after the dashed line below
                ---
                %s
                """
            .formatted(
                chunks.size(),
                embedding.size(),
                document.getId(),
                document.getMetadata(),
                document.getEmbedding(),
                document.getContent());

    return pdfToDocsSummary + chuckSummary;
  }

  @GetMapping("para")
  public String paragraphs() {
    ParagraphPdfDocumentReader pdfReader =
        new ParagraphPdfDocumentReader(bylawResource, PdfDocumentReaderConfig.builder().build());

    List<Document> documents = pdfReader.get();

    var pdfToDocsSummary =
        """
            Input pdf read from %s
            Each paragraph of the pdf was turned into a Document object
            %s total Document objects were created
            document id is %s
            document metadata is %s
            first document contents between the two dashed lines below
            ---
            %s
            ---
            """
            .formatted(
                bylawResource.getFilename(),
                documents.getFirst().getId(),
                documents.getFirst().getMetadata(),
                documents.size(),
                documents.getFirst().getContent());

    return pdfToDocsSummary;
  }
}
