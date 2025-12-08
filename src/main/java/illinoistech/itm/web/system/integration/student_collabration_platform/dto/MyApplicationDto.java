package illinoistech.itm.web.system.integration.student_collabration_platform.dto;

import illinoistech.itm.web.system.integration.student_collabration_platform.entity.Application.ApplicationStatus;
import java.time.OffsetDateTime;

public class MyApplicationDto {

    private String projectName;
    private String companyName;
    private OffsetDateTime appliedAt;
    private String status; // OR enum depending on your Application entity

    // Default constructor
    public MyApplicationDto() {
    }

    // All-args constructor
    public MyApplicationDto(String projectName, String companyName, OffsetDateTime appliedAt, String status) {
        this.projectName = projectName;
        this.companyName = companyName;
        this.appliedAt = appliedAt;
        this.status = status;
    }

    // Constructor for JPQL query that accepts enum
    public MyApplicationDto(String projectName, String companyName, OffsetDateTime appliedAt, ApplicationStatus status) {
        this.projectName = projectName;
        this.companyName = companyName;
        this.appliedAt = appliedAt;
        this.status = status != null ? status.name() : null;
    }

    // Getters
    public String getProjectName() {
        return projectName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public OffsetDateTime getAppliedAt() {
        return appliedAt;
    }

    public String getStatus() {
        return status;
    }

    // Setters
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setAppliedAt(OffsetDateTime appliedAt) {
        this.appliedAt = appliedAt;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

