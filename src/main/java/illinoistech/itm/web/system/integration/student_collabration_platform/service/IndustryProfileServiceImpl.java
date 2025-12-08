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

    @Override
    public IndustryProfileDto editProfileForUser(UUID userId, IndustryProfileDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("IndustryProfileDto must not be null");
        }

        // 1) Make sure user exists
        Users user = getUserOrThrow(userId);

        // 2) Find existing profile – DO NOT create a new one
        IndustryProfile profile = industryProfileRepository
                .findByUser_UserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Industry profile not found for user: " + userId
                ));

        // 3) Map fields from DTO → entity (adjust names to your DTO/entity)
        profile.setCompanyName(dto.getCompanyName());
        profile.setCompanyName(dto.getCompanyName());
        profile.setPosition(dto.getPosition());
        profile.setCompanySize(IndustryProfile.companySize.SMALL);
        profile.setDescription(dto.getDescription());
        profile.setHeadquartersLocation(dto.getHeadquartersLocation());
        profile.setLinkedinUrl(dto.getLinkedinUrl());
        profile.setVerificationDocument(dto.getVerificationDocument());
        profile.setVerificationDocument(dto.getVerificationDocument());
        // profile.setCreatedAt(dto.getCreatedAt());
        // profile.setUpdatedAt(dto.getUpdatedAt());
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        profile.setCreatedAt(now);
        profile.setUpdatedAt(now);
        // ... any other fields you have

        // 4) Save updated entity
        IndustryProfile saved = industryProfileRepository.save(profile);

        // 5) Return DTO
        return toDto(user, saved);
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
