package com.socialmedia.socialmediaapi.service.auth;

import com.socialmedia.socialmediaapi.config.JwtUtils;
import com.socialmedia.socialmediaapi.model.Role;
import com.socialmedia.socialmediaapi.model.User;
import com.socialmedia.socialmediaapi.model.auth.AuthenticationRequest;
import com.socialmedia.socialmediaapi.model.auth.AuthenticationResponse;
import com.socialmedia.socialmediaapi.model.auth.RegisterRequest;
import com.socialmedia.socialmediaapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var lowerCaseEmail = request.getEmail().toLowerCase();
        var user = User
            .builder()
            .firstName(request.getFirstname())
            .lastName(request.getLastname())
            .email(lowerCaseEmail)
            .password(passwordEncoder.encode(request.getPassword()))
            .role(Role.USER)
            .build();
        userService.save(user);
        var token = jwtUtils.generateToken(user);
        return AuthenticationResponse
            .builder()
            .token(token)
            .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var lowerCaseEmail = request.getEmail().toLowerCase();
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                lowerCaseEmail,
                request.getPassword()
            )
        );
        var user = userService.findByEmail(lowerCaseEmail).orElseThrow();
        var token = jwtUtils.generateToken(user);
        return AuthenticationResponse
            .builder()
            .token(token)
            .build();
    }
}
