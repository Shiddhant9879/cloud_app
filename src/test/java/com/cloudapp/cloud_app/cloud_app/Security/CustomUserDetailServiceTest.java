package com.cloudapp.cloud_app.cloud_app.Security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.cloudapp.cloud_app.cloud_app.Repository.UserLoginRepository;
import com.cloudapp.cloud_app.cloud_app.model.Users.Role;
import com.cloudapp.cloud_app.cloud_app.model.Users.Users;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailServiceTest {

    @Mock
    private UserLoginRepository userLoginRepository;

    @InjectMocks
    private CustomUserDetailService customUserDetailService;

    @Test
    void loadUserByUsername_returnsCustomerAuthority() {
        Users user = user("customer", Role.CUSTOMER);
        when(userLoginRepository.findByUsername("customer")).thenReturn(Optional.of(user));

        UserDetails result = customUserDetailService.loadUserByUsername("customer");

        assertEquals("customer", result.getUsername());
        assertEquals("encoded-password", result.getPassword());
        assertEquals("ROLE_CUSTOMER", result.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    void loadUserByUsername_returnsTechnicianAuthority() {
        Users user = user("technician", Role.TECHNICIAN);
        when(userLoginRepository.findByUsername("technician")).thenReturn(Optional.of(user));

        UserDetails result = customUserDetailService.loadUserByUsername("technician");

        assertEquals("ROLE_TECHNICIAN", result.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    void loadUserByUsername_throwsWhenUserDoesNotExist() {
        when(userLoginRepository.findByUsername("missing")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailService.loadUserByUsername("missing"));
    }

    private Users user(String username, Role role) {
        Users user = new Users();
        user.setUsername(username);
        user.setPassword("encoded-password");
        user.setRole(role);
        return user;
    }
}
