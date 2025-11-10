package illinoistech.itm.web.system.integration.student_collabration_platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(
        name = "industry_profile",
        uniqueConstraints = {
                // one profile per user
                @UniqueConstraint(name = "uq_industry_profile_user", columnNames = "user_id")
                // If you truly need company_name unique per user, use:
                // @UniqueConstraint(name = "uq_industry_profile_user_company", columnNames = {"user_id","company_name"})
        },
        indexes = {
                @Index(name = "idx_industry_profile_company_name", columnList = "company_name"),
                @Index(name = "idx_industry_profile_verification_status", columnList = "verification_status")
        }
)
public class IndustryProfile {

    // ====== PK ======
    @Id
    @UuidGenerator
    @EqualsAndHashCode.Include
    @Column(name = "profile_id", nullable = false, updatable = false)
    private UUID profileId;

    // ====== Owner (1:1 with users) ======
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_industry_profile_user")
    )
    private Users user;

    // ====== Company Info ======
    @NotBlank
    @Size(max = 255)
    @Column(name = "company_name", nullable = false, length = 255)
    private String companyName;

    @NotBlank
    @Size(max = 255)
    @Column(name = "position", nullable = false, length = 255)
    private String position;

    public enum companySize { SMALL, MEDIUM, LARGE, STARTUP, ENTERPRISE }

    @Enumerated(EnumType.STRING)
    @Column(name = "company_size", nullable = false, length = 32)
    private companySize companySize;

    // long form text -> use TEXT
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Size(max = 255)
    @Column(name = "headquarters_location", length = 255)
    private String headquartersLocation;

    @Size(max = 255)
    @Column(name = "linkedin_url", length = 255)
    private String linkedinUrl;

    public enum CompanyStatus { PENDING, VERIFIED, REJECTED }

    @Enumerated(EnumType.STRING)
    @Column(name = "verification_status", nullable = false, length = 32)
    private CompanyStatus verificationStatus = CompanyStatus.PENDING;

    @Size(max = 255)
    @Column(name = "verification_document", length = 255)
    private String verificationDocument;

    // ====== Audit ======
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, columnDefinition = "timestamp with time zone")
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false, columnDefinition = "timestamp with time zone")
    private OffsetDateTime updatedAt;

    // Optional: guard defaults
    @PrePersist
    void prePersist() {
        if (verificationStatus == null) verificationStatus = CompanyStatus.PENDING;
    }
}
