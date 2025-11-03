package user_service.user_service.security.jwt;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Lấy token từ header Authorization
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // Kiểm tra header có bắt đầu với "Bearer " không
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Lấy token (bỏ "Bearer " prefix)
        jwt = authHeader.substring(7);

        // Trích xuất email từ token
        userEmail = jwtUtils.extractEmail(jwt);

        // Nếu có email và chưa có authentication trong SecurityContext
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Load thông tin user từ database
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // Xác thực token
            if (jwtUtils.validateToken(jwt, userDetails.getUsername())) {

                // Tạo authentication object
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                // Thêm thông tin request vào authentication
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Đặt authentication vào SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Chuyển request đến filter tiếp theo
        filterChain.doFilter(request, response);
    }
}
