package com.dataart.dancestudio.handler;

import com.dataart.dancestudio.exception.*;
import com.dataart.dancestudio.model.dto.ApiErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.security.auth.message.AuthException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = AuthException.class)
    public ResponseEntity<ApiErrorDto> authException(final AuthException authException) {
        final ApiErrorDto apiErrorDto = ApiErrorDto.builder()
                .status(String.valueOf(HttpStatus.UNAUTHORIZED.value()))
                .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .messages(List.of(authException.getMessage()))
                .build();
        log.error(authException.getMessage(), authException);
        return new ResponseEntity<>(apiErrorDto, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {
            EntityCreationException.class, EntityAlreadyExistsException.class, UserCanNotBeDeletedException.class,
            ParseInputException.class, DecodeJwtTokenException.class
    })
    public ResponseEntity<ApiErrorDto> badRequestException(final RuntimeException runtimeException) {
        final ApiErrorDto apiErrorDto = ApiErrorDto.builder()
                .status(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .messages(List.of(runtimeException.getMessage()))
                .build();
        log.error(runtimeException.getMessage(), runtimeException);
        return new ResponseEntity<>(apiErrorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<ApiErrorDto> badCredentialsException(final RuntimeException runtimeException) {
        final ApiErrorDto apiErrorDto = ApiErrorDto.builder()
                .status(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .messages(List.of("Invalid email or password!"))
                .build();
        log.error(runtimeException.getMessage(), runtimeException);
        return new ResponseEntity<>(apiErrorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {EntityNotFoundException.class, UsernameNotFoundException.class})
    public ResponseEntity<ApiErrorDto> notFoundException(final RuntimeException runtimeException) {
        final ApiErrorDto apiErrorDto = ApiErrorDto.builder()
                .status(String.valueOf(HttpStatus.NOT_FOUND.value()))
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .messages(List.of(runtimeException.getMessage()))
                .build();
        log.error(runtimeException.getMessage(), runtimeException);
        return new ResponseEntity<>(apiErrorDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = GoogleResponseException.class)
    public ResponseEntity<ApiErrorDto> googleHttpException(final Exception exception) {
        final ApiErrorDto apiErrorDto = ApiErrorDto.builder()
                .status(String.valueOf(HttpStatus.BAD_GATEWAY.value()))
                .error(HttpStatus.BAD_GATEWAY.getReasonPhrase())
                .messages(List.of("Oops... Something went wrong!"))
                .build();
        log.error(exception.getMessage(), exception);
        return new ResponseEntity<>(apiErrorDto, HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiErrorDto> backendException(final Exception exception) {
        final ApiErrorDto apiErrorDto = ApiErrorDto.builder()
                .status(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .messages(List.of("Oops... Something went wrong!"))
                .build();
        log.error(exception.getMessage(), exception);
        return new ResponseEntity<>(apiErrorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @NonNull
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException exception,
                                                                  @NonNull final HttpHeaders headers,
                                                                  @NonNull final HttpStatus status,
                                                                  @NonNull final WebRequest request) {
        final List<String> details = exception.getBindingResult().getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.toList());
        final ApiErrorDto error = ApiErrorDto.builder()
                .status(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .messages(details)
                .build();
        log.error(exception.getMessage(), exception);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @NonNull
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(final HttpMessageNotReadableException exception,
                                                                  @NonNull final HttpHeaders headers,
                                                                  @NonNull final HttpStatus status,
                                                                  @NonNull final WebRequest request) {
        final String invalidInputMessage = "Invalid input.";
        final String exceptionMessage = Optional.ofNullable(exception.getRootCause())
                .map(Throwable::getMessage)
                .map(message -> message.split("at"))
                .map(split -> split[0])
                .map(String::trim)
                .map(s -> s.isBlank() ? "Invalid input" : s)
                .map(s -> s + ".")
                .orElse(invalidInputMessage);

        final ApiErrorDto error = ApiErrorDto.builder()
                .status(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .messages(List.of(exceptionMessage))
                .build();
        log.error(exception.getMessage(), exception);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}
