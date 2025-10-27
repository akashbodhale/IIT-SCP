package illinoistech.itm.web.system.integration.student_collabration_platform.controller;

import illinoistech.itm.web.system.integration.student_collabration_platform.dto.SignUpRequest;
import illinoistech.itm.web.system.integration.student_collabration_platform.dto.SignUpResponse;
import illinoistech.itm.web.system.integration.student_collabration_platform.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

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
        return "Home";
    }

    @PostMapping("/signup") public ResponseEntity<SignUpResponse> signUp(@Valid @RequestBody SignUpRequest request, UriComponentsBuilder uriBuilder )
    {
        SignUpResponse created = userService.register(request);
        var location = uriBuilder.path("/api/users/{id}").build(created.id());
        return ResponseEntity.created(location).body(created);
    }
//
//    @PostMapping("signin/{email}")
//    public  ResponseEntity<SignUpResponse> signIn(@Valid @RequestBody SignUpResponse request)
//    {
//        return  null;
//    }
}
