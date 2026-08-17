package com.cloudapp.cloud_app.cloud_app.Dto;

import com.cloudapp.cloud_app.cloud_app.model.Users.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDto {
    private String username;
    private String email;
    private String password;
    private Role role;
}