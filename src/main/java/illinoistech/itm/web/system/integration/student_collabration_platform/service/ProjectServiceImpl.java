package illinoistech.itm.web.system.integration.student_collabration_platform.service;

import illinoistech.itm.web.system.integration.student_collabration_platform.dto.ProjectSummaryDto;
import illinoistech.itm.web.system.integration.student_collabration_platform.entity.Project;
import illinoistech.itm.web.system.integration.student_collabration_platform.entity.Project.ProjectStatus;
import illinoistech.itm.web.system.integration.student_collabration_platform.repository.ProjectRepository;
import illinoistech.itm.web.system.integration.student_collabration_platform.repository.ProjectSpecs;
import illinoistech.itm.web.system.integration.student_collabration_platform.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.data.jpa.domain.Specification.allOf;


@Slf4j
@Service
@Transactional(readOnly = true)
public class ProjectServiceImpl implements ProjectService {

    private static final Logger log = LoggerFactory.getLogger(ProjectServiceImpl.class);
    private final ProjectRepository repo;
    private final UserRepository userRepository;

    public ProjectServiceImpl(ProjectRepository repo, UserRepository userRepository) {
        this.repo = repo;
        this.userRepository = userRepository;
    }

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

    @Transactional
    @Override
    public ProjectSummaryDto create(ProjectSummaryDto dto) {
        log.info("in create....");
        var proj = new Project();
        proj.setTitle(dto.title());
        proj.setDescription(dto.description());
        proj.setOwner(userRepository.getReferenceById(dto.ownerId()));
        proj.setDifficultyLevel(dto.difficultyLevel());
        proj.setCategory(dto.category());
        proj.setDuration(dto.duration());
        proj.setDurationMonths(dto.durationMonths());
        proj.setDeadline(dto.deadline());
        proj.setStartDate(dto.startDate());
        proj.setEndDate(dto.endDate());
        proj.setStatus(dto.status());
        proj.setCreatedAt(LocalDateTime.now());
        proj.setUpdatedAt(LocalDateTime.now());
        proj.setPublishedAt(LocalDateTime.now());

        var saved = repo.save(proj);
        log.info("created project: {}", saved);
        return ProjectSummaryDto.fromEntity(saved);
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
                p.getPublishedAt(),
                p.getRequirements()
        );
    }
}

