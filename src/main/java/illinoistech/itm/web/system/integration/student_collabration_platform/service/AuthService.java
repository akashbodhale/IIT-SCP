package illinoistech.itm.web.system.integration.student_collabration_platform.service;

import illinoistech.itm.web.system.integration.student_collabration_platform.dto.SignInResponse;
import illinoistech.itm.web.system.integration.student_collabration_platform.dto.SignUpResponse;
import illinoistech.itm.web.system.integration.student_collabration_platform.dto.SignUpRequest;
import illinoistech.itm.web.system.integration.student_collabration_platform.entity.IndustryProfile;
import illinoistech.itm.web.system.integration.student_collabration_platform.entity.StudentProfile;
import illinoistech.itm.web.system.integration.student_collabration_platform.entity.Users;
import illinoistech.itm.web.system.integration.student_collabration_platform.exception.EmailAlreadyUsedException;
import illinoistech.itm.web.system.integration.student_collabration_platform.repository.IndustryProfileRepository;
import illinoistech.itm.web.system.integration.student_collabration_platform.repository.StudentProfileRepository;
import illinoistech.itm.web.system.integration.student_collabration_platform.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;


@Slf4j
@Service
public class AuthService {


    private final UserRepository repo;
    private final StudentProfileRepository stdrepo;
    private final IndustryProfileRepository indrepo;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository repo, PasswordEncoder passwordEncoder, StudentProfileRepository stdrepo, IndustryProfileRepository indrepo) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
        this.stdrepo = stdrepo;
        this.indrepo = indrepo;
    }


    @Transactional
    public SignUpResponse register(SignUpRequest req) {

        String email = req.getEmail().trim().toLowerCase();
        if (repo.existsByEmail(email)) {
            throw new EmailAlreadyUsedException(email);
        }

        Users user = new Users();
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        user.setFirstName(req.getFirstName().trim());
        user.setLastName(req.getLastName().trim());
        user.setUserType(req.getUserType().trim());
//      user.getRoles().add("USER");
        Users saved = repo.save(user);

        if (saved.getUserType().equalsIgnoreCase("Student")) {

            StudentProfile profile = new StudentProfile();
            profile.setUser(saved);
            profile.setUniversity(req.getUniversity());
            profile.setstudentId(req.getStudentId());
            profile.setMajor(req.getMajor());

            // Convert String → Enum (FRESHMAN, SOPHOMORE, JUNIOR...)
            profile.setAcademicYear(
                    StudentProfile.AcademicYear.valueOf(req.getAcademicYear().toUpperCase())
            );

            // Convert String → LocalDate
            if (req.getExpectedGraduation() != null) {
                profile.setExpectedGraduation(LocalDate.parse(req.getExpectedGraduation()));
            }

            stdrepo.save(profile);
        }

        if (saved.getUserType().equalsIgnoreCase("Industry"))
        {
            IndustryProfile profile = new IndustryProfile();
            profile.setUser(saved);
            profile.setCompanyName(req.getCompanyName());
            profile.setPosition(req.getPosition());

            indrepo.save(profile);
        }


        return new SignUpResponse(saved.getUserId(), saved.getEmail(), saved.getFirstName(), saved.getLastName());
    }

    @Transactional(readOnly = true)
    public SignInResponse signInByEmail(String email)
    {
        var user = repo.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UserNotFoundException(email));
        String major = stdrepo.findByUser_UserId(user.getUserId())
                .map(StudentProfile::getMajor)
                .orElse(null);

        return new SignInResponse(user.getId(),user.getUserId(), user.getEmail(),user.getUserType(),user.getFirstName(),user.getLastName(), major);
    }

    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String email) {
            super("User not found for email:" + email);
        }
    }

}
