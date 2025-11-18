package illinoistech.itm.web.system.integration.student_collabration_platform.controller;
import illinoistech.itm.web.system.integration.student_collabration_platform.dto.ProjectSummaryDto;
import illinoistech.itm.web.system.integration.student_collabration_platform.dto.ProjectUpdateRequest;
import illinoistech.itm.web.system.integration.student_collabration_platform.entity.Project;
import illinoistech.itm.web.system.integration.student_collabration_platform.entity.Project.ProjectStatus;
import illinoistech.itm.web.system.integration.student_collabration_platform.service.ProjectService;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private static final Logger log = LoggerFactory.getLogger(ProjectController.class);
    private final ProjectService projectSvc;

    public ProjectController(ProjectService projectSvc) {
        this.projectSvc = projectSvc;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectSummaryDto> getOne(@PathVariable("id") UUID id) {
        log.info("Inside {} - getOne method.", ProjectController.class.getSimpleName());
        return ResponseEntity.ok(projectSvc.getById(id));
    }

    /**
     *
     * @return
     */
    @GetMapping("/all")
    public ResponseEntity<List<ProjectSummaryDto>> getAllProjects() {
        log.info("Inside {} - getAllProjects method.", ProjectController.class.getSimpleName());
        List<ProjectSummaryDto> projects = projectSvc.getAll();
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/industry/{industry_id}")
    public ResponseEntity<List<ProjectSummaryDto>> getAllProjectsByIndustry(@PathVariable("industry_id") UUID id) {
        log.info("Inside {} - getAllProjectsByIndustry method.", ProjectController.class.getSimpleName());

        List<ProjectSummaryDto> projects = projectSvc.getAllByIndustry(id);
        return ResponseEntity.ok(projects);
    }

    @PostMapping("/create")
    public ResponseEntity<ProjectSummaryDto> createProject(@RequestBody ProjectSummaryDto project, UriComponentsBuilder uriBuilder)
    {
        log.info("Creating project: {}", project.title());
        ProjectSummaryDto saved = projectSvc.create(project);

        URI location = uriBuilder.path("/api/projects/{id}").buildAndExpand(saved.projectId()).toUri();

        return ResponseEntity.created(location).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectSummaryDto> update(
            @PathVariable UUID id,
            @RequestBody ProjectUpdateRequest request
    ) {
        log.info("Inside {} - update (PUT) method for id={}", ProjectController.class.getSimpleName(), id);
        ProjectSummaryDto updated = projectSvc.update(id, request);
        return ResponseEntity.ok(updated);
    }

    @GetMapping
    public ResponseEntity<Page<ProjectSummaryDto>> search(
            @RequestParam(value = "status", required = false) ProjectStatus status,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "ownerId", required = false) UUID ownerId,
            @RequestParam(value = "deadlineFrom", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate deadlineFrom,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "sort", defaultValue = "deadline,asc") String sort
    ) {
        log.info("Inside {} - search method.", ProjectController.class.getSimpleName());
        Pageable pageable = buildPageable(page, size, sort);
        return ResponseEntity.ok(projectSvc.search(status, category, ownerId, deadlineFrom, pageable));
    }

    private Pageable buildPageable(int page, int size, String sort) {
        // sort format: "field,dir" (e.g., "deadline,asc" or "createdAt,desc")
        String[] parts = sort.split(",", 2);
        String field = parts[0];
        Sort.Direction dir = (parts.length > 1 && parts[1].equalsIgnoreCase("desc"))
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        return PageRequest.of(page, size, Sort.by(dir, field));
    }
    
}

