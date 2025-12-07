package illinoistech.itm.web.system.integration.student_collabration_platform.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyUsedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleEmailInUse(EmailAlreadyUsedException ex) {
        return Map.of("error", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleValidation(MethodArgumentNotValidException ex) {
        var errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(java.util.stream.Collectors.toMap(
                        fe -> fe.getField(),
                        fe -> fe.getDefaultMessage(),
                        (a, b) -> a));
        return Map.of("error", "Validation failed", "fields", errors);
    }

    // 🔹 NEW: handle "already exists" / illegal state cases
    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT) // 409
    public Map<String, String> handleIllegalState(IllegalStateException ex) {
        // e.g. "Student profile already exists for user 6f59..."
        return Map.of("error", ex.getMessage());
    }

    // 🔹 NEW: handle not-found errors from service (EntityNotFoundException)
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) // 404
    public Map<String, String> handleEntityNotFound(EntityNotFoundException ex) {
        return Map.of("error", ex.getMessage());
    }
}
