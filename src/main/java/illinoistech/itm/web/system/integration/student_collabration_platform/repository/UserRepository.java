package illinoistech.itm.web.system.integration.student_collabration_platform.repository;

import illinoistech.itm.web.system.integration.student_collabration_platform.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<Users, UUID> {

    Optional<Users> findByEmailIgnoreCase(String email);

    boolean existsByEmail(String email);
}
