package illinoistech.itm.web.system.integration.student_collabration_platform.repository;


import illinoistech.itm.web.system.integration.student_collabration_platform.entity.Project;
import illinoistech.itm.web.system.integration.student_collabration_platform.entity.Project.ProjectStatus;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

public final class ProjectSpecs {

    private ProjectSpecs() {}

    public static Specification<Project> hasStatus(ProjectStatus status) {
        return (root, q, cb) -> status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<Project> hasCategoryIgnoreCase(String category) {
        return (root, q, cb) -> (category == null || category.isBlank())
                ? null
                : cb.equal(cb.lower(root.get("category")), category.toLowerCase());
    }

    public static Specification<Project> hasOwner(UUID ownerId) {
        return (root, q, cb) -> ownerId == null
                ? null
                : cb.equal(root.get("owner").get("userId"), ownerId);
    }

    public static Specification<Project> deadlineOnOrAfter(LocalDate from) {
        return (root, q, cb) -> from == null ? null : cb.greaterThanOrEqualTo(root.get("deadline"), from);
    }
}

