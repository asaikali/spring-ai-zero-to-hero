package com.example.embed_02;

import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;


import java.util.List;
import java.util.stream.Stream;




@RestController
@RequestMapping("/embed/02")
public class SimilarityController {

  private final EmbeddingClient embeddingClient;

  public SimilarityController(EmbeddingClient embeddingClient) {
    this.embeddingClient = embeddingClient;
  }

  record Score(String a, String b, double similarity) {}
  private Score similarity(String a, String b){
    var embeddingA = embeddingClient.embed(a);
    var embeddingB = embeddingClient.embed(b);
    // 1.0  indicates perfect similarity
    // 0.0  indicates no similarity
    var similarity = SimpleVectorStore.EmbeddingMath.cosineSimilarity(embeddingA, embeddingB);

    return new Score(a, b, similarity);
  }

  @GetMapping("words")
  public List<Score> words() {
    return Stream.of(
                    similarity("man", "man"),
                    similarity("man", "woman"),
                    similarity("man", "dirt"),
                    similarity("king", "queen"),
                    similarity("queen", "reine"),
                    similarity("queen","ملكة" ),
                    similarity("banana", "car"),
                    similarity("happy", "joyful"),
                    similarity("happy", "sad")
            )
            .sorted(Comparator.comparingDouble(Score::similarity).reversed())
            .toList();
  }


  @GetMapping("quotes")
  public List<String> getDimension(@RequestParam(value = "topic", defaultValue = "getting over a losing a job") String topic) {

    List<String> quotes = Arrays.asList(
            // Importance of Education
            "Education is the most powerful weapon which you can use to change the world. – Nelson Mandela",
            "The only person who is educated is the one who has learned how to learn and change. – Carl Rogers",
            "An investment in knowledge pays the best interest. – Benjamin Franklin",
            "Education is not the filling of a pail, but the lighting of a fire. – William Butler Yeats",
            "The roots of education are bitter, but the fruit is sweet. – Aristotle",

            // Being Kind to Others
            "No act of kindness, no matter how small, is ever wasted. – Aesop",
            "Kindness is a language which the deaf can hear and the blind can see. – Mark Twain",
            "Carry out a random act of kindness, with no expectation of reward, safe in the knowledge that one day someone might do the same for you. – Princess Diana",
            "A single act of kindness throws out roots in all directions, and the roots spring up and make new trees. – Amelia Earhart",
            "The best way to find yourself is to lose yourself in the service of others. – Mahatma Gandhi",

            // Contributing to Others
            "The best way to find yourself is to lose yourself in the service of others. – Mahatma Gandhi",
            "We make a living by what we get. We make a life by what we give. – Winston Churchill",
            "No one has ever become poor by giving. – Anne Frank",
            "The meaning of life is to find your gift. The purpose of life is to give it away. – Pablo Picasso",
            "Only a life lived for others is a life worthwhile. – Albert Einstein",

            // Hard Work
            "There is no substitute for hard work. – Thomas Edison",
            "The only place where success comes before work is in the dictionary. – Vidal Sassoon",
            "I’m a greater believer in luck, and I find the harder I work the more I have of it. – Thomas Jefferson",
            "Success is not the result of spontaneous combustion. You must set yourself on fire. – Arnold H. Glasow",
            "Hard work beats talent when talent doesn’t work hard. – Tim Notke",

            // Overcoming Failure
            "Failure is simply the opportunity to begin again, this time more intelligently. – Henry Ford",
            "Success is not final, failure is not fatal: It is the courage to continue that counts. – Winston Churchill",
            "Our greatest glory is not in never falling, but in rising every time we fall. – Confucius",
            "The only real mistake is the one from which we learn nothing. – Henry Ford",
            "I have not failed. I've just found 10,000 ways that won't work. – Thomas Edison"
    );


    var embeddings = quotes.stream().map(quote -> embeddingClient.embed(quote)).toList();
    var topicEmbedding = embeddingClient.embed(topic);


    double topScore = 0;
    String topQuote = "cloud not find a quote that matches the topic";
    for(int i=0; i < embeddings.size(); i++) {
      var score = SimpleVectorStore.EmbeddingMath.cosineSimilarity(topicEmbedding,embeddings.get(i));
      if(score > topScore) {
        topScore = score;
        topQuote = quotes.get(i);
      }
    }
      return List.of(topQuote);
  }
}
