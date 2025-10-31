package illinoistech.itm.web.system.integration.student_collabration_platform.dto;
import java.util.UUID;

public record SignInResponse(UUID id, String email,String userType,String firstName,String lastName,String major)
{

}

