package dev.perfectbogus.employeeapi.service;

import dev.perfectbogus.employeeapi.exception.EmployeeNotFoundException;
import dev.perfectbogus.employeeapi.model.Employee;
import dev.perfectbogus.employeeapi.repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository repository;

    @Transactional(readOnly = true)
    public List<Employee> getAll() {
        return repository.findAll();
    }

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
}
