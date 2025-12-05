package illinoistech.itm.web.system.integration.student_collabration_platform.service;

import illinoistech.itm.web.system.integration.student_collabration_platform.dto.StudentProfileDto;
import illinoistech.itm.web.system.integration.student_collabration_platform.dto.StudentProfileRequest;
import illinoistech.itm.web.system.integration.student_collabration_platform.entity.StudentProfile;
import illinoistech.itm.web.system.integration.student_collabration_platform.entity.Users;
import illinoistech.itm.web.system.integration.student_collabration_platform.repository.StudentProfileRepository;
import illinoistech.itm.web.system.integration.student_collabration_platform.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class StudentProfileServiceImpl implements StudentProfileService {

    private final StudentProfileRepository studentProfileRepository;
    private final UserRepository userRepository;

    public StudentProfileServiceImpl(StudentProfileRepository studentProfileRepository,
                                     UserRepository userRepository) {
        this.studentProfileRepository = studentProfileRepository;
        this.userRepository = userRepository;
    }

    @Override
    public StudentProfileDto getProfileForUser(UUID userId) {
        Users user = getUserOrThrow(userId);
        StudentProfile profile = studentProfileRepository
                .findByUser_UserId(userId)
                .orElse(null);

        return toDto(user, profile);
    }

    @Override
    @Transactional
    public StudentProfileDto createProfileForUser(UUID userId, StudentProfileRequest request) {
        Users user = getUserOrThrow(userId);
        validateStudentUserType(user);

        if (studentProfileRepository.existsByUser_UserId(userId)) {
            throw new IllegalStateException("Student profile already exists for user " + userId);
        }

        StudentProfile profile = new StudentProfile();
        profile.setUser(user);
        profile.setUniversity(request.getUniversity());
        // auto-generate studentId
        profile.setstudentId(generateStudentId(user));
        profile.setMajor(request.getMajor());
        profile.setAcademicYear(request.getAcademicYear());
        profile.setExpectedGraduation(request.getExpectedGraduation());
        profile.setResumeUrl(request.getResumeUrl());
        profile.setPortfolioUrl(request.getPortfolioUrl());
        profile.setGithubUrl(request.getGithubUrl());
        profile.setLinkedinUrl(request.getLinkedinUrl());

        StudentProfile saved = studentProfileRepository.save(profile);
        return toDto(user, saved);
    }

    @Override
    @Transactional
    public StudentProfileDto updateProfileForUser(UUID userId, StudentProfileRequest request) {
        Users user = getUserOrThrow(userId);
        validateStudentUserType(user);

        StudentProfile profile = studentProfileRepository
                .findByUser_UserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Student profile not found for user " + userId));

        profile.setUniversity(request.getUniversity());
        // IMPORTANT: do NOT change studentId on update
        profile.setMajor(request.getMajor());
        profile.setAcademicYear(request.getAcademicYear());
        profile.setExpectedGraduation(request.getExpectedGraduation());
        profile.setResumeUrl(request.getResumeUrl());
        profile.setPortfolioUrl(request.getPortfolioUrl());
        profile.setGithubUrl(request.getGithubUrl());
        profile.setLinkedinUrl(request.getLinkedinUrl());

        StudentProfile saved = studentProfileRepository.save(profile);
        return toDto(user, saved);
    }

    // ---------- helpers ----------

    private Users getUserOrThrow(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));
    }

    private void validateStudentUserType(Users user) {
        if (user.getUserType() == null || !user.getUserType().equalsIgnoreCase("STUDENT")) {
            throw new IllegalStateException("User is not a student and cannot have a student profile");
        }
    }

    // auto-generate a studentId string (you can tweak the pattern if you want)
    private String generateStudentId(Users user) {
        // Example: STD-3C4F9A2B
        String shortUuid = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        return "STD-" + shortUuid;
    }

    private StudentProfileDto toDto(Users user, StudentProfile profile) {
        StudentProfileDto dto = new StudentProfileDto();

        // user info (read-only)
        dto.setUserId(user.getUserId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());

        if (profile != null) {
            dto.setProfileId(profile.getProfileId());
            dto.setUniversity(profile.getUniversity());
            dto.setStudentId(profile.getstudentId());
            dto.setMajor(profile.getMajor());
            dto.setAcademicYear(profile.getAcademicYear());
            dto.setExpectedGraduation(profile.getExpectedGraduation());
            dto.setResumeUrl(profile.getResumeUrl());
            dto.setPortfolioUrl(profile.getPortfolioUrl());
            dto.setGithubUrl(profile.getGithubUrl());
            dto.setLinkedinUrl(profile.getLinkedinUrl());
            dto.setCreatedAt(profile.getCreatedAt());
            dto.setUpdatedAt(profile.getUpdatedAt());
        }

        return dto;
    }
}
