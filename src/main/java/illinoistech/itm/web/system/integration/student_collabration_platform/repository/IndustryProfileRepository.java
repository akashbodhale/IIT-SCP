package illinoistech.itm.web.system.integration.student_collabration_platform.repository;

import illinoistech.itm.web.system.integration.student_collabration_platform.entity.IndustryProfile;
import illinoistech.itm.web.system.integration.student_collabration_platform.entity.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IndustryProfileRepository extends JpaRepository<IndustryProfile, UUID> {
    Optional<IndustryProfile> findByUser_UserId(UUID userId);
}
