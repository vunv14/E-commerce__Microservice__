package user_service.user_service.repository.criteria;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;
import user_service.user_service.entities.Role;
import user_service.user_service.entities.User;
import user_service.user_service.entities.UserRole;

import java.util.List;

@Repository
public class CRUserRepository {

    private EntityManager entityManager;

    public List<Role> getListRole(String userId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Role> query = cb.createQuery(Role.class);
        Root<Role> role = query.from(Role.class);

        Join<User, UserRole> userJoin = role.join("userRoles");
        Join<UserRole, User> userRoleJoin = userJoin.join("userRoles");



        return null;
    }

}
