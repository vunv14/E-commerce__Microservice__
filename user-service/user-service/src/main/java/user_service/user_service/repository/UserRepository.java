package user_service.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import user_service.user_service.entities.Role;
import user_service.user_service.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,String> {

    Optional<User> findByEmail(String email);

    @Query("""
    select r from User u 
    join UserRole ur on u = ur.user
    join Role r on ur.role = r
    where u.id = :userId
""")
    List<Role> getListRoles(String userId);

}
