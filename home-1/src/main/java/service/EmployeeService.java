package service;

import domain.Employee;
import java.util.List;

public interface EmployeeService {
    Employee createEmployee(int id, String name);
    Employee findById(int id);
    List<Employee> findAll();
    Employee updateEmployee(int id, String newName);
    Employee deleteEmployee(int id);
}