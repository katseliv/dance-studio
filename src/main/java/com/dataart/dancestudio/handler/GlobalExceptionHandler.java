package com.dataart.dancestudio.handler;

import com.dataart.dancestudio.exception.BookingException;
import com.dataart.dancestudio.exception.LessonException;
import com.dataart.dancestudio.exception.UserAlreadyExistsException;
import com.dataart.dancestudio.exception.UserCanNotBeDeletedException;
import com.dataart.dancestudio.model.dto.ApiErrorDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.security.auth.message.AuthException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = AuthException.class)
    public ResponseEntity<ApiErrorDto> authException(final AuthException authException) {
        final ApiErrorDto apiErrorDto = ApiErrorDto.builder()
                .status(String.valueOf(HttpStatus.UNAUTHORIZED.value()))
                .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .messages(List.of(authException.getMessage()))
                .build();
        return new ResponseEntity<>(apiErrorDto, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {
            BookingException.class, LessonException.class,
            UserCanNotBeDeletedException.class, UserAlreadyExistsException.class
    })
    public ResponseEntity<ApiErrorDto> badRequestException(final RuntimeException runtimeException) {
        final ApiErrorDto apiErrorDto = ApiErrorDto.builder()
                .status(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .messages(List.of(runtimeException.getMessage()))
                .build();
        return new ResponseEntity<>(apiErrorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {NoSuchElementException.class, UsernameNotFoundException.class})
    public ResponseEntity<ApiErrorDto> notFoundException(final RuntimeException runtimeException) {
        final ApiErrorDto apiErrorDto = ApiErrorDto.builder()
                .status(String.valueOf(HttpStatus.NOT_FOUND.value()))
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .messages(List.of(runtimeException.getMessage()))
                .build();
        return new ResponseEntity<>(apiErrorDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiErrorDto> backendException(final Exception exception) {
        final ApiErrorDto apiErrorDto = ApiErrorDto.builder()
                .status(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .messages(List.of(exception.getMessage()))
                .build();
        return new ResponseEntity<>(apiErrorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
                                                                  @NonNull final HttpHeaders headers,
                                                                  @NonNull final HttpStatus status,
                                                                  @NonNull final WebRequest request) {
        final List<String> details = new ArrayList<>();
        for (final ObjectError error : ex.getBindingResult().getAllErrors()) {
            details.add(error.getDefaultMessage());
        }
        final ApiErrorDto error = ApiErrorDto.builder()
                .status(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .messages(details)
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}
