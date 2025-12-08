package illinoistech.itm.web.system.integration.student_collabration_platform.service;

import illinoistech.itm.web.system.integration.student_collabration_platform.dto.IndustryProfileDto;
import illinoistech.itm.web.system.integration.student_collabration_platform.dto.StudentProfileDto;

import java.util.UUID;

public interface IndustryProfileService {
    IndustryProfileDto getProfileForUser(UUID userId);
    IndustryProfileDto editProfileForUser(UUID userId, IndustryProfileDto profile);
}
