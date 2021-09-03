package com.example.training.service.interfaces;

import com.example.training.entity.ERole;
import com.example.training.entity.Role;

import java.util.Optional;

public interface IRoleService extends IGeneralService<Role>{
    Optional<Role> findByName(ERole name);
}
