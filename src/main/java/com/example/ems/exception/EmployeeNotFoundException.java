package com.example.ems.exception;

/**
 * Thrown when an employee is not found.
 */
public class EmployeeNotFoundException extends RuntimeException {

    public EmployeeNotFoundException(Long id) {
        super("Employee not found with id: " + id);
    }
}


