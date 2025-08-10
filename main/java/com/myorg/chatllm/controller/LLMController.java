package com.myorg.chatllm.controller;


import com.myorg.chatllm.dto.GenerateRequest;
import com.myorg.chatllm.dto.GenerateResponse;
import com.myorg.chatllm.dto.TrainRequest;
import com.myorg.chatllm.entity.TrainingData;
import com.myorg.chatllm.repository.TrainingDataRepository;
import com.myorg.chatllm.service.LLMService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/llm")
public class LLMController {

    private final LLMService llmService;
    private final TrainingDataRepository trainingDataRepository; // optional, can be null if not using

    public LLMController(LLMService llmService, TrainingDataRepository trainingDataRepository) {
        this.llmService = llmService;
        this.trainingDataRepository = trainingDataRepository;
    }

    @PostMapping("/train")
    public ResponseEntity<?> train(@RequestBody TrainRequest request) {
        List<String> samples = request.getSamples();
        if (samples == null || samples.isEmpty()) {
            return ResponseEntity.badRequest().body("No samples provided");
        }
        // optionally save samples to DB
        if (trainingDataRepository != null) {
            for (String s : samples) {
                TrainingData td = new TrainingData();
                td.setSample(s);
                trainingDataRepository.save(td);
            }
        }
        llmService.train(samples);
        return ResponseEntity.ok("Trained on " + samples.size() + " samples. Vocab size: " + llmService.vocabSize());
    }

    @PostMapping("/generate")
    public ResponseEntity<GenerateResponse> generate(@RequestBody GenerateRequest req) {
        String out = llmService.generate(req.getPrompt() == null ? "" : req.getPrompt(), req.getMaxTokens() == null ? 20 : req.getMaxTokens());
        return ResponseEntity.ok(new GenerateResponse(out));
    }
}
