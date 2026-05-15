package com.example.ems.service;

import com.example.ems.dto.EmployeeRequestDto;
import com.example.ems.dto.PagedEmployeeResponse;
import com.example.ems.dto.EmployeeResponseDto;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for managing employees.
 */
public interface EmployeeService {

    PagedEmployeeResponse getEmployees(String search, Pageable pageable);

    EmployeeResponseDto getEmployeeById(Long id);

    EmployeeResponseDto createEmployee(EmployeeRequestDto requestDto);

    EmployeeResponseDto updateEmployee(Long id, EmployeeRequestDto requestDto);

    void deleteEmployee(Long id);
}


