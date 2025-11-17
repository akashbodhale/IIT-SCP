package illinoistech.itm.web.system.integration.student_collabration_platform.service;

import illinoistech.itm.web.system.integration.student_collabration_platform.dto.ApplicationSummaryDto;
import illinoistech.itm.web.system.integration.student_collabration_platform.entity.Application;
import illinoistech.itm.web.system.integration.student_collabration_platform.entity.Application.ApplicationStatus;
import illinoistech.itm.web.system.integration.student_collabration_platform.entity.IndustryProfile;
import illinoistech.itm.web.system.integration.student_collabration_platform.entity.StudentProfile;
import illinoistech.itm.web.system.integration.student_collabration_platform.entity.Users;
import illinoistech.itm.web.system.integration.student_collabration_platform.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final ProjectRepository  projectRepo;
    public ApplicationServiceImpl(ApplicationRepository repo, ProjectRepository projectRepo, IndustryProfileRepository industryProfileRepo,UserRepository userRepo, StudentProfileRepository studentProfileRepo) {
        this.appRepo = repo;
        this.projectRepo= projectRepo;
        this.userRepo=userRepo;
        this.industryProfileRepo = industryProfileRepo;
        this.studentProfileRepo=studentProfileRepo;
    }

    @Override
    public List<ApplicationSummaryDto> getByUserId(UUID userId) {

        // 1. Load the user
        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));

        String userType = user.getUserType(); // e.g. "STUDENT" / "INDUSTRY"

        // 2. Branch by user type
        if ("student".equalsIgnoreCase(userType)) {

            // 2a. Find StudentProfile by user_id
            StudentProfile studentProfile = studentProfileRepo
                    .findByUser_UserId(userId)
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Student profile not found for user: " + userId));

            // 2b. Find all applications by student_profile_id
            List<Application> applications = appRepo
                    .findByStudent_profileId(studentProfile.getProfileId());

            // 2c. Map to DTOs
            return applications.stream()
                    .map(ApplicationSummaryDto::fromEntity)
                    .toList();
        }

        if ("industry".equalsIgnoreCase(userType)) {

            // 2a. Find IndustryProfile by user_id
            IndustryProfile industryProfile = industryProfileRepo
                    .findByUser_UserId(userId)
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Industry profile not found for user: " + userId));

            // 2b. Find all applications by industry_profile_id
            List<Application> applications = appRepo
                    .findByIndustry_profileId(industryProfile.getProfileId());

            // 2c. Map to DTOs
            return applications.stream()
                    .map(ApplicationSummaryDto::fromEntity)
                    .toList();
        }

        // Optional: if userType is something else (e.g. "ADMIN")
        throw new IllegalStateException("Unsupported user type for applications: " + userType);
    }



    @Override
    public Page<ApplicationSummaryDto> findMyApplications(java.util.UUID studentId,
                                                          ApplicationStatus status,
                                                          Pageable pageable) {
        var spec = allOf(
                ApplicationSpecs.hasStudent(studentId),
                ApplicationSpecs.hasStatus(status)
        );
        return appRepo.findAll(spec, pageable).map(ApplicationSummaryDto::fromEntity);
    }
}
