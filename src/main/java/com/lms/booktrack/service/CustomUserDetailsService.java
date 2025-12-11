package com.lms.booktrack.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lms.booktrack.model.User;
import com.lms.booktrack.repository.AuthRepo;

@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {


    @Autowired
    private AuthRepo authRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = authRepo.findByEmail(username);

        if (user.isPresent()) {
            User u = user.get();

            return org.springframework.security.core.userdetails.User.builder()
                    .username(u.getEmail())
                    .password(u.getPassword())
                    .roles(u.getRole())
                    .build();

        } else {
            throw new UsernameNotFoundException("User not found with email: " + username);
        }
        
    }
    
}
