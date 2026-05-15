package com.example.ems.service.impl;

import com.example.ems.dto.EmployeeRequestDto;
import com.example.ems.dto.EmployeeResponseDto;
import com.example.ems.dto.PagedEmployeeResponse;
import com.example.ems.exception.EmailAlreadyExistsException;
import com.example.ems.exception.EmployeeNotFoundException;
import com.example.ems.mapper.EmployeeMapper;
import com.example.ems.model.Employee;
import com.example.ems.repository.EmployeeRepository;
import com.example.ems.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of EmployeeService.
 */
@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public PagedEmployeeResponse getEmployees(String search, Pageable pageable) {
        Page<Employee> page;
        if (search != null && !search.isBlank()) {
            String term = search.trim();
            page = employeeRepository
                    .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                            term, term, term, pageable);
        } else {
            page = employeeRepository.findAll(pageable);
        }

        List<EmployeeResponseDto> dtos = page.getContent()
                .stream()
                .map(EmployeeMapper::toDto)
                .collect(Collectors.toList());

        return new PagedEmployeeResponse(
                dtos,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponseDto getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
        return EmployeeMapper.toDto(employee);
    }

    @Override
    public EmployeeResponseDto createEmployee(EmployeeRequestDto requestDto) {
        validateSalary(requestDto.getSalary());
        ensureEmailUnique(requestDto.getEmail(), null);
        Employee employee = EmployeeMapper.toEntity(requestDto);
        Employee saved = employeeRepository.save(employee);
        log.info("Created employee with id {}", saved.getId());
        return EmployeeMapper.toDto(saved);
    }

    @Override
    public EmployeeResponseDto updateEmployee(Long id, EmployeeRequestDto requestDto) {
        validateSalary(requestDto.getSalary());
        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
        ensureEmailUnique(requestDto.getEmail(), id);
        EmployeeMapper.updateEntity(existing, requestDto);
        Employee saved = employeeRepository.save(existing);
        log.info("Updated employee with id {}", saved.getId());
        return EmployeeMapper.toDto(saved);
    }

    @Override
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new EmployeeNotFoundException(id);
        }
        employeeRepository.deleteById(id);
        log.info("Deleted employee with id {}", id);
    }

    private void ensureEmailUnique(String email, Long currentEmployeeId) {
        employeeRepository.findByEmail(email)
                .ifPresent(existing -> {
                    if (currentEmployeeId == null || !existing.getId().equals(currentEmployeeId)) {
                        throw new EmailAlreadyExistsException(email);
                    }
                });
    }

    private void validateSalary(BigDecimal salary) {
        if (salary != null && salary.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Salary must be non-negative");
        }
    }
}


