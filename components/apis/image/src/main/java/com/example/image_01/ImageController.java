package com.example.image_01;

import org.springframework.ai.image.Image;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImageOptionsBuilder;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/image/01")
public class ImageController {

  private final ImageModel imageModel;

  public ImageController(ImageModel imageModel) {
    this.imageModel = imageModel;
  }

  @GetMapping("make")
  public Image transcribe() {

    ImageResponse response =
        imageModel.call(
            new ImagePrompt(
                "A light cream colored mini golden doodle",
                ImageOptionsBuilder.builder().N(1).height(1024).width(1024).build()));

    Image image = response.getResult().getOutput();
    return image;
  }
}
