package com.example.training.repository.interfaces;

import com.example.training.entity.ERole;
import com.example.training.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
