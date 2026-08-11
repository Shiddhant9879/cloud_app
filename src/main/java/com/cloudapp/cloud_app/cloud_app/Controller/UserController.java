package com.cloudapp.cloud_app.cloud_app.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cloudapp.cloud_app.cloud_app.DTO.LoginRequestDto;

@RestController
@RequestMapping("/auth")
public class UserController {

    @PostMapping("/register")
    public ResponseEntity<?> register() {

        return ResponseEntity.ok("Registration endpoint");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequest) {

        return ResponseEntity.ok("Login endpoint");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {

        return ResponseEntity.ok("Logout endpoint");
    }
}
