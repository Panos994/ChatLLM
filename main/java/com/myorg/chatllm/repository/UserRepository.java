package com.myorg.chatllm.repository;

import com.myorg.chatllm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);  //Optional <T> A container object which may or may not contain a non-null value
}
