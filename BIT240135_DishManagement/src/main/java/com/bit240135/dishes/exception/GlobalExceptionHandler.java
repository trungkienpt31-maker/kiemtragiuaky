package com.bit240135.dishes.exception;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public String handleNotFound(NotFoundException ex, Model model, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        model.addAttribute("messageKey", ex.getMessageKey());
        return "error";
    }

    /** Id không phải số, ví dụ /dishes/abc */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public String handleBadArgument(Model model, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        model.addAttribute("messageKey", "error.invalid.id");
        return "error";
    }
}
