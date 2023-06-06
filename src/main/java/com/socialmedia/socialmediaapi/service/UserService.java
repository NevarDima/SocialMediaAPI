package com.socialmedia.socialmediaapi.service;

import com.socialmedia.socialmediaapi.model.User;
import com.socialmedia.socialmediaapi.repo.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;

    @Transactional
    public User save(User user){
        return userRepo.save(user);
    }

    public Optional<User> findByEmail(String email){
        return userRepo.findByEmail(email);
    }

}
