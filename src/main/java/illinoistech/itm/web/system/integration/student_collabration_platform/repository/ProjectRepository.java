package illinoistech.itm.web.system.integration.student_collabration_platform.repository;

import illinoistech.itm.web.system.integration.student_collabration_platform.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID>, JpaSpecificationExecutor<Project> {

    // FIX: return Project or Optional<Project>, not ProjectRepository
    Optional<Project> findByProjectId(UUID projectId);

    List<Project> findByOwner_UserId(UUID ownerId);

    // NEW: for unique title check
    boolean existsByTitle(String title);
}
