package illinoistech.itm.web.system.integration.student_collabration_platform.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import illinoistech.itm.web.system.integration.student_collabration_platform.entity.Application;
import illinoistech.itm.web.system.integration.student_collabration_platform.entity.Project;
import illinoistech.itm.web.system.integration.student_collabration_platform.repository.ProjectRepository;

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
        "company_name"
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
       @JsonProperty("company_name") String companyName
) {
    public static ApplicationSummaryDto fromEntity(Application a) {
        Project p = a.getProject();
        var Title =p.getTitle();
        var CompanyName = a.getIndustry().getCompanyName();

        UUID projId = (p != null) ? p.getProjectId() : null;

        UUID studId = (a.getStudent() != null)
                ? a.getStudent().getProfileId()   // <-- adjust if your PK field name differs
                : null;

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
                Title,
                CompanyName
        );
    }
}