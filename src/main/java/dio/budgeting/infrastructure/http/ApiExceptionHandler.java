package dio.budgeting.infrastructure.http;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler({IllegalArgumentException.class, MethodArgumentNotValidException.class})
    ProblemDetail badRequest(Exception exception) {
        var detail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, message(exception));
        detail.setTitle("Requisição inválida");
        return detail;
    }

    private String message(Exception exception) {
        if (exception instanceof MethodArgumentNotValidException validation) {
            return validation.getBindingResult().getFieldErrors().stream()
                    .findFirst()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .orElse("Dados inválidos");
        }
        return exception.getMessage();
    }
}
