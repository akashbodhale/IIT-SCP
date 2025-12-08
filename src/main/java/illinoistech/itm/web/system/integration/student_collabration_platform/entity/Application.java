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
@ToString(exclude = {"project", "student", "industry"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(
        name = "applications",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_applications_project_student",
                columnNames = {"project_id", "student_profile_id"}
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
    // now optional because sometimes application will be from industry instead of student
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(
            name = "student_profile_id",
            nullable = true,
            foreignKey = @ForeignKey(name = "fk_applications_student")
    )
    private StudentProfile student;

    // FK -> industry_profiles(profile_id)
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(
            name = "industry_profile_id",
            nullable = true,
            foreignKey = @ForeignKey(name = "fk_applications_industry_profile")
    )
    private IndustryProfile industry;

    // Denormalized industry user id (owner of the project at time of application)
    @Column(name = "industry_user_id")
    private UUID industryUserId;

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

    /**
     * Ensure that exactly one of student or industryProfile is set.
     * If student profile is there then industry_profile_id must be null, and vice versa.
     */
    @PrePersist
    @PreUpdate
    private void validateApplicantType() {
        boolean hasStudent = this.student != null;
        boolean hasIndustry = this.industry != null;

        if (hasStudent && hasIndustry) {
            throw new IllegalStateException(
                    "Application cannot have both student and industry profile. Only one is allowed."
            );
        }

        if (!hasStudent && !hasIndustry) {
            throw new IllegalStateException(
                    "Application must have either a student or an industry profile."
            );
        }
    }

    // Manual getters and setters (Lombok annotation processing not working)
    public UUID getApplicationId() { return applicationId; }
    public void setApplicationId(UUID applicationId) { this.applicationId = applicationId; }

    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }

    public StudentProfile getStudent() { return student; }
    public void setStudent(StudentProfile student) { this.student = student; }

    public IndustryProfile getIndustry() { return industry; }
    public void setIndustry(IndustryProfile industry) { this.industry = industry; }

    public UUID getIndustryUserId() { return industryUserId; }
    public void setIndustryUserId(UUID industryUserId) { this.industryUserId = industryUserId; }

    public String getCoverLetterUrl() { return coverLetterUrl; }
    public void setCoverLetterUrl(String coverLetterUrl) { this.coverLetterUrl = coverLetterUrl; }

    public String getPortfolioLink() { return portfolioLink; }
    public void setPortfolioLink(String portfolioLink) { this.portfolioLink = portfolioLink; }

    public ApplicationStatus getStatus() { return status; }
    public void setStatus(ApplicationStatus status) { this.status = status; }

    public String getReviewNotes() { return reviewNotes; }
    public void setReviewNotes(String reviewNotes) { this.reviewNotes = reviewNotes; }

    public OffsetDateTime getAppliedAt() { return appliedAt; }
    public void setAppliedAt(OffsetDateTime appliedAt) { this.appliedAt = appliedAt; }

    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}
