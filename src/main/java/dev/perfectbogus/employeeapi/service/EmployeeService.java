package dev.perfectbogus.employeeapi.service;

import dev.perfectbogus.employeeapi.dto.EmployeePatchRequest;
import dev.perfectbogus.employeeapi.dto.PageResponse;
import dev.perfectbogus.employeeapi.exception.EmployeeNotFoundException;
import dev.perfectbogus.employeeapi.model.Employee;
import dev.perfectbogus.employeeapi.repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository repository;

    @Transactional(readOnly = true)
    public Employee getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    @Transactional
    public Employee create(Employee employee) {
        return repository.save(employee);
    }

    @Transactional
    public void delete(Long id) {
        getById(id);
        repository.deleteById(id);
    }

    @Transactional
    public Employee update(Long id, Employee employee) {
        Employee existing = getById(id);

        existing.setDepartment(employee.getDepartment());
        existing.setName(employee.getName());
        existing.setSalary(employee.getSalary());

        return repository.save(existing);
    }

    @Transactional
    public Employee patch(Long id, EmployeePatchRequest request) {
        Employee existing = getById(id);

        if (request.name() != null) existing.setName(request.name());
        if (request.department() != null) existing.setDepartment(request.department());
        if (request.salary() != null) existing.setSalary(request.salary());

        return repository.save(existing);
    }

    @Transactional(readOnly = true)
    public PageResponse<Employee> filter(
            String department,
            Double minSalary,
            Double maxSalary,
            Pageable pageable) {
        boolean hasDept = department != null;
        boolean hasMin = minSalary != null;
        boolean hasMax = maxSalary != null;

        double min = hasMin ? minSalary : 0.0;
        double max = hasMax ? maxSalary : Double.MAX_VALUE;

        Page<Employee> page;
        if (!hasDept && !hasMin && !hasMax) {
            page = repository.findAll(pageable);
        } else if ( hasDept && !hasMin && !hasMax) {
            page = repository.findByDepartmentIgnoreCase(department, pageable);
        } else if (!hasDept) {
            page = repository.findBySalaryBetween(min, max, pageable);
        } else {
            page = repository.findByDepartmentIgnoreCaseAndSalaryBetween(department, min, max, pageable);
        }

        return toPageResponse(page);
    }

    private PageResponse<Employee> toPageResponse(Page<Employee> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
    }
}
