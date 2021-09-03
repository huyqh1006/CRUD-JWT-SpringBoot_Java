package com.example.training.controller;

import com.example.training.entity.ERole;
import com.example.training.entity.Role;
import com.example.training.entity.User;
import com.example.training.payload.request.JwtAuthenticationSigninRequest;
import com.example.training.payload.request.JwtAuthenticationSignupRequest;
import com.example.training.payload.response.JwtAuthenticationResponse;
import com.example.training.config.security.jwt.JwtTokenProvider;
import com.example.training.service.implementation.UserDetailsImpl;
import com.example.training.repository.interfaces.IRoleRepository;
import com.example.training.repository.interfaces.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private IUserRepository iUserRepository;

    @Autowired
    private IRoleRepository iRoleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Validated @RequestBody JwtAuthenticationSigninRequest jwtAuthenticationSigninRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        jwtAuthenticationSigninRequest.getUsername(),
                        jwtAuthenticationSigninRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.generateJwtToken((UserDetailsImpl) authentication.getPrincipal());
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Validated @RequestBody JwtAuthenticationSignupRequest signupRequest) {
        if (iUserRepository.existsByUsername(signupRequest.getUsername())) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }
        if (iUserRepository.existsByPassword(signupRequest.getPassword())) {
            return ResponseEntity.badRequest().body("Error: Password is already in user");
        }
        User user = new User(
                signupRequest.getUsername(),
                passwordEncoder.encode(signupRequest.getPassword()),
                signupRequest.getEmail());

        Set<String> nameRoles = signupRequest.getRole();
        Set<Role> roles = new HashSet<>();
        if (nameRoles == null) {
            Role userRole = iRoleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            nameRoles.forEach(role -> {
                switch (role) {
                    case "admin" -> {
                        Role adminRole = iRoleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                    }
                    case "editor" -> {
                        Role editorRole = iRoleRepository.findByName(ERole.ROLE_EDITOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(editorRole);
                    }
                    default -> {
                        Role userRole = iRoleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                    }
                }
            });
        }
        user.setRoles(roles);
        iUserRepository.save(user);
        return ResponseEntity.ok("ok");
    }
}