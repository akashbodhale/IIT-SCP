package illinoistech.itm.web.system.integration.student_collabration_platform.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"project", "student"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(
        name = "applications",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_applications_project_student",
                columnNames = {"project_id", "student_id"}
        )
)
public class Application {

    @Id
    @UuidGenerator
    @EqualsAndHashCode.Include
    @Column(name = "application_id", nullable = false, updatable = false)
    private UUID applicationId;

    // FK -> projects(project_id)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "project_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_applications_project")
    )
    private Project project;

    // FK -> student_profiles(profile_id)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "student_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_applications_student")
    )
    private StudentProfile student;

    @Size(max = 500)
    @Pattern(regexp = "^https?://.*", message = "coverLetterUrl must start with http:// or https://")
    @Column(name = "cover_letter_url", length = 500)
    private String coverLetterUrl;

    @Size(max = 500)
    @Pattern(regexp = "^https?://.*", message = "portfolioLink must start with http:// or https://")
    @Column(name = "portfolio_link", length = 500)
    private String portfolioLink;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private ApplicationStatus status = ApplicationStatus.PENDING;

    @Lob
    @Column(name = "review_notes")
    private String reviewNotes;

    // applied_at should reflect creation time
    @CreationTimestamp
    @Column(name = "applied_at", nullable = false, columnDefinition = "timestamp with time zone")
    private OffsetDateTime appliedAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false, columnDefinition = "timestamp with time zone")
    private OffsetDateTime updatedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, columnDefinition = "timestamp with time zone")
    private OffsetDateTime createdAt;



    public enum ApplicationStatus {
        PENDING,
        REVIEWED,
        ACCEPTED,
        REJECTED,
        WITHDRAWN
    }
}

