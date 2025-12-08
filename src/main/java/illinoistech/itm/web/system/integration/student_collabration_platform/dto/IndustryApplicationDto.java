package illinoistech.itm.web.system.integration.student_collabration_platform.dto;

import illinoistech.itm.web.system.integration.student_collabration_platform.entity.Application.ApplicationStatus;
import illinoistech.itm.web.system.integration.student_collabration_platform.entity.StudentProfile;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
}
