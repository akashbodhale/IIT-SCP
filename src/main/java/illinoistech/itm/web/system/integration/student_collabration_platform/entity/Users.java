package illinoistech.itm.web.system.integration.student_collabration_platform.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class Users {

    // PK -> user_id (uuid default gen_random_uuid())
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "user_id", columnDefinition = "uuid", nullable = false, updatable = false)
    private UUID userId;

    @Column(name = "email", length = 254, nullable = false)
    private String email;

    // stored bcrypt (72 chars). Column name is exactly "password_ha" per your table
    @Column(name = "password_ha", length = 72, nullable = false)
    private String passwordHash;

    @Column(name = "user_type", length = 255, nullable = false)
    private String userType;

    @Column(name = "first_name", length = 60, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 60, nullable = false)
    private String lastName;

    // nullable
    @Column(name = "phone", length = 20)
    private String phone;

    // defaults to true in DB; keep nullable=false to reflect constraint
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    // timestamps (DB defaults now()); we still set them from app as a safety net
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // extra UUID column present in your table (not the PK)
    @Column(name = "id", columnDefinition = "uuid", nullable = false)
    private UUID id;

    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;

    @PrePersist
    void onCreate() {
        // normalize + defaulting (DB already has defaults; this complements them)
        if (email != null) email = email.trim().toLowerCase();
        LocalDateTime now = LocalDateTime.now();
        if (createdAt == null) createdAt = now;
        if (updatedAt == null) updatedAt = now;
        // honor DB defaults but ensure non-null on Java side
        if (id == null) id = UUID.randomUUID();
    }

    @PreUpdate
    void onUpdate() {
        if (email != null) email = email.trim().toLowerCase();
        updatedAt = LocalDateTime.now();
    }
}
