package illinoistech.itm.web.system.integration.student_collabration_platform.repository;

import illinoistech.itm.web.system.integration.student_collabration_platform.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID>, JpaSpecificationExecutor<Project> {

    // FIX: return Project or Optional<Project>, not ProjectRepository
    Optional<Project> findByProjectId(UUID projectId);

    List<Project> findByOwner_UserId(UUID ownerId);

    // NEW: for unique title check
    boolean existsByTitle(String title);

    @Query("""
    select count(distinct p.projectId)
    from Project p
      join Application a on a.project = p
      join a.student sp
      join sp.user u
    where u.userId = :userId
      and p.status = illinoistech.itm.web.system.integration.student_collabration_platform.entity.Project.ProjectStatus.OPEN
  """)
    long countOpenProjectsForUser(@Param("userId") UUID userId);

    @Query("""
    select count(distinct p.projectId)
    from Project p
      join Application a on a.project = p
      join a.student sp
      join sp.user u
    where u.userId = :userId
  """)
    long countProjectsForUserWithoutStatus(@Param("userId") UUID userId);
}
