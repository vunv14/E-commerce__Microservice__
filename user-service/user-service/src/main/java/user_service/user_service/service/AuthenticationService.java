package user_service.user_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import user_service.user_service.entities.User;
import user_service.user_service.repository.UserRepository;
import user_service.user_service.security.dto.request.AuthRequest;
import user_service.user_service.security.dto.response.AuthenticationResponse;
import user_service.user_service.security.jwt.JwtUtils;

/**
 * Created by vunv on 10/27/2025
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    public AuthenticationResponse authenticate(AuthRequest request) {
        log.info("Authenticating user: {}", request.getUserName());

        // Xác thực thông tin đăng nhập
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUserName(),
                        request.getPassword()
                )
        );

        // Lấy thông tin user
        User user = userRepository.findByEmail(request.getUserName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Tạo access token và refresh token
        String accessToken = jwtUtils.generateToken(user.getEmail());
        String refreshToken = jwtUtils.generateRefreshToken(user.getEmail());
        log.info("User authenticated successfully: {}", request.getUserName());

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * Làm mới token sử dụng refresh token
     */
    public AuthenticationResponse refreshToken(String refreshToken) {
        log.info("Refreshing token");

        // Validate refresh token
        String username = jwtUtils.extractEmail(refreshToken);

        if (username == null) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Kiểm tra tính hợp lệ của refresh token
        if (!jwtUtils.validateToken(refreshToken, user.getEmail())) {
            throw new IllegalArgumentException("Invalid or expired refresh token");
        }

        // Tạo access token mới
        String newAccessToken = jwtUtils.generateToken(user.getEmail());

        log.info("Token refreshed successfully for user: {}", username);

        return AuthenticationResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .build();
    }

}

