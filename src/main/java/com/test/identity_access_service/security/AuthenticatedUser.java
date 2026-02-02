package com.test.identity_access_service.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class AuthenticatedUser implements UserDetails {

    @Getter
    @Setter
    private Long id;

    @Setter
    private String username;

    @Setter
    private String password;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String surname;

    @Getter
    @Setter
    private List<String> roles;

    @Getter
    @Setter
    private List<String> permissions;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Map roles to ROLE_ prefix
        List<GrantedAuthority> roleAuthorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());

        // Map permissions directly
        List<GrantedAuthority> permissionAuthorities = permissions.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        roleAuthorities.addAll(permissionAuthorities);
        return roleAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
