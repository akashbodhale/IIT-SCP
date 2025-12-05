package illinoistech.itm.web.system.integration.student_collabration_platform.dto;

import illinoistech.itm.web.system.integration.student_collabration_platform.entity.StudentProfile;

import java.time.LocalDate;

public class StudentProfileRequest {

    private String university;
    private String major;
    private StudentProfile.AcademicYear academicYear;
    private LocalDate expectedGraduation;
    private String resumeUrl;
    private String portfolioUrl;
    private String githubUrl;
    private String linkedinUrl;

    public StudentProfileRequest() {}

    public String getUniversity() { return university; }
    public void setUniversity(String university) { this.university = university; }

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
}
