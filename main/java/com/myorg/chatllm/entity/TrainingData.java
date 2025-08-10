package com.myorg.chatllm.entity;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "training_data")
public class TrainingData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String sample;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    // getters / setters
    public Long getId() { return id; }
    public String getSample() { return sample; }
    public void setSample(String sample) { this.sample = sample; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
