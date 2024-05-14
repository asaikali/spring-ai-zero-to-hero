package com.example.image_01;

import org.springframework.ai.image.Image;
import org.springframework.ai.image.ImageClient;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiAudioTranscriptionClient;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.ai.openai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.openai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/image/01")
public class ImageController {

  private final ImageClient imageClient;

  public ImageController( ImageClient imageClient) {
      this.imageClient = imageClient;
  }

  @GetMapping("make")
  public Image transcribe() {

    ImageResponse response = imageClient.call(
            new ImagePrompt("A light cream colored mini golden doodle",
                    OpenAiImageOptions.builder()
                            .withQuality("hd")
                            .withN(1)
                            .withHeight(1024)
                            .withWidth(1024).build())

    );

    Image image =  response.getResult().getOutput();
    return image;
  }

}
