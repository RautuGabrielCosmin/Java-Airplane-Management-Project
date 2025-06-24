package service;

import com.fasterxml.jackson.core.JsonProcessingException;
import domain.Employee;

import java.io.IOException;
import java.util.List;

public interface EmployeeService {
    Employee createEmployee(int id, String name) throws IOException;
    Employee findById(int id) throws IOException;
    List<Employee> findAll() throws IOException;
    Employee updateEmployee(int id, String newName) throws IOException;
    Employee deleteEmployee(int id) throws IOException;

    int getNextEmployeeId() throws IOException;
}