package dev.perfectbogus.employeeapi.controller;

import dev.perfectbogus.employeeapi.dto.EmployeePatchRequest;
import dev.perfectbogus.employeeapi.dto.PageResponse;
import dev.perfectbogus.employeeapi.model.Employee;
import dev.perfectbogus.employeeapi.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employee create(@Valid @RequestBody Employee employee) {
        return service.create(employee);
    }

    @GetMapping
    public PageResponse<Employee> getAll(
            @RequestParam(required = false) String department,
            @RequestParam(required = false) Double minSalary,
            @RequestParam(required = false) Double maxSalary,
            Pageable pageable
    ) {
        return service.filter(department, minSalary, maxSalary, pageable);
    }

    @GetMapping("/{id}")
    public Employee getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public Employee update(@PathVariable Long id, @RequestBody Employee employee) {
        return service.update(id, employee);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @PatchMapping("/{id}")
    public Employee patch(@PathVariable Long id, @RequestBody EmployeePatchRequest request) {
        return service.patch(id, request);
    }
}
