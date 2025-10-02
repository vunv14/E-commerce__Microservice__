package user_service.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import user_service.user_service.entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,String> {

    Optional<User> findByEmail(String email);

}
