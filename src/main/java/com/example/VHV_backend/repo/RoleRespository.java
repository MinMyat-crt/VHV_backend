package com.example.VHV_backend.repo;

import com.example.VHV_backend.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRespository extends JpaRepository<Role,Integer>{
    Optional<Role> findByName(String user);
}
