package illinoistech.itm.web.system.integration.student_collabration_platform.dto;

import illinoistech.itm.web.system.integration.student_collabration_platform.entity.StudentProfile;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public class StudentProfileDto {

    // From StudentProfile
    private UUID profileId;
    private UUID userId;
    private String university;
    private String studentId;
    private String major;
    private StudentProfile.AcademicYear academicYear;
    private LocalDate expectedGraduation;
    private String resumeUrl;
    private String portfolioUrl;
    private String githubUrl;
    private String linkedinUrl;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    // From Users (read-only)
    private String firstName;
    private String lastName;
    private String email;

    public StudentProfileDto() {}

    public UUID getProfileId() { return profileId; }
    public void setProfileId(UUID profileId) { this.profileId = profileId; }

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public String getUniversity() { return university; }
    public void setUniversity(String university) { this.university = university; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }

    public StudentProfile.AcademicYear getAcademicYear() { return academicYear; }
    public void setAcademicYear(StudentProfile.AcademicYear academicYear) { this.academicYear = academicYear; }

    public LocalDate getExpectedGraduation() { return expectedGraduation; }
    public void setExpectedGraduation(LocalDate expectedGraduation) { this.expectedGraduation = expectedGraduation; }

    public String getResumeUrl() { return resumeUrl; }
    public void setResumeUrl(String resumeUrl) { this.resumeUrl = resumeUrl; }

    public String getPortfolioUrl() { return portfolioUrl; }
    public void setPortfolioUrl(String portfolioUrl) { this.portfolioUrl = portfolioUrl; }

    public String getGithubUrl() { return githubUrl; }
    public void setGithubUrl(String githubUrl) { this.githubUrl = githubUrl; }

    public String getLinkedinUrl() { return linkedinUrl; }
    public void setLinkedinUrl(String linkedinUrl) { this.linkedinUrl = linkedinUrl; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
