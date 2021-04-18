package it.francescoforesti.dto.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.FieldError;

@Getter
@Setter
public class ValidationErrors {

    private Map<String, List<String>> errorMessages = new HashMap<>();

    public static ValidationErrors fromFieldErrors(List<FieldError> fieldErrors) {

        ValidationErrors dto = new ValidationErrors();
        if (fieldErrors != null) {
            fieldErrors.forEach(f -> dto.addError(f.getField(), f.getDefaultMessage()));
        }
        return dto;
    }

    private void addError(String field, String message) {
        List<String> messages = errorMessages.get(field);
        if (messages == null) {
            messages = new ArrayList<>();
        }
        messages.add(message);
        errorMessages.put(field, messages);
    }

}
