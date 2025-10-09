package user_service.user_service.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import user_service.user_service.service.CustomUserDetails;
import user_service.user_service.service.UserDetailService;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    /**
     *
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        if (authentication == null) {
            log.error("Authentication object is null");
            throw new BadCredentialsException("Authentication object cannot be null");
        }

        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        log.info("Authenticating user: {}", username);

        if (username == null || username.trim().isEmpty() || password == null) {
            throw new BadCredentialsException("Username or password cannot be null");
        }

        try {
            CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);
            log.debug("User found: {}", username);

            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                log.warn("Invalid password for user: {}", username);
                throw new BadCredentialsException("Invalid credentials");
            }

            return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        } catch (UsernameNotFoundException e) {
            log.warn("User not found: {}", username);
            throw new BadCredentialsException("Invalid credentials");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
