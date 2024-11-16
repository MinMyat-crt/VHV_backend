package com.example.VHV_backend.repo;

import com.example.VHV_backend.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.support.SimpleTriggerContext;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity,Integer> {
    boolean existsByUsername(String username);
    Optional<UserEntity> findByUsername(String username);
}
