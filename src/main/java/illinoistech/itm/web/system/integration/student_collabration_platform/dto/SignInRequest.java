package illinoistech.itm.web.system.integration.student_collabration_platform.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignInRequest(@NotBlank @Email String email)
{

}
