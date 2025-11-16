package illinoistech.itm.web.system.integration.student_collabration_platform.service;

import illinoistech.itm.web.system.integration.student_collabration_platform.dto.ApplicationSummaryDto;
import illinoistech.itm.web.system.integration.student_collabration_platform.entity.Application;
import illinoistech.itm.web.system.integration.student_collabration_platform.entity.Application.ApplicationStatus;
import illinoistech.itm.web.system.integration.student_collabration_platform.repository.ApplicationRepository;
import illinoistech.itm.web.system.integration.student_collabration_platform.repository.ApplicationSpecs;
import illinoistech.itm.web.system.integration.student_collabration_platform.repository.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.data.jpa.domain.Specification.allOf;

@Slf4j
@Service
@Transactional(readOnly = true)
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository repo;
    private final ProjectRepository  projectRepo;
    public ApplicationServiceImpl(ApplicationRepository repo,  ProjectRepository projectRepo) {
        this.repo = repo;
        this.projectRepo= projectRepo;
    }

    @Override
    public ApplicationSummaryDto getById(java.util.UUID appId) {
        Application a = repo.findById(appId)
                .orElseThrow(() -> new EntityNotFoundException("Application not found: " + appId));
        ProjectRepository pr = projectRepo.findByProjectId(a.getProject().getProjectId());
        log.info("Project Repository: " + pr);
        return ApplicationSummaryDto.fromEntity(a);
    }


    @Override
    public Page<ApplicationSummaryDto> findMyApplications(java.util.UUID studentId,
                                                          ApplicationStatus status,
                                                          Pageable pageable) {
        var spec = allOf(
                ApplicationSpecs.hasStudent(studentId),
                ApplicationSpecs.hasStatus(status)
        );
        return repo.findAll(spec, pageable).map(ApplicationSummaryDto::fromEntity);
    }
}
