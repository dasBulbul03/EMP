package com.example.ems.service;

import com.example.ems.dto.EmployeeRequestDto;
import com.example.ems.dto.PagedEmployeeResponse;
import com.example.ems.exception.EmailAlreadyExistsException;
import com.example.ems.exception.EmployeeNotFoundException;
import com.example.ems.model.Employee;
import com.example.ems.repository.EmployeeRepository;
import com.example.ems.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee(1L, "John", "Doe", "john.doe@example.com",
                "Developer", "Engineering", new BigDecimal("80000"), LocalDate.now());
    }

    @Test
    void getEmployees_returnsPagedResult() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Employee> page = new PageImpl<>(Collections.singletonList(employee), pageable, 1);
        when(employeeRepository.findAll(pageable)).thenReturn(page);

        PagedEmployeeResponse response = employeeService.getEmployees(null, pageable);

        assertEquals(1, response.getTotalElements());
        assertEquals(1, response.getContent().size());
        verify(employeeRepository, times(1)).findAll(pageable);
    }

    @Test
    void getEmployeeById_found() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        var dto = employeeService.getEmployeeById(1L);

        assertEquals("John", dto.getFirstName());
    }

    @Test
    void getEmployeeById_notFound_throws() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EmployeeNotFoundException.class, () -> employeeService.getEmployeeById(1L));
    }

    @Test
    void createEmployee_success() {
        EmployeeRequestDto request = buildRequestDto();
        when(employeeRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        var created = employeeService.createEmployee(request);

        assertNotNull(created.getId());
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    void createEmployee_duplicateEmail_throws() {
        EmployeeRequestDto request = buildRequestDto();
        when(employeeRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(employee));

        assertThrows(EmailAlreadyExistsException.class, () -> employeeService.createEmployee(request));
    }

    @Test
    void createEmployee_negativeSalary_throws() {
        EmployeeRequestDto request = buildRequestDto();
        request.setSalary(new BigDecimal("-1"));

        assertThrows(IllegalArgumentException.class, () -> employeeService.createEmployee(request));
    }

    @Test
    void updateEmployee_success() {
        EmployeeRequestDto request = buildRequestDto();
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        var updated = employeeService.updateEmployee(1L, request);

        assertEquals("John", updated.getFirstName());
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    void deleteEmployee_success() {
        when(employeeRepository.existsById(1L)).thenReturn(true);

        employeeService.deleteEmployee(1L);

        verify(employeeRepository).deleteById(1L);
    }

    @Test
    void deleteEmployee_notFound_throws() {
        when(employeeRepository.existsById(1L)).thenReturn(false);
        assertThrows(EmployeeNotFoundException.class, () -> employeeService.deleteEmployee(1L));
    }

    private EmployeeRequestDto buildRequestDto() {
        EmployeeRequestDto request = new EmployeeRequestDto();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("john.doe@example.com");
        request.setJobTitle("Developer");
        request.setDepartment("Engineering");
        request.setSalary(new BigDecimal("80000"));
        request.setHireDate(LocalDate.now());
        return request;
    }
}


