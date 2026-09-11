package dev.perfectbogus.employeeapi.service;

import dev.perfectbogus.employeeapi.exception.EmployeeNotFoundException;
import dev.perfectbogus.employeeapi.model.Employee;
import dev.perfectbogus.employeeapi.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository repository;

    public List<Employee> getAll() {
        return repository.findAll();
    }

    public Employee getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EmployeeNotFoundException("Employee not found: " + id));
    }

    @Transactional
    public Employee create(Employee employee) {
        return repository.save(employee);
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

}
