package illinoistech.itm.web.system.integration.student_collabration_platform.service;

import illinoistech.itm.web.system.integration.student_collabration_platform.dto.SignUpResponse;
import illinoistech.itm.web.system.integration.student_collabration_platform.dto.SignUpRequest;
import illinoistech.itm.web.system.integration.student_collabration_platform.entity.Users;
import illinoistech.itm.web.system.integration.student_collabration_platform.exception.EmailAlreadyUsedException;
import illinoistech.itm.web.system.integration.student_collabration_platform.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
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

        return new SignUpResponse(saved.getId(), saved.getEmail(), saved.getFirstName(), saved.getLastName());
    }
}
