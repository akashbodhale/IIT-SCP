package illinoistech.itm.web.system.integration.student_collabration_platform.service;

import illinoistech.itm.web.system.integration.student_collabration_platform.dto.ApplicationSummaryDto;
import illinoistech.itm.web.system.integration.student_collabration_platform.dto.IndustryApplicationDto;
import illinoistech.itm.web.system.integration.student_collabration_platform.dto.MyApplicationDto;
import illinoistech.itm.web.system.integration.student_collabration_platform.entity.Application;
import illinoistech.itm.web.system.integration.student_collabration_platform.entity.Application.ApplicationStatus;
import illinoistech.itm.web.system.integration.student_collabration_platform.entity.IndustryProfile;
import illinoistech.itm.web.system.integration.student_collabration_platform.entity.StudentProfile;
import illinoistech.itm.web.system.integration.student_collabration_platform.entity.Users;
import illinoistech.itm.web.system.integration.student_collabration_platform.repository.ApplicationRepository;
import illinoistech.itm.web.system.integration.student_collabration_platform.repository.IndustryProfileRepository;
import illinoistech.itm.web.system.integration.student_collabration_platform.repository.ProjectRepository;
import illinoistech.itm.web.system.integration.student_collabration_platform.repository.StudentProfileRepository;
import illinoistech.itm.web.system.integration.student_collabration_platform.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

import static org.springframework.data.jpa.domain.Specification.allOf;

