package com.example.chat_04;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/chat/04")
public class StructuredOutputConverterController {
  private final ChatModel chatModel;
  private final Logger logger = LoggerFactory.getLogger(StructuredOutputConverterController.class);

  public StructuredOutputConverterController(ChatModel chatModel) {
    this.chatModel = chatModel;
  }

  @GetMapping("/plays")
  public String getPlays(
      @RequestParam(value = "author", defaultValue = "Shakespeare") String topic) {
    PromptTemplate promptTemplate =
        new PromptTemplate(
            """
                Provide a list of the plays written by {author}.
                Provide only the list no other commentary.
                """);
    promptTemplate.add("author", topic);

    Prompt prompt = promptTemplate.create();

    // call the AI model and get the response.
    ChatResponse response = chatModel.call(prompt);
    AssistantMessage assistantMessage = response.getResult().getOutput();
    return assistantMessage.getContent();
  }

  @GetMapping("/plays/list")
  public List<String> getPlaysList(
      @RequestParam(value = "author", defaultValue = "Shakespeare") String topic) {

    ListOutputConverter outputConverter = new ListOutputConverter(new DefaultConversionService());

    PromptTemplate promptTemplate =
        new PromptTemplate(
            """
                Provide a list of the plays written by {author}.
                Provide only the list no other commentary.
                {format}
                """);
    promptTemplate.add("author", topic);
    promptTemplate.add("format", outputConverter.getFormat());
    Prompt prompt = promptTemplate.create();

    logger.info("prompt:\n{}", prompt.getContents());

    // call the AI model and get the response.
    ChatResponse response = chatModel.call(prompt);
    AssistantMessage assistantMessage = response.getResult().getOutput();
    String content = assistantMessage.getContent();

    logger.info("respose:\n{}", content);
    return outputConverter.convert(content);
  }

  @GetMapping("/plays/map")
  public Map<String, Object> getPlaysMap(
      @RequestParam(value = "author", defaultValue = "Shakespeare") String topic) {
    MapOutputConverter outputConverter = new MapOutputConverter();

    PromptTemplate promptTemplate =
        new PromptTemplate(
            """
                Provide a list of the plays written by {author}, include the year of publication.
                use JSON format with three fields author,title,year.
                {format}
                """);
    promptTemplate.add("author", topic);
    promptTemplate.add("format", outputConverter.getFormat());
    Prompt prompt = promptTemplate.create();

    logger.info("prompt:\n{}", prompt.getContents());

    // call the AI model and get the response.
    ChatResponse response = chatModel.call(prompt);
    AssistantMessage assistantMessage = response.getResult().getOutput();
    String content = assistantMessage.getContent();

    logger.info("respose:\n{}", content);
    return outputConverter.convert(content);
  }

  @GetMapping("/plays/object")
  public Play[] getPlaysObject(
      @RequestParam(value = "author", defaultValue = "Shakespeare") String topic) {
    BeanOutputConverter<Play[]> outputConverter = new BeanOutputConverter<>(Play[].class);

    PromptTemplate promptTemplate =
        new PromptTemplate(
            """
                Provide a list of the plays written by {author}, include the year of publication.
                {format}
                """);
    promptTemplate.add("author", topic);
    promptTemplate.add("format", outputConverter.getFormat());
    Prompt prompt = promptTemplate.create();

    logger.info("prompt:\n{}", prompt.getContents());

    // call the AI model and get the response.
    ChatResponse response = chatModel.call(prompt);
    AssistantMessage assistantMessage = response.getResult().getOutput();
    String content = assistantMessage.getContent();

    logger.info("respose:\n{}", content);
    return (Play[]) outputConverter.convert(content);
  }
}

record Play(String author, String title, Integer year) {}
