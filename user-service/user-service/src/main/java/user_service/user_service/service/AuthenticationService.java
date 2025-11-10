package user_service.user_service.service;

import user_service.user_service.security.dto.request.AuthRequest;
import user_service.user_service.security.dto.response.AuthenticationResponse;

/**
 * Created by vunv on 11/10/2025
 */
public interface AuthenticationService {

    AuthenticationResponse authenticate(AuthRequest request);

    AuthenticationResponse refreshToken(String refreshToken);

    String sendEmailOtp(String email);
}
