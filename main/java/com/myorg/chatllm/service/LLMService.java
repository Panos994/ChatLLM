package com.myorg.chatllm.service;


import com.myorg.chatllm.core.LLMGenerator;
import com.myorg.chatllm.core.LLMTrainer;
import com.myorg.chatllm.entity.BigramModel;
import com.myorg.chatllm.entity.Tokenizer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LLMService {

    private final Tokenizer tokenizer;
    private final BigramModel model;
    private final LLMTrainer trainer;
    private final LLMGenerator generator;

    public LLMService() {
        // initialization: in-memory model
        this.tokenizer = new Tokenizer();
        this.model = new BigramModel();
        this.trainer = new LLMTrainer(tokenizer, model);
        this.generator = new LLMGenerator(tokenizer, model);
    }

    /**
     * Train the model with list of text samples.
     */
    public void train(List<String> texts) {
        trainer.train(texts);
    }

    /**
     * Quick way to train with a single text string.
     */
    public void train(String text) {
        trainer.train(List.of(text));
    }

    /**
     * Generate text given a prompt and max tokens.
     */
    public String generate(String prompt, int maxTokens) {
        return generator.generate(prompt, maxTokens);
    }

    // for debugging / UI
    public int vocabSize() {
        return tokenizer.vocabularySize();
    }

    public Tokenizer getTokenizer() { return tokenizer; }
    public BigramModel getModel() { return model; }
}

