package job.controller.exception.handler;

import job.service.exception.productExceptions.ProductAlreadyExistsException;
import job.service.exception.productExceptions.ProductNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ProductExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ProductAlreadyExistsException.class})
    public ResponseEntity<Object> handleIdAlreadyExistsRequest(
            Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, "{\"errors\":{\"product\": \"Product already exists\"}}",
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({ProductNotFoundException.class})
    public ResponseEntity<Object> handleIdNotFoundRequest(Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, "{\"errors\":{\"product\": \"Product not found\"}}",
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
}
