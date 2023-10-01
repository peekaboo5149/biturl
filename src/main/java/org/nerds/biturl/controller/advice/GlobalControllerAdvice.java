package org.nerds.biturl.controller.advice;

import lombok.extern.slf4j.Slf4j;
import org.nerds.biturl.expection.InternalServerError;
import org.nerds.biturl.expection.InvalidUrlException;
import org.nerds.biturl.expection.UrlNotFoundException;
import org.nerds.biturl.expection.UserAlreadyExistException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, Object> handleInvalidArgumentException(MethodArgumentNotValidException ex) {
        log.error("Encountered validation error ...", ex);
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("timestamp", new Date().toString());
        error.put("errorMessage", "Provided Invalid Arguments");
        Map<String, String> reason = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> reason.put(fieldError.getField(), fieldError.getDefaultMessage()));
        error.put("reason", reason);
        error.put("errorCode", 40001);
        return error;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidUrlException.class)
    public Map<String, Object> handleInvalidUrl(InvalidUrlException ex) {
        log.error("Encountered Invalid Url ...", ex);
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("timestamp", new Date().toString());
        error.put("errorMessage", "Provided an Invalid Url");
        error.put("reason", ex.getOriginalUrl() + ":[ERROR] => " + ex.getError());
        error.put("errorCode", 40002);
        return error;
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UrlNotFoundException.class)
    public Map<String, Object> handleUrlNotFound(UrlNotFoundException ex) {
        log.error("Encountered Url Not found ...", ex);
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("timestamp", new Date().toString());
        error.put("errorMessage", "Provided an Invalid Url");
        error.put("reason", ":[ERROR] => " + ex.getMessage());
        error.put("errorCode", 40002);
        return error;
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UserAlreadyExistException.class)
    public Map<String, Object> handleUserAlreadyExist(UserAlreadyExistException ex) {
        log.error("Encountered Url Not found ...", ex);
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("timestamp", new Date().toString());
        error.put("errorMessage", ex.getMessage());
        error.put("errorCode", 40901);
        return error;
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler({UsernameNotFoundException.class, AuthenticationException.class})
    public Map<String, Object> handleUserNameNotFound(Exception ex) {
        log.error("Authentication failure ...", ex);
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("timestamp", new Date().toString());
        error.put("errorMessage", "Authentication failure");
        error.put("reason", ex.getMessage());
        error.put("errorCode", 40901);
        return error;
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler({InternalServerError.class})
    public Map<String, Object> handleInternalServerError(InternalServerError ex) {
        log.error("Authentication failure ...", ex);
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("timestamp", new Date().toString());
        error.put("errorMessage", "Internal Server Error");
        error.put("reason", ex.getMessage());
        error.put("errorCode", 50001);
        return error;
    }
}
