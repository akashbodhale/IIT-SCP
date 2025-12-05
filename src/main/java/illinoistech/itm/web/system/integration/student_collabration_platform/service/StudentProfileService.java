package illinoistech.itm.web.system.integration.student_collabration_platform.service;

import illinoistech.itm.web.system.integration.student_collabration_platform.dto.StudentProfileDto;
import illinoistech.itm.web.system.integration.student_collabration_platform.dto.StudentProfileRequest;

import java.util.UUID;

public interface StudentProfileService {

    StudentProfileDto getProfileForUser(UUID userId);

    StudentProfileDto createProfileForUser(UUID userId, StudentProfileRequest request);

    StudentProfileDto updateProfileForUser(UUID userId, StudentProfileRequest request);
}
