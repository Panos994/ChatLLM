package com.myorg.chatllm.entity;


import java.util.HashMap;
import java.util.Map;

public class BigramModel {
    // map: prevToken -> (nextToken -> count)
    private final Map<Integer, Map<Integer, Integer>> counts = new HashMap<>();

    public void increment(int prev, int next) {
        counts.computeIfAbsent(prev, k -> new HashMap<>());
        Map<Integer,Integer> inner = counts.get(prev);
        inner.put(next, inner.getOrDefault(next, 0) + 1);
    }

    public Map<Integer, Integer> getNextCounts(int prev) {
        return counts.getOrDefault(prev, Map.of());
    }

    public boolean hasPrev(int prev) {
        return counts.containsKey(prev);
    }

    // Convert counts to probabilities on the fly in generator
}
