package illinoistech.itm.web.system.integration.student_collabration_platform.repository;

import illinoistech.itm.web.system.integration.student_collabration_platform.entity.IndustryProfile;
import illinoistech.itm.web.system.integration.student_collabration_platform.entity.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface IndustryProfileRepository extends JpaRepository<IndustryProfile, UUID> {
    Optional<IndustryProfile> findByUser_UserId(UUID userId);
    
    @Query("select ip.companyName from IndustryProfile ip where ip.user.userId = :userId")
    Optional<String> findCompanyNameByUserId(@Param("userId") UUID userId);

    @Query("select ip.profileId from IndustryProfile ip where ip.user.userId = :userId")
    Optional<UUID> findProfileIdByOwnerId(@Param("userId") UUID userId);
}
