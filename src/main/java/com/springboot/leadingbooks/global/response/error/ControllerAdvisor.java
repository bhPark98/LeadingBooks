package com.springboot.leadingbooks.global.response.error;

import com.springboot.leadingbooks.controller.dto.request.LoginRequestDto;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    protected ModelAndView handleBindException(MethodArgumentNotValidException e, Model model) {
        log.error("[Error] 에러 발생 ! errorCode : {}, errorMessage : {}",e.getMessage());
        List<String> errorList = e.getFieldErrors().stream().map(
            b -> b.getField() + " : " +b.getDefaultMessage()
        ).toList();
        model.addAttribute("errorMessages", errorList);
        return new ModelAndView("members/createMemberForm", model.asMap());
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    protected ModelAndView handleConstraintException(ConstraintViolationException e, Model model) {
        log.error("[Error] 에러 발생 ! errorCode : {}, errorMessage : {}",e.getMessage());

        ModelAndView mav = new ModelAndView("/members/createMemberForm");
        mav.addObject("errorMessage", e.getMessage());
        return mav;
    }

    @ExceptionHandler(value = CustomException.class)
    protected ResponseEntity<?> customExceptionHandler(CustomException e) {
        log.error("Error occurred in controller advice! errorCode: {}, errorMessage: {}", e.getErrorCode(), e.getMessage());
        ErrorCode errorCode = e.getErrorCode();
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                errorCode.getHttpStatus(),
                errorCode.getCode(),
                errorCode.getMessage()
        );
        log.error("status = {}, code = {}, message = {}", errorResponseDto.getStatus(), errorResponseDto.getCode(), errorResponseDto.getMessage());

        return new ResponseEntity<>(errorResponseDto, errorCode.getHttpStatus());

    }
}
