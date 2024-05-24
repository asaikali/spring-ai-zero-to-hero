package com.example.audio_01;

import org.springframework.ai.openai.OpenAiAudioTranscriptionClient;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.ai.openai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.openai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/audio/01")
public class TranscribeController {

  private final OpenAiAudioTranscriptionClient transcriptionClient;

  @Value("classpath:/data/The_Astronomer_Vermeer.ogg")
  private Resource audioResource;

  public TranscribeController(OpenAiAudioTranscriptionClient transcriptionClient) {
    this.transcriptionClient = transcriptionClient;
  }

  @GetMapping("text")
  public String transcribe() {

    var transcriptionOptions =
        OpenAiAudioTranscriptionOptions.builder()
            .withResponseFormat(OpenAiAudioApi.TranscriptResponseFormat.TEXT)
            .withTemperature(0f)
            .build();

    AudioTranscriptionPrompt transcriptionRequest =
        new AudioTranscriptionPrompt(audioResource, transcriptionOptions);
    AudioTranscriptionResponse response = this.transcriptionClient.call(transcriptionRequest);

    return response.getResult().getOutput();
  }
}
