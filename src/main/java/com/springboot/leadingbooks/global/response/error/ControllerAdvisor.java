package com.springboot.leadingbooks.global.response.error;

import com.springboot.leadingbooks.controller.dto.request.LoginRequestDto;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
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
@ControllerAdvice
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
    protected ModelAndView customExceptionHandler(CustomException e) {
        log.error("Error occurred in controller advice! errorCode: {}, errorMessage: {}", e.getErrorCode(), e.getMessage());

        ModelAndView mav = new ModelAndView("/members/login");
        mav.addObject("global", e.getMessage());
        // 로그인 관련 오류인 경우에만 LoginRequestDto 추가
        if (e.getErrorCode() == ErrorCode.NOT_AUTHORIZED ||
                e.getErrorCode() == ErrorCode.NOT_MATCH_PASSWORD ||
                e.getErrorCode() == ErrorCode.NOT_FOUND_EMAIL) {
            mav.addObject("loginRequestDto", new LoginRequestDto());
        }
        return mav;
    }
}
