package com.cloudapp.cloud_app.cloud_app.Dto;

public class RegisterRequestDto {
    private String Username;
    private String email;
    private String password;

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        this.Username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}