package se.apegroup.pizzaapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = ApiException.class)
    public ResponseEntity handleApiException(ApiException e) {
        switch (e.errorCode) {
            case 502:
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(e.getMessage());
            case 404:
                return  ResponseEntity.notFound().build();
            case 400:
                return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing request");
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity handleException(Exception e) {
        if (e.getMessage()!=null){
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing request");
    }
}
