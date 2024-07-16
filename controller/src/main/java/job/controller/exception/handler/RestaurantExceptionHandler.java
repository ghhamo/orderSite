package job.controller.exception.handler;

import job.service.exception.restaurantExceptions.RestaurantAlreadyExistsException;
import job.service.exception.restaurantExceptions.RestaurantNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@RestControllerAdvice
public class RestaurantExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({RestaurantAlreadyExistsException.class})
    protected ResponseEntity<Object> handleNameAlreadyExistsRequest(
            Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, "{\"errors\":{\"restaurant\": \"Restaurant already exist\"}}",
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({RestaurantNotFoundException.class})
    public ResponseEntity<Object> handleNameNotFoundRequest(Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, "{\"errors\":{\"restaurantName\": \"Restaurant not found\"}}",
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
}
