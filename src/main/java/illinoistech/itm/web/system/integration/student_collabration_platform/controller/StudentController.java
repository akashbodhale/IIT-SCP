package illinoistech.itm.web.system.integration.student_collabration_platform.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// Student controller
@Slf4j
@RestController
public class StudentController
{
    private static final Logger log = LoggerFactory.getLogger(StudentController.class);

    @GetMapping("/api/Student")
    public String sayHello()
    {
        log.info("Inside {} - sayHello method.", StudentController.class.getSimpleName());
        return "Hello from Student Collaboration Platform!";
    }
}
