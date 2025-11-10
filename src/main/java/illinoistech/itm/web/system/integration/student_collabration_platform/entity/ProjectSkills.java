package illinoistech.itm.web.system.integration.student_collabration_platform.entity;

import jakarta.persistence.*;
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
@EqualsAndHashCode(of = "id")
@Entity
@Table(
        name = "project_skills",
        uniqueConstraints = {
                // prevent duplicate skill requirement per project
                @UniqueConstraint(name = "uq_project_skills_project_skill", columnNames = {"project_id","skill_id"})
        },
        indexes = {
                @Index(name = "idx_project_skills_project", columnList = "project_id"),
                @Index(name = "idx_project_skills_skill", columnList = "skill_id"),
                @Index(name = "idx_project_skills_required", columnList = "is_required")
        }
)
public class ProjectSkills {

    @Id
    @UuidGenerator
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    // FK -> projects(project_id)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "project_id",
            referencedColumnName = "project_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_project_skills_project")
    )
    private Project project;

    // FK -> skills(skill_id)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "skill_id",
            referencedColumnName = "skill_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_project_skills_skill")
    )
    private Skill skill;

    public enum ProficiencyLevel { BEGINNER, INTERMEDIATE, ADVANCED }

    @Enumerated(EnumType.STRING)
    @Column(name = "proficiency_level", length = 20)
    private ProficiencyLevel proficiencyLevel; // nullable by design

    @Builder.Default
    @Column(name = "is_required", nullable = false)
    private Boolean isRequired = Boolean.TRUE;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, columnDefinition = "timestamp with time zone")
    private OffsetDateTime createdAt;

    @PrePersist
    void prePersist() {
        if (isRequired == null) isRequired = Boolean.TRUE;
    }
}
