package com.example.jwt.service;

import com.example.jwt.model.Role;
import com.example.jwt.model.Token;
import com.example.jwt.model.User;
import com.example.jwt.repository.TokenRepository;
import com.example.jwt.repository.UserRepository;
import com.example.jwt.request.CreateUserRequest;
import com.example.jwt.response.CreateUserResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;


    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, JwtService jwtService, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtService = jwtService;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> user = userRepository.findByUsername(username);
        return user.orElseThrow(EntityNotFoundException::new);
    }

    public CreateUserResponse createUser(CreateUserRequest request) {

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return CreateUserResponse.builder().message("This username already using by another user").build();
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return CreateUserResponse.builder().message("This email already using by another user").build();
        }
        User newUser = User.builder()
                .name(request.getName())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(bCryptPasswordEncoder.encode(request.getPassword()))
                .authorities(Role.getUserRole())
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .isEnabled(true)
                .accountNonLocked(true)
                .build();

        var registeredUser = userRepository.save(newUser);
        var token = jwtService.generateToken(registeredUser.getUsername());
        registeredUser.setToken(token);
        saveToken(registeredUser, token);
        return CreateUserResponse.builder()
                .user(registeredUser)
                .message("User created successfully").build();
    }

    public void saveToken(User user, String stringToken) {
        Token token = Token.builder()
                .user_token(stringToken)
                .user(user)
                .revoked(false)
                .expired(false)
                .build();
        tokenRepository.save(token);
    }

    public Optional<User> getByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
