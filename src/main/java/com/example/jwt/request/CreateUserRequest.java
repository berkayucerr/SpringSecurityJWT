package com.example.jwt.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {
    @Size(min = 3, max = 25)
    @NotBlank(message = "Please enter the name field")
    private String name;

    @Size(min = 3, max = 15)
    @NotBlank(message = "Please enter the username field")
    private String username;

    @Size(min = 3, max = 40)
    @NotBlank(message = "Please enter the password field")
    private String password;

    @NotBlank(message = "Please enter the email field")
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
            flags = Pattern.Flag.CASE_INSENSITIVE)
    private String email;
}
