package com.myorg.chatllm.utils;

import java.util.ArrayList;
import java.util.List;

public class TextUtils {

    /**
     * Normalize & tokenize into words (keeps letters, numbers, dash, apostrophe).
     * Returns list of lower-cased tokens.
     */
    public static List<String> tokenizeToWords(String text) {
        List<String> out = new ArrayList<>();
        if (text == null || text.isBlank()) return out;
        String[] parts = text.trim().split("\\s+");
        for (String raw : parts) {
            String w = raw.strip().replaceAll("[^\\p{L}\\p{Nd}\\-']", "").toLowerCase();
            if (!w.isEmpty()) out.add(w);
        }
        return out;
    }
}
