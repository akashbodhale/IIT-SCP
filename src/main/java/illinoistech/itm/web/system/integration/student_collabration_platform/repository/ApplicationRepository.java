package illinoistech.itm.web.system.integration.student_collabration_platform.repository;

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

    // paginated applications for an industry (all statuses)
    Page<Application> findByIndustry_ProfileId(UUID profileId, Pageable pageable);

    // paginated applications for an industry filtered by status
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

}
