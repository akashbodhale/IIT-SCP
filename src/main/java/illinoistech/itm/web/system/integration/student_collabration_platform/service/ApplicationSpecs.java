package illinoistech.itm.web.system.integration.student_collabration_platform.service;

import illinoistech.itm.web.system.integration.student_collabration_platform.entity.Application;
import illinoistech.itm.web.system.integration.student_collabration_platform.entity.Application.ApplicationStatus;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

/**
 * Specification helpers for filtering Application entities.
 * Used in ApplicationServiceImpl.findMyApplications(...)
 */
public final class ApplicationSpecs {

    private ApplicationSpecs() {
        // utility class, no instances
    }

    /**
     * Filter by student profile id.
     * If studentId is null, returns null so it is ignored by Specification.allOf(...).
     */
    public static Specification<Application> hasStudent(UUID studentId) {
        if (studentId == null) {
            return null; // allOf(...) will skip null specs
        }
        return (root, query, cb) ->
                cb.equal(root.get("student").get("profileId"), studentId);
    }

    /**
     * Filter by application status.
     * If status is null, returns null so it is ignored by Specification.allOf(...).
     */
    public static Specification<Application> hasStatus(ApplicationStatus status) {
        if (status == null) {
            return null;
        }
        return (root, query, cb) ->
                cb.equal(root.get("status"), status);
    }
}
