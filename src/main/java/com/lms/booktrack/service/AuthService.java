package com.lms.booktrack.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lms.booktrack.model.User;
import com.lms.booktrack.repository.AuthRepo;

@Service
public class AuthService {
    // Authentication related methods would go here

    @Autowired
    private AuthRepo authRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerUser(User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        user.setRegistrationDate();
        authRepo.save(user);
    }
    
}