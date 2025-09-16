package com.myorg.chatllm.entity;

import java.io.Serializable;
import java.util.*;

public class NGramModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int n;
    private final Map<List<Integer>, Map<Integer, Integer>> counts = new HashMap<>();
    private int totalCount = 0;

    public NGramModel(int n) {
        if (n < 2) throw new IllegalArgumentException("n must be >= 2");
        this.n = n;
    }

    public int getN() { return n; }

    /**
     * Προσθέτει ένα παρατηρηθέν n-gram: context -> nextToken
     */
    public void add(List<Integer> context, int nextToken) {
        // φτιάχνουμε αντίγραφο για να μην αλλάζει το key εξωτερικά
        List<Integer> ctxCopy = new ArrayList<>(context);
        counts.computeIfAbsent(ctxCopy, k -> new HashMap<>())
                .merge(nextToken, 1, Integer::sum);
        totalCount++;
    }

    /**
     * Επιστρέφει map (token -> count) για ένα context
     */
    public Map<Integer, Integer> getCandidates(List<Integer> context) {
        return counts.getOrDefault(context, Collections.emptyMap());
    }

    public int totalCount() { return totalCount; }

    public void clear() {
        counts.clear();
        totalCount = 0;
    }

    /**
     * Vocabulary size: αριθμός μοναδικών tokens σε contexts και nextTokens
     */
    public int vocabularySize() {
        Set<Integer> vocab = new HashSet<>();
        for (List<Integer> ctx : counts.keySet()) {
            vocab.addAll(ctx);
        }
        for (Map<Integer, Integer> inner : counts.values()) {
            vocab.addAll(inner.keySet());
        }
        return vocab.size();
    }

    public Set<Integer> getVocabulary() {
        Set<Integer> vocab = new HashSet<>();
        for (List<Integer> ctx : counts.keySet()) vocab.addAll(ctx);
        for (Map<Integer, Integer> inner : counts.values()) vocab.addAll(inner.keySet());
        return vocab;
    }

    public int contextCount() {
        return counts.size();
    }

}