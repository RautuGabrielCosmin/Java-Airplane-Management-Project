package service;

import desktop_app.user.User;

import java.util.List;

public interface UserService {
    User createUser(int id, String name, String password);
    User findById(int id);
    List<User> findAll();
    User updateUser(int id, String newName, String newPassword);
    User deleteUser(int id);
}
