package illinoistech.itm.web.system.integration.student_collabration_platform.dto;

import illinoistech.itm.web.system.integration.student_collabration_platform.entity.Project;
import illinoistech.itm.web.system.integration.student_collabration_platform.entity.Project.ProjectStatus;
import illinoistech.itm.web.system.integration.student_collabration_platform.entity.Project.DifficultyLevel;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record ProjectSummaryDto(
        UUID projectId,
        UUID ownerId,
        String title,
        String description,
        String category,
        DifficultyLevel difficultyLevel,
        String duration,
        Integer durationMonths,
        LocalDate deadline,
        LocalDate startDate,
        LocalDate endDate,
        ProjectStatus status,
        Integer applicationsCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime publishedAt
) {
    public static ProjectSummaryDto fromEntity(Project project) {
        return new ProjectSummaryDto(
                project.getProjectId(),
                project.getOwner() != null ? project.getOwner().getUserId() : null,
                project.getTitle(),
                project.getDescription(),
                project.getCategory(),
                project.getDifficultyLevel(),
                project.getDuration(),
                project.getDurationMonths(),
                project.getDeadline(),
                project.getStartDate(),
                project.getEndDate(),
                project.getStatus(),
                project.getApplicationsCount(),
                project.getCreatedAt(),
                project.getUpdatedAt(),
                project.getPublishedAt()
        );
    }
}

