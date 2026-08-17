package com.cloudapp.cloud_app.cloud_app.Service;

import org.springframework.stereotype.Service;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.cloudapp.cloud_app.cloud_app.Repository.UserLoginRepository;
import com.cloudapp.cloud_app.cloud_app.model.Users.Users;

import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserLoginRepository userLoginRepository;

    public CustomUserDetailService(UserLoginRepository userLoginRepository) {
        this.userLoginRepository = userLoginRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Users> userOptional = userLoginRepository.findByUsername(username);

        Users user = userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // role ko GrantedAuthority mein convert kar, ROLE_ prefix ke saath
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().name());

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(List.of(authority))
                .build();
    }
}