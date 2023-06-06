package com.socialmedia.socialmediaapi.service.auth;

import com.socialmedia.socialmediaapi.config.JwtUtils;
import com.socialmedia.socialmediaapi.model.Role;
import com.socialmedia.socialmediaapi.model.User;
import com.socialmedia.socialmediaapi.model.auth.AuthenticationRequest;
import com.socialmedia.socialmediaapi.model.auth.AuthenticationResponse;
import com.socialmedia.socialmediaapi.model.auth.RegisterRequest;
import com.socialmedia.socialmediaapi.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User
            .builder()
            .firstName(request.getFirstname())
            .lastName(request.getLastname())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(Role.USER)
            .build();
        userRepo.save(user);
        var token = jwtUtils.generateToken(user);
        return AuthenticationResponse
            .builder()
            .token(token)
            .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
            )
        );
        var user = userRepo.findByEmail(request.getEmail()).orElseThrow();
        var token = jwtUtils.generateToken(user);
        return AuthenticationResponse
            .builder()
            .token(token)
            .build();
    }
}
