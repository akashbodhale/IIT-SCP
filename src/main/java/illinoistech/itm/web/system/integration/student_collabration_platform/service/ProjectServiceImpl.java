package illinoistech.itm.web.system.integration.student_collabration_platform.service;

import illinoistech.itm.web.system.integration.student_collabration_platform.dto.ProjectSummaryDto;
import illinoistech.itm.web.system.integration.student_collabration_platform.dto.ProjectUpdateRequest;
import illinoistech.itm.web.system.integration.student_collabration_platform.entity.IndustryProfile;
import illinoistech.itm.web.system.integration.student_collabration_platform.entity.Project;
import illinoistech.itm.web.system.integration.student_collabration_platform.entity.Project.ProjectStatus;
import illinoistech.itm.web.system.integration.student_collabration_platform.repository.IndustryProfileRepository;
import illinoistech.itm.web.system.integration.student_collabration_platform.repository.ProjectRepository;
import illinoistech.itm.web.system.integration.student_collabration_platform.repository.ProjectSpecs;
import illinoistech.itm.web.system.integration.student_collabration_platform.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.data.jpa.domain.Specification.allOf;



@Service
@Transactional(readOnly = true)
public class ProjectServiceImpl implements ProjectService {

    private static final Logger log = LoggerFactory.getLogger(ProjectServiceImpl.class);
    private final ProjectRepository repo;
    private final UserRepository userRepository;
    private final IndustryProfileRepository industryProfileRepo;

    public ProjectServiceImpl(ProjectRepository repo, UserRepository userRepository, IndustryProfileRepository industryProfileRepo) {
        this.repo = repo;
        this.userRepository = userRepository;
        this.industryProfileRepo = industryProfileRepo;
    }

    @Override
    public ProjectSummaryDto getById(UUID projectId) {
        Project p = repo.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found: " + projectId));
        return toDto(p);
    }

    @Override
    public List<ProjectSummaryDto> getAll() {
        List<Project> all = repo.findAll();

        List<ProjectSummaryDto> result = new ArrayList<>(all.size());
        for (Project p : all) {
            UUID ownerId = (p.getOwner() != null) ? p.getOwner().getUserId() : null;
            String company = (ownerId != null)
                    ? industryProfileRepo.findCompanyNameByUserId(ownerId).orElse(null)
                    : null;
            result.add(ProjectSummaryDto.fromEntity(p, company));
        }
        return result;
    }

    @Override
    public List<ProjectSummaryDto> getAllByIndustry(UUID industryId) {

        List<Project> projects = repo.findByOwner_UserId(industryId);



        String company = industryProfileRepo
                .findCompanyNameByUserId(industryId)
                .orElse(null);

        List<ProjectSummaryDto> result = new ArrayList<>(projects.size());
        for (Project p : projects) {
            result.add(ProjectSummaryDto.fromEntity(p, company));
        }
        return result;
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

        proj.setDeadline(dto.deadline());
        proj.setPostedDate(dto.postedDate());

        proj.setStatus(dto.status());
        proj.setCreatedAt(LocalDateTime.now());
        proj.setUpdatedAt(LocalDateTime.now());
        proj.setPublishedAt(LocalDateTime.now());
        proj.setSpecificRequirements(dto.specificRequirements());
        proj.setDeliverables(dto.deliverables());
        proj.setProjectObjective(dto.project_objectives());
        proj.setSkills(dto.skills());

        var saved = repo.save(proj);
        log.info("created project: {}", saved);
        return ProjectSummaryDto.fromEntity(saved,"");
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

    @Transactional
    @Override
    public ProjectSummaryDto update(UUID id, ProjectUpdateRequest req) {

        Project entity = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found: " + id));

        // Map ALL fields from req (full replace semantics)
        entity.setTitle(req.title());
        entity.setDescription(req.description());
        entity.setCategory(req.category());
        entity.setDifficultyLevel(req.difficultyLevel());
        entity.setDuration(req.duration());

        entity.setDeadline(req.deadline());
        entity.setPostedDate(req.startDate());
        entity.setStatus(req.status());
        entity.setSpecificRequirements(req.requirments());

        entity.setUpdatedAt(java.time.LocalDateTime.now());

        Project saved = repo.save(entity);
        log.info("updated project: {}", saved.getDeadline());
        return ProjectSummaryDto.fromEntity(saved,"");
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
                p.getDeadline(),
                p.getPostedDate(),
                p.getStatus(),
                p.getApplicationsCount(),
                p.getSpecificRequirements(),
                p.getDeliverables(),
                p.getSkills(),
                p.getCreatedAt(),
                p.getUpdatedAt(),
                p.getPublishedAt(),
                p.getProjectObjective(),
                ""
        );
    }

}

