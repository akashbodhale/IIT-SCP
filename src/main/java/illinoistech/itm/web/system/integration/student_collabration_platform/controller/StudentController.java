package illinoistech.itm.web.system.integration.student_collabration_platform.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StudentController {

    @GetMapping("/api/Student")
    public String sayHello() {
        return "Hello from Student Collaboration Platform!";
    }
}