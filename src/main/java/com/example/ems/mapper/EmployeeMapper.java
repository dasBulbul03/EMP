package com.example.ems.mapper;

import com.example.ems.dto.EmployeeRequestDto;
import com.example.ems.dto.EmployeeResponseDto;
import com.example.ems.model.Employee;

/**
 * Simple mapper for converting between Employee entities and DTOs.
 */
public final class EmployeeMapper {

    private EmployeeMapper() {
        // Utility class
    }

    public static Employee toEntity(EmployeeRequestDto dto) {
        if (dto == null) {
            return null;
        }
        Employee employee = new Employee();
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setEmail(dto.getEmail());
        employee.setJobTitle(dto.getJobTitle());
        employee.setDepartment(dto.getDepartment());
        employee.setSalary(dto.getSalary());
        employee.setHireDate(dto.getHireDate());
        return employee;
    }

    public static void updateEntity(Employee employee, EmployeeRequestDto dto) {
        if (employee == null || dto == null) {
            return;
        }
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setEmail(dto.getEmail());
        employee.setJobTitle(dto.getJobTitle());
        employee.setDepartment(dto.getDepartment());
        employee.setSalary(dto.getSalary());
        employee.setHireDate(dto.getHireDate());
    }

    public static EmployeeResponseDto toDto(Employee employee) {
        if (employee == null) {
            return null;
        }
        return new EmployeeResponseDto(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getJobTitle(),
                employee.getDepartment(),
                employee.getSalary(),
                employee.getHireDate()
        );
    }
}


