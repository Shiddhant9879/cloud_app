package com.cloudapp.cloud_app.cloud_app.Controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.cloudapp.cloud_app.cloud_app.Dto.LoginRequestDto;
import com.cloudapp.cloud_app.cloud_app.Dto.RegisterRequestDto;
import com.cloudapp.cloud_app.cloud_app.Repository.CustomerRepository;
import com.cloudapp.cloud_app.cloud_app.Repository.TechnicianRequestRepository;
import com.cloudapp.cloud_app.cloud_app.Repository.UserLoginRepository;
import com.cloudapp.cloud_app.cloud_app.Security.JwtUtil;
import com.cloudapp.cloud_app.cloud_app.model.Users.AvailibilityStatus;
import com.cloudapp.cloud_app.cloud_app.model.Users.Customer;
import com.cloudapp.cloud_app.cloud_app.model.Users.Role;
import com.cloudapp.cloud_app.cloud_app.model.Users.Technician;
import com.cloudapp.cloud_app.cloud_app.model.Users.Users;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserLoginRepository userRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private TechnicianRequestRepository technicianRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserController userController;

    private RegisterRequestDto customerRegistration;

    @BeforeEach
    void setUp() {
        customerRegistration = new RegisterRequestDto("customer", "customer@example.com", "password", Role.CUSTOMER);
    }

    @Test
    void register_createsCustomerProfileAndReturnsToken() {
        when(userRepository.existsByUsername("customer")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encoded-password");
        when(jwtUtil.buildToken("customer")).thenReturn("jwt-token");

        ResponseEntity<?> response = userController.register(customerRegistration);

        ArgumentCaptor<Users> userCaptor = ArgumentCaptor.forClass(Users.class);
        ArgumentCaptor<Customer> customerCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(userRepository).save(userCaptor.capture());
        verify(customerRepository).save(customerCaptor.capture());
        assertEquals("customer", userCaptor.getValue().getUsername());
        assertEquals("encoded-password", userCaptor.getValue().getPassword());
        assertEquals(Role.CUSTOMER, userCaptor.getValue().getRole());
        assertEquals(userCaptor.getValue(), customerCaptor.getValue().getUser());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("jwt-token", response.getBody());
    }

    @Test
    void register_createsTechnicianProfileWithDefaultAvailability() {
        RegisterRequestDto technicianRegistration = new RegisterRequestDto(
                "technician", "technician@example.com", "password", Role.TECHNICIAN);
        when(userRepository.existsByUsername("technician")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encoded-password");
        when(jwtUtil.buildToken("technician")).thenReturn("jwt-token");

        userController.register(technicianRegistration);

        ArgumentCaptor<Technician> technicianCaptor = ArgumentCaptor.forClass(Technician.class);
        verify(technicianRepository).save(technicianCaptor.capture());
        assertEquals("technician", technicianCaptor.getValue().getName());
        assertEquals(AvailibilityStatus.NotAvailable, technicianCaptor.getValue().getStatus());
        assertEquals(false, technicianCaptor.getValue().isVerified());
    }

    @Test
    void register_returnsConflictForDuplicateUsername() {
        when(userRepository.existsByUsername("customer")).thenReturn(true);

        ResponseEntity<?> response = userController.register(customerRegistration);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Username already taken", response.getBody());
        verify(userRepository, never()).save(any());
    }

    @Test
    void login_returnsTokenForValidCredentials() {
        LoginRequestDto login = new LoginRequestDto("customer", "password");
        Authentication authentication = org.mockito.Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn("customer");
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtUtil.buildToken("customer")).thenReturn("jwt-token");

        ResponseEntity<?> response = userController.login(login);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("jwt-token", response.getBody());
    }

    @Test
    void login_returnsUnauthorizedForInvalidCredentials() {
        LoginRequestDto login = new LoginRequestDto("customer", "wrong-password");
        when(authenticationManager.authenticate(any())).thenThrow(new RuntimeException("Invalid credentials"));

        ResponseEntity<?> response = userController.login(login);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid username or password", response.getBody());
    }
}
