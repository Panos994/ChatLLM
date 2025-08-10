package com.myorg.chatllm.dto;

public class GenerateResponse {
    private String generated;

    public GenerateResponse() {}
    public GenerateResponse(String generated) { this.generated = generated; }

    public String getGenerated() { return generated; }
    public void setGenerated(String generated) { this.generated = generated; }
}