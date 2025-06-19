package com.petproject.Securehub.service;

import com.petproject.Securehub.dto.AuthRequest;
import com.petproject.Securehub.dto.AuthResponse;
import com.petproject.Securehub.dto.RegisterUserRequest;
import com.petproject.Securehub.entity.RefreshToken;
import com.petproject.Securehub.entity.User;
import com.petproject.Securehub.repository.RefreshTokenRepository;
import com.petproject.Securehub.repository.UserRepository;
import com.petproject.Securehub.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    public AuthResponse authenticate(AuthRequest request) {
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String accessToken = JwtUtil.generateToken(user.getUsername(), user.getRoles());
        RefreshToken refreshToken = createRefreshToken(user);
        return new AuthResponse(accessToken, refreshToken.getToken());
    }

    public void register(RegisterUserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already taken");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setRoles(request.getRoles());

        userRepository.save(user);
    }

    public AuthResponse refreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token).orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            throw new RuntimeException("Refresh token expired. Login again.");
        }

        User user = refreshToken.getUser();
        String newToken = JwtUtil.generateToken(user.getUsername(), user.getRoles());
        return new AuthResponse(newToken, token);
    }


    private RefreshToken createRefreshToken(User user) {
        RefreshToken token = new RefreshToken();
        token.setUser(user);
        token.setToken(JwtUtil.generateRefreshToken());
        token.setExpiryDate(Instant.now().plusSeconds(604800));
        return refreshTokenRepository.save(token);
    }
}
