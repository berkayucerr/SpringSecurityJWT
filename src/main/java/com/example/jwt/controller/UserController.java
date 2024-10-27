package com.example.jwt.controller;

import com.example.jwt.request.AuthRequest;
import com.example.jwt.request.CreateUserRequest;
import com.example.jwt.response.CreateUserResponse;
import com.example.jwt.response.LoginResponse;
import com.example.jwt.service.AuthenticationService;
import com.example.jwt.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/auth")
@Slf4j
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    public UserController(UserService userService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<CreateUserResponse> addNewUser(@Valid @RequestBody CreateUserRequest createUserRequest) {
        log.info("Add New User: %s".formatted(createUserRequest.getUsername()));
        return new ResponseEntity<>(userService.createUser(createUserRequest), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> generateToken(@RequestBody AuthRequest authRequest) {
        log.info("Login, username: %s".formatted(authRequest.getUsername()));
        return ResponseEntity.ok(authenticationService.login(authRequest));
    }

    @GetMapping("/user")
    public ResponseEntity<UserDetails> getUser(Principal request) {
        String username = request.getName();
        UserDetails userDetails = userService.loadUserByUsername(username);
        return ResponseEntity.ok(userDetails);
    }

    @GetMapping("/admin")
    public ResponseEntity<String> getAdmin() {
        return ResponseEntity.ok("getAdmin endpoint");
    }
}
