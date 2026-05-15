package com.example.ems.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * Exception handler for Thymeleaf views, returning a friendly error page.
 */
@ControllerAdvice
public class GlobalViewExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalViewExceptionHandler.class);

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ModelAndView handleEmployeeNotFound(EmployeeNotFoundException ex, Model model) {
        log.warn("Employee not found: {}", ex.getMessage());
        model.addAttribute("message", "The employee you're looking for doesn't exist. It may have been deleted.");
        ModelAndView mav = new ModelAndView("error");
        mav.addAllObjects(model.asMap());
        return mav;
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ModelAndView handleEmailAlreadyExists(EmailAlreadyExistsException ex, Model model) {
        log.warn("Email already exists: {}", ex.getMessage());
        model.addAttribute("message", "An employee with this email address already exists. Please use a different email.");
        ModelAndView mav = new ModelAndView("error");
        mav.addAllObjects(model.asMap());
        return mav;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ModelAndView handleIllegalArgument(IllegalArgumentException ex, Model model) {
        log.warn("Invalid argument: {}", ex.getMessage());
        model.addAttribute("message", ex.getMessage() != null ? ex.getMessage() : "Invalid data provided. Please check your input.");
        ModelAndView mav = new ModelAndView("error");
        mav.addAllObjects(model.asMap());
        return mav;
    }

    @ExceptionHandler(org.springframework.web.servlet.resource.NoResourceFoundException.class)
    public void handleNoResourceFound(org.springframework.web.servlet.resource.NoResourceFoundException ex) {
        // Ignore favicon and other static resource requests
        if (!ex.getResourcePath().equals("favicon.ico")) {
            log.debug("Resource not found: {}", ex.getResourcePath());
        }
        // Let it pass through - don't show error page for missing static resources
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception ex, Model model) {
        log.error("Unexpected error occurred", ex);
        model.addAttribute("message", 
            ex.getMessage() != null && !ex.getMessage().isEmpty() 
                ? ex.getMessage() 
                : "An unexpected error occurred. Please try again later.");
        ModelAndView mav = new ModelAndView("error");
        mav.addAllObjects(model.asMap());
        return mav;
    }
}


