package com.test.identity_access_service.service;

import com.test.identity_access_service.dto.LoginDTO;
import com.test.identity_access_service.dto.RefreshTokenDTO;
import com.test.identity_access_service.model.RefreshToken;
import com.test.identity_access_service.model.User;
import com.test.identity_access_service.payload.LoginPayload;
import com.test.identity_access_service.payload.RefreshTokenPayload;
import com.test.identity_access_service.payload.RegisterPayload;
import com.test.identity_access_service.repository.RefreshTokenRepository;
import com.test.identity_access_service.repository.UserRepository;
import com.test.identity_access_service.security.AuthenticatedUser;
import com.test.identity_access_service.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;

    // REGISTER NEW USER
    public void register(RegisterPayload payload) {
        // Check if username already exists
        userRepository.findByUsername(payload.getUsername()).ifPresent(u -> {
            throw new RuntimeException("Username already in use");
        });

        User user = new User();
        user.setUsername(payload.getUsername());
        user.setName(payload.getName());
        user.setSurname(payload.getSurname());
        user.setPassword(passwordEncoder.encode(payload.getPassword()));

        // Set roles and permissions from payload
        user.setRoles(payload.getRole());
        user.setPermissions(payload.getPermission());

        userRepository.save(user);
    }

    // LOGIN USER
    public LoginDTO login(LoginPayload payload) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(payload.getUsername(), payload.getPassword())
        );

        AuthenticatedUser userDetails = (AuthenticatedUser) authentication.getPrincipal();

        String accessToken = jwtUtil.generateToken(userDetails);

        String refreshTokenValue = UUID.randomUUID().toString().replace("-", "").substring(0, 30);

        User user = userRepository.findByUsername(payload.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(refreshTokenValue);
        refreshToken.setUser(user);
        refreshToken.setCreateDateTime(LocalDateTime.now());
        refreshToken.setExpires(LocalDateTime.now().plusHours(1));

        refreshTokenRepository.save(refreshToken);

        return new LoginDTO(
                accessToken,
                refreshTokenValue,
                user.getUsername(),
                user.getName(),
                user.getSurname(),
                user.getRoles(),
                user.getPermissions()
        );
    }

    // REFRESH ACCESS TOKEN
    public RefreshTokenDTO refreshToken(RefreshTokenPayload payload) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(payload.getRefreshToken())
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        if (refreshToken.getExpires().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Refresh token expired");
        }

        User user = refreshToken.getUser();

        // Recreate AuthenticatedUser with roles & permissions
        AuthenticatedUser userDetails = new AuthenticatedUser();
        userDetails.setId(user.getId());
        userDetails.setUsername(user.getUsername());
        userDetails.setName(user.getName());
        userDetails.setSurname(user.getSurname());
        userDetails.setRoles(user.getRoles().stream().map(Enum::name).toList());
        userDetails.setPermissions(user.getPermissions().stream().map(Enum::name).toList());

        String newAccessToken = jwtUtil.generateToken(userDetails);

        return new RefreshTokenDTO(newAccessToken, refreshToken.getToken());
    }

}
