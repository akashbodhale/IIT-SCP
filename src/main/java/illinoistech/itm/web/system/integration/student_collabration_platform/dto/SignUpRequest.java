package illinoistech.itm.web.system.integration.student_collabration_platform.dto;
import jakarta.validation.constraints.*;

public class SignUpRequest {

    @Email
    @NotBlank
    @Size(max = 254)
    private String email;

    // BCrypt max input we store is 72 chars
    @NotBlank
    @Size(min = 8, max = 72)
    private String password;

    @NotBlank
    @Size(max = 60)
    private String firstName;

    @NotBlank
    @Size(max = 60)
    private String lastName;

    // Matches your DB "user_type" (e.g., Student/Industry/Faculty/Admin, etc.)
    @NotBlank
    @Size(max = 255)
    private String userType;

    // Optional — aligns with "phone" (varchar(20)); keep simple digits/+,-
    @Size(max = 20)
    @Pattern(regexp = "^[0-9+\\-()\\s]*$", message = "Phone may contain digits, spaces, +, -, ( )")
    private String phone;



    // --- Student Profile fields (only for Student users) ---
    private String university;
    private String studentId;
    private String major;
    private String academicYear;      // String → will convert to enum
    private String expectedGraduation; // LocalDate as text "2026-12-15"



    // --- getters & setters ---

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserType() {
        return userType;
    }
    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getUniversity() { return university; }
    public void setUniversity(String university) { this.university = university; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }

    public String getAcademicYear() { return academicYear; }
    public void setAcademicYear(String academicYear) { this.academicYear = academicYear; }

    public String getExpectedGraduation() { return expectedGraduation; }
    public void setExpectedGraduation(String expectedGraduation) { this.expectedGraduation = expectedGraduation; }

}
