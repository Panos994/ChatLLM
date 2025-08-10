package com.myorg.chatllm.dto;

public class GenerateRequest {
    private String prompt;
    private Integer maxTokens = 20; // default

    public String getPrompt() { return prompt; }
    public void setPrompt(String prompt) { this.prompt = prompt; }
    public Integer getMaxTokens() { return maxTokens; }
    public void setMaxTokens(Integer maxTokens) { this.maxTokens = maxTokens; }
}
