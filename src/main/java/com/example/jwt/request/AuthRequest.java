package com.example.jwt.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthRequest {
    @NotBlank(message = "Please fill in the username field")
    private String username;
    @NotBlank(message = "Please fill in the password field")
    private String password;
}
