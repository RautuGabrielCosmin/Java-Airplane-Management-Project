package service;

import domain.User;

import java.io.IOException;
import java.util.List;

public interface UserService {
    User createUser(int id, String name, String password) throws IOException;
    //    User createUser(String name, String password);
    User findById(int id) throws IOException;
    List<User> findAll() throws IOException;
    User updateUser(int id, String newName, String newPassword) throws IOException;
    User deleteUser(int id) throws IOException;
}
