package illinoistech.itm.web.system.integration.student_collabration_platform.exception;

import java.time.OffsetDateTime;

public class ApiError {

    private int status;
    private String error;
    private String message;
    private String path;
    private OffsetDateTime timestamp;

    public ApiError() {
    }

    public ApiError(int status, String error, String message, String path) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.timestamp = OffsetDateTime.now();
    }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }

    public OffsetDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(OffsetDateTime timestamp) { this.timestamp = timestamp; }
}
