package illinoistech.itm.web.system.integration.student_collabration_platform.repository;

import illinoistech.itm.web.system.integration.student_collabration_platform.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

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
}
