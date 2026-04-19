package br.edu.ifsul.sapucaia.projeto.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashMap;
import java.util.Map;

import static java.time.LocalTime.now;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatusException(ResponseStatusException ex,
                                                                             HttpServletRequest request) {

        HttpStatusCode statusCode = ex.getStatusCode();

        String errorPhrase = "Error";
        if (statusCode instanceof HttpStatus status) {
            errorPhrase = status.getReasonPhrase();
        }

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", now());
        body.put("status", statusCode.value());
        body.put("error", errorPhrase);
        body.put("message", ex.getReason());
        body.put("path", request.getServletPath());

        return new ResponseEntity<>(body, statusCode);
    }
}
