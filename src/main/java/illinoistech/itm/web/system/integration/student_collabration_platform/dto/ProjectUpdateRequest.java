package illinoistech.itm.web.system.integration.student_collabration_platform.dto;
import illinoistech.itm.web.system.integration.student_collabration_platform.entity.Project;

import java.time.LocalDate;

public record ProjectUpdateRequest(
        String title,
        String description,
        String category,
        Project.DifficultyLevel difficultyLevel,
        String duration,
        Integer durationMonths,
        LocalDate deadline,
        LocalDate startDate,
        LocalDate endDate,
        Project.ProjectStatus status,
        String requirments
) {}