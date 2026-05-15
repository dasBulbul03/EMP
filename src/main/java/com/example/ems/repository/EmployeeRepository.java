package com.example.ems.repository;

import com.example.ems.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Employee entity.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    /**
     * Find an employee by email.
     *
     * @param email the email address
     * @return an Optional containing the employee if found
     */
    Optional<Employee> findByEmail(String email);

    /**
     * Search for employees by first name, last name, or email (case-insensitive).
     *
     * @param firstName the first name search term
     * @param lastName  the last name search term
     * @param email     the email search term
     * @param pageable  pagination information
     * @return a page of employees matching the search criteria
     */
    Page<Employee> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            String firstName, String lastName, String email, Pageable pageable);
}
