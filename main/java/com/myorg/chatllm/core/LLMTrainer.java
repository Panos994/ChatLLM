package com.myorg.chatllm.core;


import com.myorg.chatllm.entity.BigramModel;
import com.myorg.chatllm.entity.Tokenizer;
import com.myorg.chatllm.entity.NGramModel;
import com.myorg.chatllm.utils.TextUtils;


import java.util.List;
/**
 * Trainer that fills NGramModel counts.
 * For each training text we add observations for all context lengths 1..(n-1) (backoff support).
 *
 * Example (n=3): for sequence [w0,w1,w2,w3]
 * - add context "w0" -> w1   (bigram)
 * - add context "w0 w1" -> w2   (trigram)
 * - add context "w1" -> w2
 * - add context "w1 w2" -> w3
 */
public class LLMTrainer {

    private final NGramModel model;
    private final Tokenizer tokenizer;

    public LLMTrainer(NGramModel model, Tokenizer tokenizer) {
        this.model = model;
        this.tokenizer = tokenizer;
    }

    public void train(List<String> texts) {
        if (texts == null || texts.isEmpty()) return;
        int maxContext = Math.max(1, model.getN() - 1);

        for (String txt : texts) {
            // tokenize to words
            List<String> words = TextUtils.tokenizeToWords(txt);
            // convert to indexes via tokenizer
            List<Integer> tokens = tokenizer.tokenize(String.join(" ", words));
            if (tokens.size() < 2) continue;

            for (int start = 0; start < tokens.size(); start++) {
                for (int k = 1; k <= maxContext; k++) {
                    int ctxEnd = start + k - 1;
                    int nextIndex = start + k;
                    if (nextIndex >= tokens.size()) break;

                    List<Integer> contextTokens = tokens.subList(start, ctxEnd + 1);
                    int nextToken = tokens.get(nextIndex);
                    model.add(contextTokens, nextToken); // αντί για string, δουλεύει με indexes
                }
            }
        }
    }
}
