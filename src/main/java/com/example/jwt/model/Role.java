package com.example.jwt.model;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashSet;
import java.util.Set;


@Getter
public enum Role implements GrantedAuthority {
    ROLE_USER("USER"),
    ROLE_ADMIN("ADMIN");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    public static Set<Role> getUserRole() {
        Set<Role> roles = new HashSet<>();
        roles.add(ROLE_USER);
        return roles;
    }

    @Override
    public String getAuthority() {
        return name();
    }
}
