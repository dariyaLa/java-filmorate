package ru.yandex.practicum.filmorate.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class ExpHandler {

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> exeptionHandle(final ExceptionNotFound e) {
        return new ResponseEntity<Map<String, String>>(
                Map.of("messageError", e.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }
}
