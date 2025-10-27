package illinoistech.itm.web.system.integration.student_collabration_platform.service;

import illinoistech.itm.web.system.integration.student_collabration_platform.dto.ProjectSummaryDto;
import illinoistech.itm.web.system.integration.student_collabration_platform.entity.Project;
import illinoistech.itm.web.system.integration.student_collabration_platform.entity.Project.ProjectStatus;
import illinoistech.itm.web.system.integration.student_collabration_platform.repository.ProjectRepository;
import illinoistech.itm.web.system.integration.student_collabration_platform.repository.ProjectSpecs;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.data.jpa.domain.Specification.allOf;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository repo;

    @Override
    public ProjectSummaryDto getById(UUID projectId) {
        Project p = repo.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found: " + projectId));
        return toDto(p);
    }

    @Override
    public List<ProjectSummaryDto> getAll() {
        return repo.findAll()
                .stream()
                .map(ProjectSummaryDto::fromEntity)
                .toList();
    }

    @Override
    public Page<ProjectSummaryDto> search(ProjectStatus status, String category, UUID ownerId,
                                          LocalDate deadlineFrom, Pageable pageable) {
        var spec = allOf(
                ProjectSpecs.hasStatus(status),
                ProjectSpecs.hasCategoryIgnoreCase(category),
                ProjectSpecs.hasOwner(ownerId),
                ProjectSpecs.deadlineOnOrAfter(deadlineFrom)
        );

        return repo.findAll(spec, pageable).map(this::toDto);
    }

    private ProjectSummaryDto toDto(Project p) {
        UUID ownerId = (p.getOwner() != null) ? p.getOwner().getUserId() : null;
        return new ProjectSummaryDto(
                p.getProjectId(),
                ownerId,
                p.getTitle(),
                p.getDescription(),
                p.getCategory(),
                p.getDifficultyLevel(),
                p.getDuration(),
                p.getDurationMonths(),
                p.getDeadline(),
                p.getStartDate(),
                p.getEndDate(),
                p.getStatus(),
                p.getApplicationsCount(),
                p.getCreatedAt(),
                p.getUpdatedAt(),
                p.getPublishedAt()
        );
    }
}

