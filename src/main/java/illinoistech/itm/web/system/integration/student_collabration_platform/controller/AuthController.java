package illinoistech.itm.web.system.integration.student_collabration_platform.controller;

import illinoistech.itm.web.system.integration.student_collabration_platform.dto.SignInRequest;
import illinoistech.itm.web.system.integration.student_collabration_platform.dto.SignInResponse;
import illinoistech.itm.web.system.integration.student_collabration_platform.dto.SignUpRequest;
import illinoistech.itm.web.system.integration.student_collabration_platform.dto.SignUpResponse;
import illinoistech.itm.web.system.integration.student_collabration_platform.service.AuthService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService userService;

    public AuthController(AuthService userService)
    {
        this.userService = userService;
    }

    @GetMapping("/home")
    public String home()
    {
        log.info("Inside {} - Home method.", AuthController.class.getSimpleName());
        return "Home";
    }

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponse> signUp(@Valid @RequestBody SignUpRequest request, UriComponentsBuilder uriBuilder )
    {
        log.info("Inside {} - Signup method.", AuthController.class.getSimpleName());
        SignUpResponse created = userService.register(request);
        var location = uriBuilder.path("/api/users/{id}").build(created.id());
        return ResponseEntity.created(location).body(created);
    }


    @PostMapping("/signin")
    public ResponseEntity<SignInResponse> signIn(@Valid @RequestBody SignInRequest request) {
        log.info("Sign-in API called for email={}", request.email());
        var resp = userService.signInByEmail(request.email());
        return ResponseEntity.ok(resp);
    }

}
