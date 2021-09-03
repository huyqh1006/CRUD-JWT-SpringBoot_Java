package com.example.training.service.implementation;

import com.example.training.entity.User;
import com.example.training.repository.interfaces.IUserRepository;
import com.example.training.service.implementation.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    @Autowired
    private IUserRepository iUserRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = iUserRepository.findByUsername(username)
                .orElseThrow(() ->  new UsernameNotFoundException("User NOT found with username:" + username));
        return UserDetailsImpl.build(user);
    }
}
