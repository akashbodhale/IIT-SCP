package illinoistech.itm.web.system.integration.student_collabration_platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "projectId")
@ToString(exclude = "owner") // avoid lazy-loading recursion
@Entity
@Table(
        name = "projects",
        indexes = {
                @Index(name = "idx_projects_owner",    columnList = "industry_user_id"),
                @Index(name = "idx_projects_category", columnList = "category"),
                @Index(name = "idx_projects_status",   columnList = "status"),
                @Index(name = "idx_projects_deadline", columnList = "deadline")
        }
)
public class Project {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "project_id", columnDefinition = "uuid", nullable = false, updatable = false)
    private UUID projectId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "industry_user_id",
            referencedColumnName = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_projects_owner")
    )
    private Users owner;

    @NotBlank
    @Size(max = 255)
    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @NotBlank
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @NotBlank
    @Size(max = 100)
    @Column(name = "category", nullable = false, length = 100)
    private String category;

    // ===== Difficulty =====
    public enum DifficultyLevel { BEGINNER, INTERMEDIATE, ADVANCED }

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty_level", nullable = false, length = 20)
    private DifficultyLevel difficultyLevel;

    // ===== Duration =====
    @NotBlank
    @Size(max = 50)
    @Column(name = "duration", nullable = false, length = 50) // e.g., "3 months"
    private String duration;

    @Min(0)
    @Column(name = "duration_months")
    private Integer durationMonths;

    @NotNull
    @Column(name = "deadline", nullable = false)
    private LocalDate deadline;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;


    public enum ProjectStatus { DRAFT, OPEN, IN_PROGRESS, COMPLETED, CANCELLED, CLOSED }

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ProjectStatus status = ProjectStatus.DRAFT;


    @Builder.Default
    @Min(0)
    @Column(name = "applications_count", nullable = false)
    private Integer applicationsCount = 0;


    @Column(name = "requirements", columnDefinition = "TEXT")
    private String requirements;

    @Column(name = "deliverables", columnDefinition = "TEXT")
    private String deliverables;

    // ===== Audit =====
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    // ===== Defaults / Guards =====
    @PrePersist
    void prePersist() {
        if (status == null) status = ProjectStatus.DRAFT;
        if (applicationsCount == null) applicationsCount = 0;
    }
}
