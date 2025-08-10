package com.myorg.chatllm.repository;

import com.myorg.chatllm.entity.TrainingData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingDataRepository extends JpaRepository<TrainingData, Long> {
}
