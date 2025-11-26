package com.example.ems.exception;

/**
 * Thrown when an employee email already exists.
 */
public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String email) {
        super("Employee with email already exists: " + email);
    }
}


