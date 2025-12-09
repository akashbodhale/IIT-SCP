package illinoistech.itm.web.system.integration.student_collabration_platform.dto;

import illinoistech.itm.web.system.integration.student_collabration_platform.entity.Application.ApplicationStatus;
import illinoistech.itm.web.system.integration.student_collabration_platform.entity.StudentProfile;

import java.time.OffsetDateTime;
import java.util.UUID;

public class IndustryApplicationDto {

    private UUID applicationId;

    private UUID projectId;
    private String projectTitle;
    private String projectSkills;   // from Project.skills

    private UUID studentProfileId;
    private UUID studentUserId;
    private String studentFirstName;
    private String studentLastName;
    private String studentEmail;

    private String studentUniversity;
    private String studentStudentId;
    private String studentMajor;
    private String studentAcademicYear;

    private OffsetDateTime appliedAt;
    private String status;

    private String coverLetterUrl;
    private String portfolioLink;

    // Default constructor
    public IndustryApplicationDto() {
    }

    // All-args constructor
    public IndustryApplicationDto(UUID applicationId, UUID projectId, String projectTitle, String projectSkills,
                                  UUID studentProfileId, UUID studentUserId, String studentFirstName,
                                  String studentLastName, String studentEmail, String studentUniversity,
                                  String studentStudentId, String studentMajor, String studentAcademicYear,
                                  OffsetDateTime appliedAt, String status, String coverLetterUrl, String portfolioLink) {
        this.applicationId = applicationId;
        this.projectId = projectId;
        this.projectTitle = projectTitle;
        this.projectSkills = projectSkills;
        this.studentProfileId = studentProfileId;
        this.studentUserId = studentUserId;
        this.studentFirstName = studentFirstName;
        this.studentLastName = studentLastName;
        this.studentEmail = studentEmail;
        this.studentUniversity = studentUniversity;
        this.studentStudentId = studentStudentId;
        this.studentMajor = studentMajor;
        this.studentAcademicYear = studentAcademicYear;
        this.appliedAt = appliedAt;
        this.status = status;
        this.coverLetterUrl = coverLetterUrl;
        this.portfolioLink = portfolioLink;
    }

    // Projection constructor for JPQL
    public IndustryApplicationDto(
            UUID applicationId,
            UUID projectId,
            String projectTitle,
            String projectSkills,
            UUID studentProfileId,
            UUID studentUserId,
            String studentFirstName,
            String studentLastName,
            String studentEmail,
            String studentUniversity,
            String studentStudentId,
            String studentMajor,
            StudentProfile.AcademicYear academicYear,
            OffsetDateTime appliedAt,
            ApplicationStatus status,
            String coverLetterUrl,
            String portfolioLink
    ) {
        this.applicationId = applicationId;
        this.projectId = projectId;
        this.projectTitle = projectTitle;
        this.projectSkills = projectSkills;
        this.studentProfileId = studentProfileId;
        this.studentUserId = studentUserId;
        this.studentFirstName = studentFirstName;
        this.studentLastName = studentLastName;
        this.studentEmail = studentEmail;
        this.studentUniversity = studentUniversity;
        this.studentStudentId = studentStudentId;
        this.studentMajor = studentMajor;
        this.studentAcademicYear = (academicYear != null ? academicYear.name() : null);
        this.appliedAt = appliedAt;
        this.status = (status != null ? status.name() : null);
        this.coverLetterUrl = coverLetterUrl;
        this.portfolioLink = portfolioLink;
    }

    // Getters
    public UUID getApplicationId() {
        return applicationId;
    }

    public UUID getProjectId() {
        return projectId;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public String getProjectSkills() {
        return projectSkills;
    }

    public UUID getStudentProfileId() {
        return studentProfileId;
    }

    public UUID getStudentUserId() {
        return studentUserId;
    }

    public String getStudentFirstName() {
        return studentFirstName;
    }

    public String getStudentLastName() {
        return studentLastName;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public String getStudentUniversity() {
        return studentUniversity;
    }

    public String getStudentStudentId() {
        return studentStudentId;
    }

    public String getStudentMajor() {
        return studentMajor;
    }

    public String getStudentAcademicYear() {
        return studentAcademicYear;
    }

    public OffsetDateTime getAppliedAt() {
        return appliedAt;
    }

    public String getStatus() {
        return status;
    }

    public String getCoverLetterUrl() {
        return coverLetterUrl;
    }

    public String getPortfolioLink() {
        return portfolioLink;
    }

    // Setters
    public void setApplicationId(UUID applicationId) {
        this.applicationId = applicationId;
    }

    public void setProjectId(UUID projectId) {
        this.projectId = projectId;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public void setProjectSkills(String projectSkills) {
        this.projectSkills = projectSkills;
    }

    public void setStudentProfileId(UUID studentProfileId) {
        this.studentProfileId = studentProfileId;
    }

    public void setStudentUserId(UUID studentUserId) {
        this.studentUserId = studentUserId;
    }

    public void setStudentFirstName(String studentFirstName) {
        this.studentFirstName = studentFirstName;
    }

    public void setStudentLastName(String studentLastName) {
        this.studentLastName = studentLastName;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public void setStudentUniversity(String studentUniversity) {
        this.studentUniversity = studentUniversity;
    }

    public void setStudentStudentId(String studentStudentId) {
        this.studentStudentId = studentStudentId;
    }

    public void setStudentMajor(String studentMajor) {
        this.studentMajor = studentMajor;
    }

    public void setStudentAcademicYear(String studentAcademicYear) {
        this.studentAcademicYear = studentAcademicYear;
    }

    public void setAppliedAt(OffsetDateTime appliedAt) {
        this.appliedAt = appliedAt;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCoverLetterUrl(String coverLetterUrl) {
        this.coverLetterUrl = coverLetterUrl;
    }

    public void setPortfolioLink(String portfolioLink) {
        this.portfolioLink = portfolioLink;
    }
}
