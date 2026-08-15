package com.cloudapp.cloud_app.cloud_app.Service;

import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.cloudapp.cloud_app.cloud_app.Repository.UserLoginRepository;
import com.cloudapp.cloud_app.cloud_app.model.Users.Users;

import java.util.Collections;
import java.util.Optional;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserLoginRepository userLoginRepository;

    public CustomUserDetailService(UserLoginRepository userLoginRepository) {
        this.userLoginRepository = userLoginRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Step a: repository se user dhoondo
        Optional<Users> userOptional = userLoginRepository.findByUsername(username);

        // Step b: agar nahi mila -> exception throw karo
        Users user = userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // Step c: apne User entity ko Spring Security ke UserDetails mein convert karo
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(Collections.emptyList())
                .build();
    }
}