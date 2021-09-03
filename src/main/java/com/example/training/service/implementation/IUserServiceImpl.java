package com.example.training.service.implementation;


import com.example.training.entity.User;
import com.example.training.repository.interfaces.IUserRepository;
import com.example.training.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class IUserServiceImpl implements IUserService {

    @Autowired
    private IUserRepository iUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public IUserServiceImpl() {
        super();
    }

    @Override
    public Iterable<User> findAll(){
        return iUserRepository.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return iUserRepository.findById(id);
    }

    @Override
    public User save(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return iUserRepository.save(user);
    }

    @Override
    public void delete(Long id){
        iUserRepository.deleteById(id);
    }
}
