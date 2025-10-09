package user_service.user_service.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import user_service.user_service.security.service.CustomAuthenticationProvider;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String[] WHITE_LIST_URL = {"/api/v1/auth/**", "/api/v1/**",};

    private final CustomAuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(req -> req.requestMatchers(WHITE_LIST_URL).permitAll() // cho phép tất cả mọi người truy cập các đường dẫn trong WHITE_LIST_URL
                .requestMatchers("/api/v1/admin/**").hasRole() // chỉ cho phép người dùng có vai trò ADMIN truy cập các đường dẫn admin
                .anyRequest().authenticated()    // các đường dẫn còn lại ko được phép truy cập
        );

        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // không sử dụng session để lưu trữ thông tin người dùng
        http.authenticationProvider(authenticationProvider); // cấu hình AuthenticationProvider

        return http.build();
    }


    /**
     *
     * @return
     */

    /**
     * Xử lý xác thực người dùng dựa trên UserDetailsService và PasswordEncoder được cấu hình trong ứng dụng.
     * Xử lý xác thực người dùng (authentication) dựa trên thông tin đăng nhập mà client gửi lên (như username/password, token, v.v).
     * Nó kiểm tra xem thông tin đăng nhập có hợp lệ không và nếu hợp lệ, nó sẽ tạo ra một đối tượng Authentication đại diện cho người dùng đã xác thực.
     *
     * @return
     */
//    @Bean
//    public AuthenticationProvider authenticationProvider() {
//
//    }

}
