package domain;

import java.io.Serial;
import java.io.Serializable;

public class Employee implements IdEmployee<Integer>, Serializable {
    @Serial
    private static final long serialVersionUID = 4L;
    transient private int id;
    private String name;

    public Employee() {
    }

    public Employee(int id, String name) {
        this.id = id;
        this.name = name;
    }
    @Override
    public Integer getIdEmployee() {
        return id;
    }
    @Override
    public void setIdEmployee(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}