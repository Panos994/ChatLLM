package com.myorg.chatllm.service;


import com.myorg.chatllm.core.LLMGenerator;
import com.myorg.chatllm.core.LLMTrainer;
import com.myorg.chatllm.entity.BigramModel;
import com.myorg.chatllm.entity.NGramModel;
import com.myorg.chatllm.entity.Tokenizer;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.Map;
@Service
public class LLMService implements Serializable {

    private static final long serialVersionUID = 1L;

    private NGramModel model;
    private LLMTrainer trainer;
    private LLMGenerator generator;
    private Tokenizer tokenizer;

    // hyperparams
    private final double smoothingAlpha = 1.0; // Laplace
    private final double defaultTemperature = 1.0;
    private final int defaultN = 5; // 5-gram


    public LLMService() {
        initComponents();
    }

    // Initialize model, tokenizer, trainer, generator safely
    private void initComponents() {
        if (this.model == null) this.model = new NGramModel(defaultN);
        if (this.tokenizer == null) this.tokenizer = new Tokenizer();
        if (this.trainer == null) this.trainer = new LLMTrainer(model, tokenizer);
        if (this.generator == null) this.generator = new LLMGenerator(tokenizer, model, smoothingAlpha);
    }

    public void train(List<String> samples) {
        initComponents(); // ensure everything is initialized
        if (samples != null && !samples.isEmpty()) {
            trainer.train(samples);
        }
    }

    public void train(String text) {
        if (text != null && !text.isBlank()) {
            train(List.of(text));
        }
    }

    // -----------------------
    // Generation
    // -----------------------
    public String generate(String prompt, int maxTokens, Double temperature) {
        initComponents(); // ensure trainer & generator are ready
        double temp = temperature == null ? defaultTemperature : Math.max(0.01, temperature);
        return generator.generate(
                prompt == null ? "" : prompt,
                Math.max(0, maxTokens),
                temp
        );
    }

    // -----------------------
    // Info / Debug
    // -----------------------
    public int vocabSize() {
        return model != null ? model.vocabularySize() : 0;
    }

    public boolean isTrained() {
        return model != null && model.totalCount() > 0;
    }

    public void clear() {
        if (model != null) model.clear();
    }

    // -----------------------
    // Serialization
    // -----------------------
    public void saveModel(String path) throws IOException {
        initComponents();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
            oos.writeObject(this.model);
            oos.writeObject(this.tokenizer);
        }
    }

    public void loadModel(String path) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))) {
            this.model = (NGramModel) ois.readObject();
            this.tokenizer = (Tokenizer) ois.readObject();
        }
        // Recreate trainer/generator safely
        this.trainer = new LLMTrainer(model, tokenizer);
        this.generator = new LLMGenerator(tokenizer, model, smoothingAlpha);
    }

    // -----------------------
    // Controller helper
    // -----------------------
    public Map<String, Object> info() {
        return Map.of(
                "vocab", vocabSize(),
                "trained", isTrained(),
                "contexts", model != null ? model.contextCount() : 0
        );
    }
}



