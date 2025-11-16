package illinoistech.itm.web.system.integration.student_collabration_platform.repository;

import illinoistech.itm.web.system.integration.student_collabration_platform.entity.Application;
import illinoistech.itm.web.system.integration.student_collabration_platform.entity.Application.ApplicationStatus;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public final class ApplicationSpecs {
    private ApplicationSpecs() {}

    public static Specification<Application> hasStudent(UUID studentId) {
        if (studentId == null) return null;
        return (root, q, cb) -> cb.equal(root.get("student").get("profileId"), studentId);
    }

    public static Specification<Application> hasStatus(ApplicationStatus status) {
        if (status == null) return null;
        return (root, q, cb) -> cb.equal(root.get("status"), status);
    }
}
