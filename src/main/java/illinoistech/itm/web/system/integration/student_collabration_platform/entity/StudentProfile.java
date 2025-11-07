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

    // Manual getters and setters (Lombok annotation processing not working)
    public UUID getProfileId() { return profileId; }
    public void setProfileId(UUID profileId) { this.profileId = profileId; }
    
    public Users getUser() { return user; }
    public void setUser(Users user) { this.user = user; }
    
    public String getUniversity() { return university; }
    public void setUniversity(String university) { this.university = university; }
    
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    
    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }
    
    public AcademicYear getAcademicYear() { return academicYear; }
    public void setAcademicYear(AcademicYear academicYear) { this.academicYear = academicYear; }
    
    public LocalDate getExpectedGraduation() { return expectedGraduation; }
    public void setExpectedGraduation(LocalDate expectedGraduation) { this.expectedGraduation = expectedGraduation; }
    
    public String getResumeUrl() { return resumeUrl; }
    public void setResumeUrl(String resumeUrl) { this.resumeUrl = resumeUrl; }
    
    public String getPortfolioUrl() { return portfolioUrl; }
    public void setPortfolioUrl(String portfolioUrl) { this.portfolioUrl = portfolioUrl; }
    
    public String getGithubUrl() { return githubUrl; }
    public void setGithubUrl(String githubUrl) { this.githubUrl = githubUrl; }
    
    public String getLinkedinUrl() { return linkedinUrl; }
    public void setLinkedinUrl(String linkedinUrl) { this.linkedinUrl = linkedinUrl; }
    
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
}
