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

//    // Manual getters and setters (Lombok not working properly)
//    public UUID getUserId() { return userId; }
//    public void setUserId(UUID userId) { this.userId = userId; }
//
//    public String getEmail() { return email; }
//    public void setEmail(String email) { this.email = email; }
//
//    public String getPasswordHash() { return passwordHash; }
//    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
//
//    public String getUserType() { return userType; }
//    public void setUserType(String userType) { this.userType = userType; }
//
//    public String getFirstName() { return firstName; }
//    public void setFirstName(String firstName) { this.firstName = firstName; }
//
//    public String getLastName() { return lastName; }
//    public void setLastName(String lastName) { this.lastName = lastName; }
//
//    public String getPhone() { return phone; }
//    public void setPhone(String phone) { this.phone = phone; }
//
//    public boolean isActive() { return isActive; }
//    public void setActive(boolean active) { isActive = active; }
//
//    public LocalDateTime getCreatedAt() { return createdAt; }
//    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
//
//    public LocalDateTime getUpdatedAt() { return updatedAt; }
//    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
//
//    public UUID getId() { return id; }
//    public void setId(UUID id) { this.id = id; }
//
//    public boolean isEnabled() { return enabled; }
//    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}
