package illinoistech.itm.web.system.integration.student_collabration_platform.dto;

import illinoistech.itm.web.system.integration.student_collabration_platform.entity.Project;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record ProjectSummaryDto(
        UUID projectId,
        UUID ownerId,
        String title,
        String description,
        String category,
        Project.DifficultyLevel difficultyLevel,
        String duration,
        LocalDate deadline,
        LocalDate postedDate,
        Project.ProjectStatus status,
        Integer applicationsCount,
        String specificRequirements,
        String deliverables,
        String skills,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime publishedAt,
        String project_objectives,
        String company,
        UUID Industry_profile_id
) {

    public static ProjectSummaryDto fromEntity(Project project,String company,UUID industryProfileId) {
        return new ProjectSummaryDto(
                project.getProjectId(),
                project.getOwner() != null ? project.getOwner().getUserId() : null,
                project.getTitle(),
                project.getDescription(),
                project.getCategory(),
                project.getDifficultyLevel(),
                project.getDuration(),
                project.getDeadline(),
                project.getPostedDate(),
                project.getStatus(),
                project.getApplicationsCount(),
                project.getSpecificRequirements(),
                project.getDeliverables(),
                project.getSkills(),
                project.getCreatedAt(),
                project.getUpdatedAt(),
                project.getPublishedAt(),
                project.getProjectObjective(),
                company,
                industryProfileId

        );
    }
}
