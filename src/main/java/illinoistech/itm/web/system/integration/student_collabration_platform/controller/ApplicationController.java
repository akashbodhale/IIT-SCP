package illinoistech.itm.web.system.integration.student_collabration_platform.controller;

import illinoistech.itm.web.system.integration.student_collabration_platform.dto.ApplicationSummaryDto;
import illinoistech.itm.web.system.integration.student_collabration_platform.dto.CreateApplicationRequest;
import illinoistech.itm.web.system.integration.student_collabration_platform.dto.IndustryApplicationDto;
import illinoistech.itm.web.system.integration.student_collabration_platform.entity.Application.ApplicationStatus;
import illinoistech.itm.web.system.integration.student_collabration_platform.service.ApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationController.class.getSimpleName());
    private final ApplicationService appSvc;

    public ApplicationController(ApplicationService appSvc) {
        this.appSvc = appSvc;
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<ApplicationSummaryDto>> getOne(@PathVariable("id") UUID id) {
        logger.info("Inside {} - getOne method.", ApplicationController.class.getSimpleName());
        return ResponseEntity.ok(appSvc.getByUserId(id));
    }

    @GetMapping("/project/{id}")
    public ResponseEntity<List<ApplicationSummaryDto>> getApplicationByProjectId(@PathVariable("id") UUID id) {
        logger.info("Inside {} - getApplicationByProjectId method.", ApplicationController.class.getSimpleName());
        return ResponseEntity.ok(appSvc.getApplicationsByProjectId(id));
    }

    /** Feed for “My Applications” (student) */
    @GetMapping
    public ResponseEntity<Page<ApplicationSummaryDto>> myApplications(
            @RequestParam(value = "student_id", required = false) UUID studentIdSnake,
            @RequestParam(value = "studentId",  required = false) UUID studentIdCamel,
            @RequestParam(value = "status",     required = false) ApplicationStatus status,
            @RequestParam(value = "page",       defaultValue = "0") int page,
            @RequestParam(value = "size",       defaultValue = "20") int size,
            @RequestParam(value = "sort",       defaultValue = "updatedAt,desc") String sort
    ) {
        logger.info("Inside {} - myApplications method.", ApplicationController.class.getSimpleName());
        UUID studentId = (studentIdSnake != null) ? studentIdSnake : studentIdCamel;
        Pageable pageable = buildPageable(page, size, sort);
        return ResponseEntity.ok(appSvc.findMyApplications(studentId, status, pageable));
    }

    /** Industry applications (when industry is APPLICANT, by profile id) */
    @GetMapping("/industry")
    public ResponseEntity<Page<ApplicationSummaryDto>> industryApplications(
            @RequestParam(value = "industry_id", required = false) UUID industryIdSnake,
            @RequestParam(value = "industryId",  required = false) UUID industryIdCamel,
            @RequestParam(value = "status",      required = false) ApplicationStatus status,
            @RequestParam(value = "page",        defaultValue = "0") int page,
            @RequestParam(value = "size",        defaultValue = "20") int size,
            @RequestParam(value = "sort",        defaultValue = "updatedAt,desc") String sort
    ) {
        logger.info("Inside {} - industryApplications method.", ApplicationController.class.getSimpleName());
        UUID industryId = (industryIdSnake != null) ? industryIdSnake : industryIdCamel;
        Pageable pageable = buildPageable(page, size, sort);
        return ResponseEntity.ok(appSvc.findIndustryApplications(industryId, status, pageable));
    }

    // ========= NEW: INDUSTRY VIEW OF STUDENT APPLICATIONS =========
    /**
     * Student applications to projects owned by an INDUSTRY USER.
     *
     * Example:
     *   GET /api/applications/industry/project-applications
     *       ?industry_user_id=<user-uuid>
     *       &status=PENDING
     *       &page=0
     *       &size=20
     */
    @GetMapping("/industry/project-applications")
    public ResponseEntity<List<IndustryApplicationDto>> industryProjectApplicationsByUser(
            @RequestParam(value = "industry_user_id", required = false) UUID industryUserIdSnake,
            @RequestParam(value = "industryUserId",   required = false) UUID industryUserIdCamel,
            @RequestParam(value = "status",           required = false) ApplicationStatus status,
            @RequestParam(value = "page",             defaultValue = "0") int page,
            @RequestParam(value = "size",             defaultValue = "20") int size,
            @RequestParam(value = "sort",             defaultValue = "appliedAt,desc") String sort
    ) {
        logger.info("Inside {} - industryProjectApplicationsByUser method.", ApplicationController.class.getSimpleName());

        UUID industryUserId = (industryUserIdSnake != null) ? industryUserIdSnake : industryUserIdCamel;
        Pageable pageable = buildPageable(page, size, sort);

        Page<IndustryApplicationDto> pageResult =
                appSvc.findIndustryProjectApplicationsByUser(industryUserId, status, pageable);

        // return ONLY the list:
        return ResponseEntity.ok(pageResult.getContent());
    }

    private Pageable buildPageable(int page, int size, String sort) {
        String[] parts = sort.split(",", 2);
        String field = parts[0];
        Sort.Direction dir = (parts.length > 1 && parts[1].equalsIgnoreCase("asc"))
                ? Sort.Direction.ASC : Sort.Direction.DESC;
        return PageRequest.of(page, size, Sort.by(dir, field));
    }

    @PostMapping ("/apply")
    public ResponseEntity<ApplicationSummaryDto> applyToProject(@RequestBody CreateApplicationRequest request) {
        logger.info("Inside {} - applyToProject method.", ApplicationController.class.getSimpleName());

        ApplicationSummaryDto dto = appSvc.applyToProject(request.getUserId(),request.getProjectId(),request.getCoverLetterUrl(), request.getPortfolioLink());

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
}
