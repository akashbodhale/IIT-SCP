package illinoistech.itm.web.system.integration.student_collabration_platform.service;

import illinoistech.itm.web.system.integration.student_collabration_platform.dto.ProjectSummaryDto;
import illinoistech.itm.web.system.integration.student_collabration_platform.entity.Project;
import illinoistech.itm.web.system.integration.student_collabration_platform.entity.Project.ProjectStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProjectService {

    ProjectSummaryDto getById(UUID projectId);

    Page<ProjectSummaryDto> search(
            ProjectStatus status,
            String category,
            UUID ownerId,
            LocalDate deadlineFrom,
            Pageable pageable
    );

    List<ProjectSummaryDto> getAll();
    ProjectSummaryDto create(ProjectSummaryDto dto);
}
