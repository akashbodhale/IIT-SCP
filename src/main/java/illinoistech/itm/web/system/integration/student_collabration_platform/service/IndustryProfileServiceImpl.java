package illinoistech.itm.web.system.integration.student_collabration_platform.service;

import illinoistech.itm.web.system.integration.student_collabration_platform.dto.IndustryProfileDto;
import illinoistech.itm.web.system.integration.student_collabration_platform.dto.StudentProfileDto;
import illinoistech.itm.web.system.integration.student_collabration_platform.entity.IndustryProfile;
import illinoistech.itm.web.system.integration.student_collabration_platform.entity.StudentProfile;
import illinoistech.itm.web.system.integration.student_collabration_platform.entity.Users;
import illinoistech.itm.web.system.integration.student_collabration_platform.repository.IndustryProfileRepository;
import illinoistech.itm.web.system.integration.student_collabration_platform.repository.StudentProfileRepository;
import illinoistech.itm.web.system.integration.student_collabration_platform.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class IndustryProfileServiceImpl implements IndustryProfileService {
    private final IndustryProfileRepository industryProfileRepository;
    private final UserRepository userRepository;

    public IndustryProfileServiceImpl(IndustryProfileRepository industryProfileRepository, UserRepository userRepository) {
        this.industryProfileRepository = industryProfileRepository;
        this.userRepository = userRepository;
    }

    @Override
    public IndustryProfileDto getProfileForUser(UUID userId) {
        Users user = getUserOrThrow(userId);
        IndustryProfile profile = industryProfileRepository
                .findByUser_UserId(userId)
                .orElse(null); // may be null first time

        return toDto(user, profile);
    }

    private Users getUserOrThrow(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));
    }


    private IndustryProfileDto toDto(Users user, IndustryProfile profile) {
        IndustryProfileDto dto = new IndustryProfileDto();

        // always return user info
        dto.setUserId(user.getUserId());

        // profile may be null (first time)
        if (profile != null) {
            dto.setProfileId(profile.getProfileId());
            dto.setUserId(user.getUserId());
            dto.setCompanyName(profile.getCompanyName());
            dto.setPosition(profile.getPosition());
            dto.setCompanySize(profile.getCompanySize().toString());
            dto.setDescription(profile.getDescription());
            dto.setHeadquartersLocation(profile.getHeadquartersLocation());
            dto.setLinkedinUrl(profile.getLinkedinUrl());
            dto.setVerificationDocument(profile.getVerificationDocument());
            dto.setVerificationDocument(profile.getVerificationDocument());
            dto.setCreatedAt(profile.getCreatedAt());
            dto.setUpdatedAt(profile.getUpdatedAt());
        }

        return dto;
    }
}
