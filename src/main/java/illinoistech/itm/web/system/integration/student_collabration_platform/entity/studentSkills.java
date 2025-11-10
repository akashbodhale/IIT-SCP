package illinoistech.itm.web.system.integration.student_collabration_platform.entity;

import jakarta.persistence.*;
        import jakarta.validation.constraints.*;
        import lombok.*;
        import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
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
        name = "student_skills",
        uniqueConstraints = {
                // A student should not have the same skill twice
                @UniqueConstraint(name = "uq_student_skills_student_skill", columnNames = {"student_id", "skill_id"})
        },
        indexes = {
                @Index(name = "idx_student_skills_student", columnList = "student_id"),
                @Index(name = "idx_student_skills_skill", columnList = "skill_id"),
                @Index(name = "idx_student_skills_verified", columnList = "is_verified"),
                @Index(name = "idx_student_skills_proficiency", columnList = "proficiency_level")
        }
)
public class studentSkills
{

    @Id
    @UuidGenerator
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "student_id",
            referencedColumnName = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_student_skills_student")
    )
    private Users student;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "skill_id",
            referencedColumnName = "skill_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_student_skills_skill")
    )
    private Skill skill;

    public enum ProficiencyLevel { BEGINNER, INTERMEDIATE, ADVANCED, EXPERT }

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "proficiency_level", nullable = false, length = 20)
    private ProficiencyLevel proficiencyLevel;


    @Digits(integer = 3, fraction = 1)
    @Column(name = "years_of_experience", precision = 3, scale = 1)
    private BigDecimal yearsOfExperience;

    @Builder.Default
    @Column(name = "is_verified", nullable = false)
    private Boolean isVerified = Boolean.FALSE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "verified_by",
            referencedColumnName = "user_id",
            foreignKey = @ForeignKey(name = "fk_student_skills_verified_by")
    )
    private Users verifiedBy;

    @Column(name = "verified_at")
    private OffsetDateTime verifiedAt;

    @Builder.Default
    @Min(0)
    @Column(name = "endorsements_count", nullable = false)
    private Integer endorsementsCount = 0;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, columnDefinition = "timestamp with time zone")
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false, columnDefinition = "timestamp with time zone")
    private OffsetDateTime updatedAt;

    @PrePersist
    void prePersist() {
        if (isVerified == null) isVerified = Boolean.FALSE;
        if (endorsementsCount == null) endorsementsCount = 0;
    }
}

