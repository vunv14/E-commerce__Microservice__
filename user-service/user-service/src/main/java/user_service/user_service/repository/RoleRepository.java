package user_service.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import user_service.user_service.entities.Role;

public interface RoleRepository extends JpaRepository<Role,String> {

}
