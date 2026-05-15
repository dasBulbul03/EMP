package com.example.ems.controller;

import com.example.ems.dto.EmployeeRequestDto;
import com.example.ems.dto.PagedEmployeeResponse;
import com.example.ems.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * Controller serving Thymeleaf pages for employee management.
 */
@Controller
@RequestMapping("/employees")
public class EmployeeViewController {

    private final EmployeeService employeeService;

    public EmployeeViewController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public String listEmployees(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size,
                                @RequestParam(defaultValue = "id,asc") String sort,
                                @RequestParam(required = false) String search,
                                Model model) {
        String[] sortParams = sort.split(",");
        String sortBy = sortParams[0];
        Sort.Direction direction = sortParams.length > 1 && "desc".equalsIgnoreCase(sortParams[1])
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        PagedEmployeeResponse response = employeeService.getEmployees(search, pageable);
        model.addAttribute("employeesPage", response);
        model.addAttribute("currentPage", page);
        model.addAttribute("search", search);
        model.addAttribute("sort", sort);
        return "index";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("employee", new EmployeeRequestDto());
        model.addAttribute("formTitle", "Create Employee");
        return "employee-form";
    }

    @PostMapping
    public String createEmployee(@Valid @ModelAttribute("employee") EmployeeRequestDto employeeRequestDto,
                                 BindingResult bindingResult,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("formTitle", "Create Employee");
            return "employee-form";
        }
        try {
            employeeService.createEmployee(employeeRequestDto);
            return "redirect:/employees";
        } catch (Exception ex) {
            model.addAttribute("formTitle", "Create Employee");
            model.addAttribute("error", ex.getMessage());
            return "employee-form";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        try {
            var existing = employeeService.getEmployeeById(id);
            EmployeeRequestDto dto = new EmployeeRequestDto();
            dto.setFirstName(existing.getFirstName());
            dto.setLastName(existing.getLastName());
            dto.setEmail(existing.getEmail());
            dto.setJobTitle(existing.getJobTitle());
            dto.setDepartment(existing.getDepartment());
            dto.setSalary(existing.getSalary());
            dto.setHireDate(existing.getHireDate());

            model.addAttribute("employee", dto);
            model.addAttribute("employeeId", id);
            model.addAttribute("formTitle", "Edit Employee");
            return "employee-form";
        } catch (Exception ex) {
            model.addAttribute("message", ex.getMessage());
            return "error";
        }
    }

    @PostMapping("/{id}")
    public String updateEmployee(@PathVariable Long id,
                                 @Valid @ModelAttribute("employee") EmployeeRequestDto employeeRequestDto,
                                 BindingResult bindingResult,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("formTitle", "Edit Employee");
            model.addAttribute("employeeId", id);
            return "employee-form";
        }
        try {
            employeeService.updateEmployee(id, employeeRequestDto);
            return "redirect:/employees";
        } catch (Exception ex) {
            model.addAttribute("formTitle", "Edit Employee");
            model.addAttribute("employeeId", id);
            model.addAttribute("error", ex.getMessage());
            return "employee-form";
        }
    }

    @GetMapping("/{id}")
    public String viewEmployee(@PathVariable Long id, Model model) {
        try {
            var employee = employeeService.getEmployeeById(id);
            model.addAttribute("employee", employee);
            return "employee-view";
        } catch (Exception ex) {
            model.addAttribute("message", ex.getMessage());
            return "error";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteEmployee(@PathVariable Long id, Model model) {
        try {
            employeeService.deleteEmployee(id);
            return "redirect:/employees";
        } catch (Exception ex) {
            model.addAttribute("message", ex.getMessage());
            return "error";
        }
    }
}


