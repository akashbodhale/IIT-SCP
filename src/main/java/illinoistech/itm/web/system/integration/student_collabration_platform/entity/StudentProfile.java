package illinoistech.itm.web.system.integration.student_collabration_platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "user") // avoid recursive toString()
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(
        name = "student_profiles",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_student_profiles_user", columnNames = "user_id"),
                @UniqueConstraint(name = "uq_student_profiles_univ_sid", columnNames = {"university", "student_id"})
        }
)
public class StudentProfile {

    @Id
    @UuidGenerator
    @EqualsAndHashCode.Include
    @Column(name = "profile_id", nullable = false, updatable = false)
    private UUID profileId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_student_profiles_user"))
    private Users user;

    @Column(name = "university", nullable = false, length = 255)
    private String university;

    @Column(name = "student_id", nullable = false, length = 50)
    private String studentId;

    @Column(name = "major", nullable = false, length = 100)
    private String major;

    @Enumerated(EnumType.STRING)
    @Column(name = "academic_year", nullable = false, length = 20)
    private AcademicYear academicYear;

    @Column(name = "expected_graduation")
    private LocalDate expectedGraduation;

    @Size(max = 500)
    @Pattern(regexp = "^https?://.*", message = "resumeUrl must start with http:// or https://")
    @Column(name = "resume_url", length = 500)
    private String resumeUrl;

    @Size(max = 500)
    @Pattern(regexp = "^https?://.*", message = "portfolioUrl must start with http:// or https://")
    @Column(name = "portfolio_url", length = 500)
    private String portfolioUrl;

    @Size(max = 500)
    @Pattern(regexp = "^https?://.*", message = "githubUrl must start with http:// or https://")
    @Column(name = "github_url", length = 500)
    private String githubUrl;

    @Size(max = 500)
    @Pattern(regexp = "^https?://.*", message = "linkedinUrl must start with http:// or https://")
    @Column(name = "linkedin_url", length = 500)
    private String linkedinUrl;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, columnDefinition = "timestamp with time zone")
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false, columnDefinition = "timestamp with time zone")
    private OffsetDateTime updatedAt;

    public enum AcademicYear
    {
        FRESHMAN, SOPHOMORE, JUNIOR, SENIOR, GRADUATE
    }
}
