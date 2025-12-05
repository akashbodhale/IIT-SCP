package illinoistech.itm.web.system.integration.student_collabration_platform.controller;

import illinoistech.itm.web.system.integration.student_collabration_platform.dto.StudentProfileDto;
import illinoistech.itm.web.system.integration.student_collabration_platform.dto.StudentProfileRequest;
import illinoistech.itm.web.system.integration.student_collabration_platform.service.StudentProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/student-profiles")
public class StudentProfileController {

    private final StudentProfileService studentProfileService;

    public StudentProfileController(StudentProfileService studentProfileService) {
        this.studentProfileService = studentProfileService;
    }

    /**
     * Get student profile + user info for a userId.
     * If profile does not exist yet, returns firstName/lastName/email and nulls for profile fields.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<StudentProfileDto> getProfile(@PathVariable("userId") UUID userId) {
        StudentProfileDto dto = studentProfileService.getProfileForUser(userId);
        return ResponseEntity.ok(dto);
    }

    /**
     * Create a student profile for the given userId (only if not existing).
     */
    @PostMapping("/{userId}")
    public ResponseEntity<StudentProfileDto> createProfile(
            @PathVariable("userId") UUID userId,
            @RequestBody StudentProfileRequest request) {

        StudentProfileDto dto = studentProfileService.createProfileForUser(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    /**
     * Update existing student profile for the given userId.
     */
    @PutMapping("/{userId}")
    public ResponseEntity<StudentProfileDto> updateProfile(
            @PathVariable("userId") UUID userId,
            @RequestBody StudentProfileRequest request) {

        StudentProfileDto dto = studentProfileService.updateProfileForUser(userId, request);
        return ResponseEntity.ok(dto);
    }
}
