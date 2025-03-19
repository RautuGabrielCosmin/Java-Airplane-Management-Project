package service;

import domain.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.EmployeeRepository;

import java.util.List;

public class MemoryEmployeeService implements EmployeeService {
    private static final Logger logger = LoggerFactory.getLogger(MemoryEmployeeService.class);

    private final EmployeeRepository employeeRepository;
    public MemoryEmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee createEmployee(int id, String name) {
        logger.info("Creating employee with id={} and name={}", id, name);
        if (name == null || name.trim().isEmpty()) {
            logger.warn("Cannot create employee - provided name is blank.");
            throw new IllegalArgumentException("Employee name must not be blank.");
        }
        Employee existing = employeeRepository.findById(id);
        if (existing != null) {
            logger.warn("Cannot create employee - id={} is already in use by {}",
                    id, existing);
            throw new IllegalStateException("Employee with id=" + id + " already exists.");
        }
        Employee newEmployee = new Employee(id, name);
        Employee saved = employeeRepository.save(newEmployee);
        logger.info("Employee created successfully: {}", saved);
        return saved;
    }

    @Override
    public Employee findById(int id) {
        logger.info("Service: Looking for employee with id={}", id);
        Employee employee = employeeRepository.findById(id);
        if (employee == null) {
            logger.warn("No employee found with id={}", id);
        } else {
            logger.info("Found employee with id={}, name={}", employee.getIdEmployee(), employee.getName());
        }
        return employee;
    }

    @Override
    public List<Employee> findAll() {
        logger.info("Service: Retrieving all employees");
        return employeeRepository.findAll();
    }

    @Override
    public Employee updateEmployee(int id, String newName) {
        logger.info("Service: Updating employee with id={} to have name={}", id, newName);
        Employee existing = employeeRepository.findById(id);
        if (existing == null) {
            logger.warn("Cannot update - no employee found with id={}", id);
            return null;
        }
        existing.setName(newName);
        Employee updated = employeeRepository.update(existing);
        logger.info("Employee updated: {}", updated);
        return updated;
    }

    @Override
    public Employee deleteEmployee(int id) {
        logger.info("Service: Deleting employee with id={}", id);
        Employee deleted = employeeRepository.delete(id);
        if (deleted == null) {
            logger.warn("No employee found for deletion with id={}", id);
        } else {
            logger.info("Employee deleted successfully: {}", deleted);
        }
        return deleted;
    }
}
