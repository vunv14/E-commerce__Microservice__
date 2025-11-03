package user_service.user_service.security.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import user_service.user_service.infrastructure.constants.ApiPath;
import user_service.user_service.security.dto.request.AuthRequest;
import user_service.user_service.security.dto.response.AuthenticationResponse;
import user_service.user_service.service.AuthenticationService;

@RestController
@RequestMapping(ApiPath.LOGIN)
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping()
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthRequest req) {
        return ResponseEntity.ok(authenticationService.authenticate(req));
    }

}
