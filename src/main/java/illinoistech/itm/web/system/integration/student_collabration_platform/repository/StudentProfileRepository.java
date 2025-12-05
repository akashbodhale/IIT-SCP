package illinoistech.itm.web.system.integration.student_collabration_platform.repository;

import illinoistech.itm.web.system.integration.student_collabration_platform.entity.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StudentProfileRepository extends JpaRepository<StudentProfile, UUID> {

    // you already had this
    Optional<StudentProfile> findByUser_UserId(UUID userId);

    // new helper: check if a profile already exists for a user
    boolean existsByUser_UserId(UUID userId);
}
