package domain;

import java.io.Serial;
import java.io.Serializable;

public class Tourist implements IdTourist<Integer>, Serializable {
    @Serial
    private static final long serialVersionUID = 3L;

    private int id;
    private String name;
    // Potentially add passport, phone, etc.

    public Tourist() {
    }

    public Tourist(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public Integer getIdTourist() {
        return id;
    }

    @Override
    public void setIdTourist(Integer id) {
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
        return "Tourist{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
