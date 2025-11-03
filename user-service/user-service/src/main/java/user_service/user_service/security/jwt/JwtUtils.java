
package user_service.user_service.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {

    // Khóa bí mật để mã hóa token
    @Value("${jwt.secret}")
    private String secret;

    // Thời gian hết hạn của access token (milliseconds)
    @Value("${jwt.expiration}")
    private Long expiration;

    // Thời gian hết hạn của refresh token (milliseconds)
    @Value("${jwt.refresh.expiration}")
    private Long refreshExpiration;

    // Tạo khóa ký từ secret key
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // Tạo access token từ email người dùng
    public String generateToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, email, expiration);
    }

    // Tạo refresh token từ email người dùng (thời gian sống lâu hơn)
    public String generateRefreshToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, email, refreshExpiration);
    }

    // Tạo token với claims, subject (email) và thời gian hết hạn
    private String createToken(Map<String, Object> claims, String subject, Long expirationTime) {
        return Jwts.builder()
                .setClaims(claims) // Thêm các thông tin bổ sung
                .setSubject(subject) // Đặt email làm subject
                .setIssuedAt(new Date(System.currentTimeMillis())) // Thời gian tạo token
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime)) // Thời gian hết hạn
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Ký token bằng HS256
                .compact(); // Tạo chuỗi JWT
    }

    // Lấy email từ token
    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    // Lấy thời gian hết hạn từ token
    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    // Giải mã và lấy tất cả claims từ token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // Đặt khóa để xác thực
                .build()
                .parseClaimsJws(token) // Phân tích token
                .getBody(); // Lấy nội dung claims
    }

    // Kiểm tra token đã hết hạn chưa
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Xác thực token với email và kiểm tra hết hạn
    public boolean validateToken(String token, String email) {
        final String tokenEmail = extractEmail(token);
        return (tokenEmail.equals(email) && !isTokenExpired(token));
    }
}

