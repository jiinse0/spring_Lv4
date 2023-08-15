package com.sparta.spring_lv4.exception;

import com.sparta.spring_lv4.dto.StatusResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/*
* @RestControllerAdvice는 Spring에서 제공하는 AOP 기능을 활용하여 예외 처리를 통합적으로 관리하는 방법
* 여러 컨트롤러에서 발생하는 예외를 한 곳에서 효과적으로 처리할 수 있다.
* @RestControllerAdvice = @ControllerAdvice + @ResponseBody
*/
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<StatusResponseDto> illegalArgumentExceptionHandler(IllegalArgumentException ex) {
        StatusResponseDto statusResponseDto = new StatusResponseDto(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(
                statusResponseDto,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler({NullPointerException.class})
    public ResponseEntity<StatusResponseDto> nullPointerExceptionHandler(NullPointerException ex) {
        StatusResponseDto statusResponseDto = new StatusResponseDto(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(
                statusResponseDto,
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<StatusResponseDto> notFoundProductExceptionHandler(NotFoundException ex) {
        StatusResponseDto statusResponseDto = new StatusResponseDto(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(
                statusResponseDto,
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler({LikeException.class})
    public ResponseEntity<StatusResponseDto> notFoundProductExceptionHandler(LikeException ex) {
        StatusResponseDto statusResponseDto = new StatusResponseDto(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(
                statusResponseDto,
                HttpStatus.NOT_FOUND
        );
    }
}