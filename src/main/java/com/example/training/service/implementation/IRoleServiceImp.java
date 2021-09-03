package com.example.training.service.implementation;

import com.example.training.entity.ERole;
import com.example.training.entity.Role;
import com.example.training.repository.interfaces.IRoleRepository;
import com.example.training.service.interfaces.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class IRoleServiceImp implements IRoleService {
    @Autowired
    private IRoleRepository iRoleRepository;

    @Override
    public Iterable<Role> findAll() {
        return iRoleRepository.findAll();
    }

    @Override
    public Optional<Role> findById(Long id) {
        return iRoleRepository.findById(id);
    }

    @Override
    public Role save(Role role) {
        return iRoleRepository.save(role);
    }

    @Override
    public void delete(Long id) {
        iRoleRepository.deleteById(id);
    }

    @Override
    public Optional<Role> findByName(ERole name) {
        return iRoleRepository.findByName(name);
    }
}
