package com.myorg.chatllm.entity;


import java.util.*;


public class Tokenizer {
    private final Map<String, Integer> wordToIndex = new HashMap<>();
    private final Map<Integer, String> indexToWord = new HashMap<>();
    private int indexCounter = 0;

    // simple normalization: lowercase & remove extra punctuation except sentence-enders
    private String normalize(String token) {
        return token.strip().replaceAll("[^a-zA-Z0-9Α-Ωα-ωάέήίόύώϊϋΰΆΈΉΊΌΎΏ΄΄-]", "");
    }

    public List<Integer> tokenize(String text) {
        List<Integer> tokens = new ArrayList<>();
        if (text == null || text.isBlank()) return tokens;
        String[] parts = text.toLowerCase().split("\\s+");
        for (String raw : parts) {
            String word = normalize(raw);
            if (word.isEmpty()) continue;
            Integer idx = wordToIndex.get(word);
            if (idx == null) {
                idx = indexCounter++;
                wordToIndex.put(word, idx);
                indexToWord.put(idx, word);
            }
            tokens.add(idx);
        }
        return tokens;
    }

    public List<List<Integer>> tokenizeBatch(List<String> texts) {
        List<List<Integer>> batch = new ArrayList<>();
        for (String t : texts) batch.add(tokenize(t));
        return batch;
    }

    public String detokenize(List<Integer> tokens) {
        StringBuilder sb = new StringBuilder();
        for (Integer t : tokens) {
            String w = indexToWord.get(t);
            if (w == null) continue;
            if (sb.length() > 0) sb.append(' ');
            sb.append(w);
        }
        return sb.toString();
    }

    public Optional<String> getWord(int index) {
        return Optional.ofNullable(indexToWord.get(index));
    }

    public int vocabularySize() {
        return wordToIndex.size();
    }

    public Map<String,Integer> getWordToIndex() { return Collections.unmodifiableMap(wordToIndex); }
    public Map<Integer,String> getIndexToWord() { return Collections.unmodifiableMap(indexToWord); }
}
