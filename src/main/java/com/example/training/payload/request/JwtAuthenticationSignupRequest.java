package com.example.training.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtAuthenticationSignupRequest {
    private String username;
    private String password;
    private String email;
    private Set<String> Role;
}
