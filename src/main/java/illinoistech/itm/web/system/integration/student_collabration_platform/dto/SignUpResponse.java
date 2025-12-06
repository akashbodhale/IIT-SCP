package illinoistech.itm.web.system.integration.student_collabration_platform.dto;

import java.util.UUID;

public record SignUpResponse(UUID userId, String email, String firstName, String lastName)
{

}
