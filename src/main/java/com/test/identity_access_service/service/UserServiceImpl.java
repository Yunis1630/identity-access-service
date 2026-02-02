package com.test.identity_access_service.service;

import com.test.identity_access_service.model.User;
import com.test.identity_access_service.repository.UserRepository;
import com.test.identity_access_service.security.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        AuthenticatedUser authenticatedUser = new AuthenticatedUser();
        authenticatedUser.setId(user.getId());
        authenticatedUser.setUsername(user.getUsername());
        authenticatedUser.setName(user.getName());
        authenticatedUser.setSurname(user.getSurname());
        authenticatedUser.setPassword(user.getPassword());

        authenticatedUser.setRoles(
                user.getRoles().stream()
                        .map(Enum::name)
                        .collect(Collectors.toList())
        );

        authenticatedUser.setPermissions(
                user.getPermissions().stream()
                        .map(Enum::name)
                        .collect(Collectors.toList())
        );

        return authenticatedUser;
    }
}
