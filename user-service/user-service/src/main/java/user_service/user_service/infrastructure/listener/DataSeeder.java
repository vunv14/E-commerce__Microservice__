package user_service.user_service.infrastructure.listener;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import user_service.user_service.entities.User;
import user_service.user_service.repository.UserRepository;

@Configuration
public class DataSeeder {

    /**
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Argon2PasswordEncoder(16,   // saltLength
                32,   // hashLength
                1,    // parallelism
                65536, // memory (64MB)
                3);
    }

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {

        return args -> {
            if (userRepository.count() == 0) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setEmail("admin@example.com");
                admin.setPassword(passwordEncoder.encode("123456"));
                admin.setFullName("Admin User");
                userRepository.save(admin);
            }
        };
    }
}
