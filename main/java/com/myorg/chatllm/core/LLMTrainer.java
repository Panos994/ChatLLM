package com.myorg.chatllm.core;


import com.myorg.chatllm.entity.BigramModel;
import com.myorg.chatllm.entity.Tokenizer;

import java.util.List;

public class LLMTrainer {

    private final Tokenizer tokenizer;
    private final BigramModel model;

    public LLMTrainer(Tokenizer tokenizer, BigramModel model) {
        this.tokenizer = tokenizer;
        this.model = model;
    }

    /**
     * Train from a batch of texts (sentences/documents). We treat each text as sequence of tokens.
     */
    public void train(List<String> texts) {
        var batch = tokenizer.tokenizeBatch(texts);
        for (List<Integer> tokens : batch) {
            if (tokens.isEmpty()) continue;
            // optionally add a special START token; here we'll use the first token pairs only
            for (int i = 0; i < tokens.size() - 1; i++) {
                int prev = tokens.get(i);
                int next = tokens.get(i + 1);
                model.increment(prev, next);
            }
        }
    }
}
