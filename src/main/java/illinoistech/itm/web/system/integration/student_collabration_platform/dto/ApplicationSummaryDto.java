package illinoistech.itm.web.system.integration.student_collabration_platform.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import illinoistech.itm.web.system.integration.student_collabration_platform.entity.Application;
import illinoistech.itm.web.system.integration.student_collabration_platform.entity.Project;
import illinoistech.itm.web.system.integration.student_collabration_platform.entity.StudentProfile;
import illinoistech.itm.web.system.integration.student_collabration_platform.entity.Users;

import java.time.OffsetDateTime;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "application_id",
        "applied_at",
        "cover_letter_url",
        "created_at",
        "portfolio_link",
        "review_notes",
        "status",
        "updated_at",
        "project_id",
        "student_id",
        "title",
        "company_name",
        "student_name",
        "student_email",
        "student_major"
})
public record ApplicationSummaryDto(
        @JsonProperty("application_id") UUID applicationId,
        @JsonProperty("applied_at") OffsetDateTime appliedAt,
        @JsonProperty("cover_letter_url") String coverLetterUrl,
        @JsonProperty("created_at") OffsetDateTime createdAt,
        @JsonProperty("portfolio_link") String portfolioLink,
        @JsonProperty("review_notes") String reviewNotes,
        @JsonProperty("status") Application.ApplicationStatus status,
        @JsonProperty("updated_at") OffsetDateTime updatedAt,
        @JsonProperty("project_id") UUID projectId,
        @JsonProperty("student_id") UUID studentId,
        @JsonProperty("title") String title,
        @JsonProperty("company_name") String companyName,
        @JsonProperty("student_name") String studentName,
        @JsonProperty("student_email") String studentEmail,
        @JsonProperty("student_major") String studentMajor
) {
    public static ApplicationSummaryDto fromEntity(Application a) {

        // ---- Project info ----
        Project p = a.getProject();
        UUID projId = (p != null) ? p.getProjectId() : null;
        String title = (p != null) ? p.getTitle() : null;

        // ---- Student info ----
        StudentProfile student = a.getStudent();
        UUID studId = (student != null) ? student.getProfileId() : null;

        Users user = (student != null) ? student.getUser() : null;

        String studentName = null;
        String studentEmail = null;
        String studentMajor = null;

        if (user != null) {
            String firstName = user.getFirstName();
            String lastName = user.getLastName();

            if (firstName != null || lastName != null) {
                StringBuilder sb = new StringBuilder();
                if (firstName != null) sb.append(firstName);
                if (lastName != null) {
                    if (!sb.isEmpty()) sb.append(" ");
                    sb.append(lastName);
                }
                studentName = sb.toString();
            }

            studentEmail = user.getEmail();
        }

        if (student != null) {
            studentMajor = student.getMajor();
        }

        // ---- Company info ----
        String companyName = null;
        if (a.getIndustry() != null) {
            companyName = a.getIndustry().getCompanyName();
        }

        return new ApplicationSummaryDto(
                a.getApplicationId(),
                a.getAppliedAt(),
                a.getCoverLetterUrl(),
                a.getCreatedAt(),
                a.getPortfolioLink(),
                a.getReviewNotes(),
                a.getStatus(),
                a.getUpdatedAt(),
                projId,
                studId,
                title,
                companyName,
                studentName,
                studentEmail,
                studentMajor
        );
    }
}
