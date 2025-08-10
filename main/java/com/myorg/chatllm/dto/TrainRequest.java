package com.myorg.chatllm.dto;

import java.util.List;

public class TrainRequest {
    private List<String> samples;

    public List<String> getSamples() { return samples; }
    public void setSamples(List<String> samples) { this.samples = samples; }
}
