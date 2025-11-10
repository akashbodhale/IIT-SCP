package illinoistech.itm.web.system.integration.student_collabration_platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "skillId")
@Entity
@Table(
        name = "skills",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_skills_name", columnNames = "name")
        },
        indexes = {
                @Index(name = "idx_skills_category", columnList = "category")
        }
)
public class Skill {

    @Id
    @UuidGenerator
    @Column(name = "skill_id", nullable = false, updatable = false)
    private UUID skillId;

    // VARCHAR(100) UNIQUE NOT NULL
    @NotBlank
    @Size(max = 100)
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    // VARCHAR(50) NOT NULL
    @NotBlank
    @Size(max = 50)
    @Column(name = "category", nullable = false, length = 50)
    private String category;

    // TEXT NULL
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    // BOOLEAN DEFAULT TRUE
    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = Boolean.TRUE;

    // INTEGER DEFAULT 0
    @Builder.Default
    @Column(name = "usage_count", nullable = false)
    private Integer usageCount = 0;

    // TIMESTAMP DEFAULT NOW()
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, columnDefinition = "timestamp with time zone")
    private OffsetDateTime createdAt;

    @PrePersist
    void prePersist() {
        if (isActive == null) isActive = Boolean.TRUE;
        if (usageCount == null) usageCount = 0;
    }
}
