package user_service.user_service.entities;

import com.example.commonservice.entities.base.AuditEntity;
import jakarta.persistence.*;

/**
 * Created by vunv on 10/27/2025
 */
@Entity
@Table(name = "refresh_tokens")
public class RefreshToken extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(nullable = false, unique = true)
    private String token;

    public boolean revoked;

}
