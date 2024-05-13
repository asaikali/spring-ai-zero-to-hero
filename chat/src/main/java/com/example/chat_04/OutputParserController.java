package com.example.chat_04;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.parser.BeanOutputParser;
import org.springframework.ai.parser.ListOutputParser;
import org.springframework.ai.parser.MapOutputParser;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController()
@RequestMapping("/chat/04")
public class OutputParserController {
    private final ChatClient chatClient;
    private final Logger logger = LoggerFactory.getLogger(OutputParserController.class);

    public OutputParserController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/plays")
    public String getPlays(@RequestParam(value = "author", defaultValue = "Shakespeare") String topic){
        PromptTemplate promptTemplate = new PromptTemplate(""" 
                Provide a list of the plays written by {author}. 
                Provide only the list no other commentary.
                """);
        promptTemplate.add("author", topic);

        Prompt prompt = promptTemplate.create();

        // call the AI model and get the response.
        ChatResponse response = chatClient.call(prompt);
        AssistantMessage assistantMessage = response.getResult().getOutput();
        return assistantMessage.getContent();
    }

    @GetMapping("/plays/list")
    public List<String> getPlaysList(@RequestParam(value = "author", defaultValue = "Shakespeare") String topic){

        ListOutputParser outputParser = new ListOutputParser(new DefaultConversionService());

        PromptTemplate promptTemplate = new PromptTemplate(""" 
                Provide a list of the plays written by {author}.
                Provide only the list no other commentary.
                {format}
                """);
        promptTemplate.add("author", topic);
        promptTemplate.add( "format", outputParser.getFormat());
        Prompt prompt = promptTemplate.create();

        logger.info("prompt:\n{}", prompt.getContents());

        // call the AI model and get the response.
        ChatResponse response = chatClient.call(prompt);
        AssistantMessage assistantMessage = response.getResult().getOutput();
        String content = assistantMessage.getContent();

        logger.info("respose:\n{}",content);
        return outputParser.parse(content);
    }


    @GetMapping("/plays/map")
    public Map<String,Object> getPlaysMap(@RequestParam(value = "author", defaultValue = "Shakespeare") String topic){
        MapOutputParser outputParser = new MapOutputParser();

        PromptTemplate promptTemplate = new PromptTemplate(""" 
                Provide a list of the plays written by {author}, include the year of publication.
                use JSON format with three fields author,title,year.
                {format}
                """);
        promptTemplate.add("author", topic);
        promptTemplate.add( "format", outputParser.getFormat());
        Prompt prompt = promptTemplate.create();

        logger.info("prompt:\n{}", prompt.getContents());

        // call the AI model and get the response.
        ChatResponse response = chatClient.call(prompt);
        AssistantMessage assistantMessage = response.getResult().getOutput();
        String content = assistantMessage.getContent();

        logger.info("respose:\n{}",content);
        return outputParser.parse(content);
    }

    @GetMapping("/plays/object")
    public Play[] getPlaysObject(@RequestParam(value = "author", defaultValue = "Shakespeare") String topic){
        BeanOutputParser outputParser = new BeanOutputParser(Play[].class);

        PromptTemplate promptTemplate = new PromptTemplate(""" 
                Provide a list of the plays written by {author}, include the year of publication.
                {format}
                """);
        promptTemplate.add("author", topic);
        promptTemplate.add( "format", outputParser.getFormat());
        Prompt prompt = promptTemplate.create();

        logger.info("prompt:\n{}", prompt.getContents());

        // call the AI model and get the response.
        ChatResponse response = chatClient.call(prompt);
        AssistantMessage assistantMessage = response.getResult().getOutput();
        String content = assistantMessage.getContent();

        logger.info("respose:\n{}",content);
        return (Play[]) outputParser.parse(content);
    }
}

record Play(String author, String title, String year){}