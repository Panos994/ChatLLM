package com.myorg.chatllm.controller;


import com.myorg.chatllm.dto.GenerateRequest;
import com.myorg.chatllm.dto.GenerateResponse;
import com.myorg.chatllm.dto.TrainRequest;
import com.myorg.chatllm.entity.TrainingData;
import com.myorg.chatllm.repository.TrainingDataRepository;
import com.myorg.chatllm.service.LLMService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/llm")
public class LLMController {

    private final LLMService llmService;
    //private final TrainingDataRepository trainingDataRepository; // optional, can be null if not using

    public LLMController(LLMService llmService) {
        this.llmService = llmService;

    }
    @PostMapping("/train")
    public ResponseEntity<?> train(@RequestBody List<String> samples) {
        if (samples == null || samples.isEmpty()) return ResponseEntity.badRequest().body(Map.of("error", "No samples"));
        llmService.train(samples);
        return ResponseEntity.ok(Map.of("status", "trained", "vocab", llmService.vocabSize()));
    }

    @PostMapping("/generate")
    public ResponseEntity<?> generate(@RequestBody Map<String, Object> body) {
        String prompt = (String) body.getOrDefault("prompt", "");
        int maxTokens = ((Number) body.getOrDefault("maxTokens", 20)).intValue();
        Double temp = body.containsKey("temperature") ? ((Number) body.get("temperature")).doubleValue() : null;
        String out = llmService.generate(prompt, maxTokens, temp);
        return ResponseEntity.ok(Map.of("generated", out));
    }

    @GetMapping("/info")
    public ResponseEntity<?> info() {
        return ResponseEntity.ok(llmService.info());
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody Map<String,String> body) {
        String path = body.getOrDefault("path", "mymodel.ser");
        try {
            llmService.saveModel(path);
            return ResponseEntity.ok(Map.of("saved", path));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/load")
    public ResponseEntity<?> load(@RequestBody Map<String,String> body) {
        String path = body.getOrDefault("path", "mymodel.ser");
        try {
            llmService.loadModel(path);
            return ResponseEntity.ok(Map.of("loaded", path, "vocab", llmService.vocabSize()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
}
