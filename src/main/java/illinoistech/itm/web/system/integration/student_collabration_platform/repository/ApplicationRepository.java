package illinoistech.itm.web.system.integration.student_collabration_platform.repository;

import illinoistech.itm.web.system.integration.student_collabration_platform.dto.IndustryApplicationDto;
import illinoistech.itm.web.system.integration.student_collabration_platform.dto.MyApplicationDto;
import illinoistech.itm.web.system.integration.student_collabration_platform.entity.Application;
import illinoistech.itm.web.system.integration.student_collabration_platform.entity.Application.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ApplicationRepository
        extends JpaRepository<Application, UUID>, JpaSpecificationExecutor<Application> {

    // for getByUserId (student side)
    List<Application> findByStudent_ProfileId(UUID profileId);

    // for getByUserId (industry side)
    List<Application> findByIndustry_ProfileId(UUID profileId);

    // for getApplicationsByProjectId
    List<Application> findByProject_ProjectId(UUID projectId);

    // NEW: to enforce "student can apply only once per project"
    boolean existsByProject_ProjectIdAndStudent_ProfileId(UUID projectId, UUID studentProfileId);

    // ========= NEW FOR INDUSTRY APPLICATIONS PAGE =========

    // paginated applications for an industry (all statuses) – when industry is APPLICANT
    Page<Application> findByIndustry_ProfileId(UUID profileId, Pageable pageable);

    // paginated applications for an industry filtered by status – when industry is APPLICANT
    Page<Application> findByIndustry_ProfileIdAndStatus(UUID profileId,
                                                        ApplicationStatus status,
                                                        Pageable pageable);

    @Query("""
        SELECT new illinoistech.itm.web.system.integration.student_collabration_platform.dto.MyApplicationDto(
            p.title,
            ip.companyName,
            a.appliedAt,
            a.status
        )
        FROM Application a
        JOIN a.project p
        JOIN IndustryProfile ip ON ip.user.userId = p.owner.userId
        WHERE a.student.user.userId = :studentUserId
        ORDER BY a.appliedAt DESC
        """)
    List<MyApplicationDto> findMyApplications(UUID studentUserId);

    // ========= NEW: INDUSTRY VIEW – STUDENT APPS TO THEIR PROJECTS =========

    /**
     * All student applications to projects owned by a given INDUSTRY USER (no status filter).
     */
    @Query("""
        SELECT new illinoistech.itm.web.system.integration.student_collabration_platform.dto.IndustryApplicationDto(
            a.applicationId,
            p.projectId,
            p.title,
            p.skills,
            sp.profileId,
            stuUser.userId,
            stuUser.firstName,
            stuUser.lastName,
            stuUser.email,
            sp.university,
            sp.studentId,
            sp.major,
            sp.academicYear,
            a.appliedAt,
            a.status,
            a.coverLetterUrl,
            a.portfolioLink
        )
        FROM Application a
        JOIN a.project p
        JOIN p.owner su
        JOIN a.student sp
        JOIN sp.user stuUser
        WHERE su.userId = :industryUserId
        """)
    Page<IndustryApplicationDto> findIndustryProjectApplications(UUID industryUserId,
                                                                 Pageable pageable);

    /**
     * Same as above, but filtered by Application.status.
     */
    @Query("""
        SELECT new illinoistech.itm.web.system.integration.student_collabration_platform.dto.IndustryApplicationDto(
            a.applicationId,
            p.projectId,
            p.title,
            p.skills,
            sp.profileId,
            stuUser.userId,
            stuUser.firstName,
            stuUser.lastName,
            stuUser.email,
            sp.university,
            sp.studentId,
            sp.major,
            sp.academicYear,
            a.appliedAt,
            a.status,
            a.coverLetterUrl,
            a.portfolioLink
        )
        FROM Application a
        JOIN a.project p
        JOIN p.owner su
        JOIN a.student sp
        JOIN sp.user stuUser
        WHERE su.userId = :industryUserId
          AND a.status = :status
        """)
    Page<IndustryApplicationDto> findIndustryProjectApplications(UUID industryUserId,
                                                                 ApplicationStatus status,
                                                                 Pageable pageable);
    @Query("""
        SELECT new illinoistech.itm.web.system.integration.student_collabration_platform.dto.IndustryApplicationDto(
            a.applicationId,
            p.projectId,
            p.title,
            p.skills,
            sp.profileId,
            stuUser.userId,
            stuUser.firstName,
            stuUser.lastName,
            stuUser.email,
            sp.university,
            sp.studentId,
            sp.major,
            sp.academicYear,
            a.appliedAt,
            a.status,
            a.coverLetterUrl,
            a.portfolioLink
        )
        FROM Application a
        JOIN a.project p
        JOIN p.owner su
        JOIN a.student sp
        JOIN sp.user stuUser
        WHERE su.userId = :industryUserId
        """)
    List<IndustryApplicationDto>findIndustryProjectApplicationswithoutstatus(UUID industryUserId);
}
