package illinoistech.itm.web.system.integration.student_collabration_platform.controller;

import illinoistech.itm.web.system.integration.student_collabration_platform.dto.IndustryProfileDto;
import illinoistech.itm.web.system.integration.student_collabration_platform.dto.StudentProfileDto;
import illinoistech.itm.web.system.integration.student_collabration_platform.service.IndustryProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/industry-profiles")
public class IndustryProfileController {
    private final IndustryProfileService industryProfileService;

    public IndustryProfileController(IndustryProfileService industryProfileService) {
        this.industryProfileService = industryProfileService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<IndustryProfileDto> getProfile(@PathVariable("userId") UUID userId) {
        IndustryProfileDto dto = industryProfileService.getProfileForUser(userId);
        return ResponseEntity.ok(dto);
    }
}
