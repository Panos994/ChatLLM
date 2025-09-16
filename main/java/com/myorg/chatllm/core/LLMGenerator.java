package com.myorg.chatllm.core;


import com.myorg.chatllm.entity.BigramModel;
import com.myorg.chatllm.entity.NGramModel;
import com.myorg.chatllm.entity.Tokenizer;
import com.myorg.chatllm.utils.TextUtils;

import java.util.*;

/**
 * Generator: uses NGramModel. Tries longest context first (n-1), then backoff to shorter contexts.
 * Applies Laplace (add-alpha) smoothing and temperature sampling.
 */
public class LLMGenerator {

    private final NGramModel model;
    private final Tokenizer tokenizer;
    private final Random rng = new Random();
    private final double alpha;

    public LLMGenerator(Tokenizer tokenizer, NGramModel model, double alpha) {
        this.tokenizer = tokenizer;
        this.model = model;
        this.alpha = Math.max(0.0, alpha);
    }

    public String generate(String prompt, int maxTokens, double temperature) {
        List<String> words = TextUtils.tokenizeToWords(prompt);
        List<Integer> context = tokenizer.tokenize(String.join(" ", words));

        List<Integer> history = new ArrayList<>(context);
        int maxContext = Math.max(1, model.getN() - 1);

        // fallback: pick random token if history empty
        if (history.isEmpty()) {
            var vocab = new ArrayList<>(tokenizer.getIndexToWord().keySet());
            if (vocab.isEmpty()) return "";
            history.add(vocab.get(rng.nextInt(vocab.size())));
        }

        for (int t = 0; t < maxTokens; t++) {
            Integer next = sampleNext(history, maxContext);
            if (next == null) break;
            history.add(next);
        }

        // convert back to words
        List<Integer> generated = history.subList(context.size(), history.size());
        return tokenizer.detokenize(generated);
    }

    private Integer sampleNext(List<Integer> history, int maxContext) {
        for (int ctxLen = Math.min(maxContext, history.size()); ctxLen >= 1; ctxLen--) {
            List<Integer> context = history.subList(history.size() - ctxLen, history.size());
            Map<Integer, Integer> counts = model.getCandidates(context); // returns map token -> count
            if (counts != null && !counts.isEmpty()) return sampleFromCounts(counts, alpha);
        }

        var vocab = new ArrayList<>(tokenizer.getIndexToWord().keySet());
        if (vocab.isEmpty()) return null;
        return vocab.get(rng.nextInt(vocab.size()));
    }

    private Integer sampleFromCounts(Map<Integer, Integer> counts, double alpha) {
        int V = Math.max(1, tokenizer.vocabularySize());
        double totalRaw = counts.values().stream().mapToDouble(Integer::doubleValue).sum();
        double denom = totalRaw + alpha * V;

        List<Integer> tokens = new ArrayList<>(counts.keySet());
        double[] probs = new double[tokens.size()];
        for (int i = 0; i < tokens.size(); i++) {
            double count = counts.get(tokens.get(i));
            probs[i] = (count + alpha) / denom;
        }

        // temperature softmax
        double[] adjusted = new double[probs.length];
        double maxLog = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < probs.length; i++) {
            double logp = Math.log(Math.max(probs[i], 1e-12));
            double adj = logp / Math.max(1e-8, 1.0); // εδώ μπορείς να βάλεις το temperature
            adjusted[i] = adj;
            if (adj > maxLog) maxLog = adj;
        }

        double sumExp = 0.0;
        for (int i = 0; i < adjusted.length; i++) {
            adjusted[i] = Math.exp(adjusted[i] - maxLog);
            sumExp += adjusted[i];
        }

        double r = rng.nextDouble() * sumExp;
        double acc = 0.0;
        for (int i = 0; i < adjusted.length; i++) {
            acc += adjusted[i];
            if (r <= acc) return tokens.get(i);
        }
        return tokens.get(tokens.size() - 1);
    }

}
