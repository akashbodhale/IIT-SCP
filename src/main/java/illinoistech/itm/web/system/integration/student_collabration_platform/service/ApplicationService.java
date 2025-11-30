package illinoistech.itm.web.system.integration.student_collabration_platform.service;

import illinoistech.itm.web.system.integration.student_collabration_platform.dto.ApplicationSummaryDto;
import illinoistech.itm.web.system.integration.student_collabration_platform.entity.Application.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ApplicationService {
    List<ApplicationSummaryDto> getByUserId(UUID userId);
    Page<ApplicationSummaryDto> findMyApplications(UUID studentId, ApplicationStatus status, Pageable pageable);
    List<ApplicationSummaryDto> getApplicationsByProjectId(UUID projectId);

    // NEW: create application (student or industry)
    ApplicationSummaryDto applyToProject(UUID userId,
                                         UUID projectId,
                                         String coverLetterUrl,
                                         String portfolioLink);
}