@Slf4j
@Service
@Transactional(readOnly = true)
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository appRepo;
    private final UserRepository userRepo;
    private final StudentProfileRepository studentProfileRepo;
    private final IndustryProfileRepository industryProfileRepo;
    private final ProjectRepository projectRepo;

    public ApplicationServiceImpl(
            ApplicationRepository repo,
            ProjectRepository projectRepo,
            IndustryProfileRepository industryProfileRepo,
            UserRepository userRepo,
            StudentProfileRepository studentProfileRepo
    ) {
        this.appRepo = repo;
        this.projectRepo = projectRepo;
        this.userRepo = userRepo;
        this.industryProfileRepo = industryProfileRepo;
        this.studentProfileRepo = studentProfileRepo;
    }

    @Override
    public List<ApplicationSummaryDto> getByUserId(UUID userId) {

        // 1. Load the user
        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));

        String userType = user.getUserType(); // e.g. "student" / "industry"

        // 2. Branch by user type
        if ("student".equalsIgnoreCase(userType)) {

            StudentProfile studentProfile = studentProfileRepo
                    .findByUser_UserId(userId)
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Student profile not found for user: " + userId));

            List<Application> applications = appRepo
                    .findByStudent_ProfileId(studentProfile.getProfileId());

            return applications.stream()
                    .map(ApplicationSummaryDto::fromEntity)
                    .toList();
        }

        if ("industry".equalsIgnoreCase(userType)) {

            IndustryProfile industryProfile = industryProfileRepo
                    .findByUser_UserId(userId)
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Industry profile not found for user: " + userId));

            List<Application> applications = appRepo
                    .findByIndustry_ProfileId(industryProfile.getProfileId());

            return applications.stream()
                    .map(ApplicationSummaryDto::fromEntity)
                    .toList();
        }

        throw new IllegalStateException("Unsupported user type for applications: " + userType);
    }

    @Override
    public List<ApplicationSummaryDto> getApplicationsByProjectId(UUID projectId) {
        List<Application> apps = appRepo.findByProject_ProjectId(projectId);
        return apps.stream()
                .map(ApplicationSummaryDto::fromEntity)
                .toList();
    }

    @Override
    public Page<ApplicationSummaryDto> findMyApplications(UUID studentId,
                                                          ApplicationStatus status,
                                                          Pageable pageable) {
        var spec = allOf(
                ApplicationSpecs.hasStudent(studentId),
                ApplicationSpecs.hasStatus(status)
        );
        return appRepo.findAll(spec, pageable).map(ApplicationSummaryDto::fromEntity);
    }

    // ========================= EXISTING METHOD =========================
    // Industry applications feed (where industry is APPLICANT)
    // ==============================================================
    @Override
    public Page<ApplicationSummaryDto> findIndustryApplications(UUID industryProfileId,
                                                                ApplicationStatus status,
                                                                Pageable pageable) {
        if (industryProfileId == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "industry_id / industryId is required");
        }

        Page<Application> apps;
        if (status == null) {
            apps = appRepo.findByIndustry_ProfileId(industryProfileId, pageable);
        } else {
            apps = appRepo.findByIndustry_ProfileIdAndStatus(industryProfileId, status, pageable);
        }

        return apps.map(ApplicationSummaryDto::fromEntity);
    }

    // ========================= NEW METHOD =========================
    // Student applications to projects OWNED by an INDUSTRY USER.
    // Powers the Industry → Applications page (screenshot).
    // ==============================================================
    @Override
    public Page<IndustryApplicationDto> findIndustryProjectApplicationsByUser(UUID industryUserId,
                                                                              ApplicationStatus status,
                                                                              Pageable pageable) {

        if (industryUserId == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "industry_user_id / industryUserId is required"
            );
        }

        // ensure user exists and is industry
        Users user = userRepo.findById(industryUserId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + industryUserId));

        if (!"industry".equalsIgnoreCase(user.getUserType())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "User is not an industry user"
            );
        }

        if (status == null) {
            return appRepo.findIndustryProjectApplications(industryUserId, pageable);
        } else {
            return appRepo.findIndustryProjectApplications(industryUserId, status, pageable);
        }
    }

    // ========================= EXISTING METHOD =========================
    // Student/Industry applies to a project
    // ==============================================================
    @Override
    @Transactional
    public ApplicationSummaryDto applyToProject(UUID userId,
                                                UUID projectId,
                                                String coverLetterUrl,
                                                String portfolioLink) {

        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));

        String userType = user.getUserType(); // "student" / "industry"

        var project = projectRepo.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found: " + projectId));

        // ---------- STUDENT FLOW ----------
        if ("student".equalsIgnoreCase(userType)) {

            StudentProfile studentProfile = studentProfileRepo
                    .findByUser_UserId(userId)
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Student profile not found for user: " + userId));

            boolean alreadyApplied = appRepo
                    .existsByProject_ProjectIdAndStudent_ProfileId(projectId, studentProfile.getProfileId());

            if (alreadyApplied) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "You have already applied to this project.");
            }

            // NEW: denormalize industry user id (project owner)
            UUID industryUserId = null;
            if (project.getOwner() != null) {
                industryUserId = project.getOwner().getUserId();
            }

            Application app = new Application();
            app.setProject(project);
            app.setStudent(studentProfile);
            app.setCoverLetterUrl(coverLetterUrl);
            app.setPortfolioLink(portfolioLink);
            app.setStatus(ApplicationStatus.PENDING);
            app.setIndustryUserId(industryUserId); // NEW

            Application saved = appRepo.save(app);
            return ApplicationSummaryDto.fromEntity(saved);
        }

        // ---------- INDUSTRY FLOW ----------
        if ("industry".equalsIgnoreCase(userType)) {

            IndustryProfile industryProfile = industryProfileRepo
                    .findByUser_UserId(userId)
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Industry profile not found for user: " + userId));

            Application app = new Application();
            app.setProject(project);
            app.setIndustry(industryProfile);
            app.setCoverLetterUrl(coverLetterUrl);
            app.setPortfolioLink(portfolioLink);
            app.setStatus(ApplicationStatus.PENDING);

            Application saved = appRepo.save(app);
            return ApplicationSummaryDto.fromEntity(saved);
        }

        throw new IllegalStateException("Unsupported user type for applications: " + userType);
    }

    @Override
    public List<MyApplicationDto> getMyApplications(UUID studentUserId) {
        return appRepo.findMyApplications(studentUserId);
    }
}
