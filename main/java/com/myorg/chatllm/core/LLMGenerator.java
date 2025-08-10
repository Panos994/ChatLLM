package com.myorg.chatllm.core;


import com.myorg.chatllm.entity.BigramModel;
import com.myorg.chatllm.entity.Tokenizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class LLMGenerator {

    private final Tokenizer tokenizer;
    private final BigramModel model;
    private final Random random = new Random();

    public LLMGenerator(Tokenizer tokenizer, BigramModel model) {
        this.tokenizer = tokenizer;
        this.model = model;
    }

    /**
     * Generate text given a prompt. We tokenize the prompt, take last token as seed,
     * then sample next tokens using bigram counts for up to maxTokens.
     *
     * If the seed token has no outgoing counts, we pick a random token from vocabulary.
     */
    public String generate(String prompt, int maxTokens) {
        List<Integer> seedTokens = tokenizer.tokenize(prompt);
        int current;
        if (seedTokens.isEmpty()) {
            // pick a random token as start
            if (tokenizer.vocabularySize() == 0) return "";
            current = random.nextInt(tokenizer.vocabularySize());
        } else {
            current = seedTokens.get(seedTokens.size() - 1);
        }

        List<Integer> output = new ArrayList<>();
        // include seed tokens optionally (here: we produce only generated tokens)
        for (int i = 0; i < maxTokens; i++) {
            int next = sampleNext(current);
            output.add(next);
            current = next;
        }

        return tokenizer.detokenize(output);
    }

    private int sampleNext(int current) {
        Map<Integer,Integer> nextCounts = model.getNextCounts(current);
        if (nextCounts.isEmpty()) {
            // fallback: random token from vocab
            if (tokenizer.vocabularySize() == 0) return 0;
            return random.nextInt(tokenizer.vocabularySize());
        }
        // build cumulative distribution
        int total = nextCounts.values().stream().mapToInt(Integer::intValue).sum();
        int r = random.nextInt(total);
        int acc = 0;
        for (Map.Entry<Integer,Integer> e : nextCounts.entrySet()) {
            acc += e.getValue();
            if (r < acc) return e.getKey();
        }
        // fallback
        return nextCounts.keySet().iterator().next();
    }
}
