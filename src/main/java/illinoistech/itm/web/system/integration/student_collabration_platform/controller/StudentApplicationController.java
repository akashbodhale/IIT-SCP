package illinoistech.itm.web.system.integration.student_collabration_platform.controller;

import illinoistech.itm.web.system.integration.student_collabration_platform.dto.MyApplicationDto;
import illinoistech.itm.web.system.integration.student_collabration_platform.service.ApplicationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/student")
@CrossOrigin(origins = "*")   // Adjust for production
public class StudentApplicationController {

    private final ApplicationService applicationService;

    public StudentApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping("/{studentId}/applications")
    public List<MyApplicationDto> getMyApplications(@PathVariable UUID studentId) {
        return applicationService.getMyApplications(studentId);
    }
}
