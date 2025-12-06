package illinoistech.itm.web.system.integration.student_collabration_platform.dto;

import illinoistech.itm.web.system.integration.student_collabration_platform.entity.Application.ApplicationStatus;
import java.time.OffsetDateTime;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyApplicationDto {

    private String projectName;
    private String companyName;
    private OffsetDateTime appliedAt;
    private String status; // OR enum depending on your Application entity

    // Constructor for JPQL query that accepts enum
    public MyApplicationDto(String projectName, String companyName, OffsetDateTime appliedAt, ApplicationStatus status) {
        this.projectName = projectName;
        this.companyName = companyName;
        this.appliedAt = appliedAt;
        this.status = status != null ? status.name() : null;
    }
}

