package com.example.ems.data;

import com.example.ems.model.Employee;
import com.example.ems.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * Loads sample employees into the database on startup.
 */
@Component
public class SampleDataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(SampleDataLoader.class);

    private final EmployeeRepository employeeRepository;

    public SampleDataLoader(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void run(String... args) {
        if (employeeRepository.count() > 0) {
            return;
        }

        List<Employee> employees = Arrays.asList(
                new Employee(null, "Alice", "Johnson", "alice.johnson@example.com", "Developer", "Engineering",
                        new BigDecimal("80000"), LocalDate.now().minusYears(3)),
                new Employee(null, "Bob", "Smith", "bob.smith@example.com", "Manager", "Sales",
                        new BigDecimal("90000"), LocalDate.now().minusYears(5)),
                new Employee(null, "Charlie", "Brown", "charlie.brown@example.com", "Analyst", "Finance",
                        new BigDecimal("70000"), LocalDate.now().minusYears(2)),
                new Employee(null, "Diana", "Prince", "diana.prince@example.com", "HR Specialist", "HR",
                        new BigDecimal("65000"), LocalDate.now().minusYears(4)),
                new Employee(null, "Ethan", "Hunt", "ethan.hunt@example.com", "Security Lead", "Security",
                        new BigDecimal("95000"), LocalDate.now().minusYears(6)),
                new Employee(null, "Fiona", "Gallagher", "fiona.gallagher@example.com", "Designer", "Design",
                        new BigDecimal("72000"), LocalDate.now().minusYears(1)),
                new Employee(null, "George", "Martin", "george.martin@example.com", "Architect", "Engineering",
                        new BigDecimal("110000"), LocalDate.now().minusYears(7)),
                new Employee(null, "Hannah", "Lee", "hannah.lee@example.com", "QA Engineer", "Engineering",
                        new BigDecimal("68000"), LocalDate.now().minusYears(2)),
                new Employee(null, "Ian", "Wright", "ian.wright@example.com", "Support Engineer", "Support",
                        new BigDecimal("60000"), LocalDate.now().minusYears(3)),
                new Employee(null, "Julia", "Roberts", "julia.roberts@example.com", "Product Manager", "Product",
                        new BigDecimal("95000"), LocalDate.now().minusYears(4))
        );

        employeeRepository.saveAll(employees);
        log.info("Inserted {} sample employees", employees.size());
    }
}


