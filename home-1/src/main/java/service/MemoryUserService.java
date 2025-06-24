package service;

import java.util.List;
import domain.User;
import repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MemoryUserService implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(MemoryUserService.class);

    private final UserRepository userRepository;
    public MemoryUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(int id, String name, String password){
        logger.info("Creating user with id " + id + " and name " + name + " and password " + password);
        if(id <= 0){
            id = getNextUserId();  // use the helper method above
            logger.info("Auto-generated new user ID: {}", id);
        }

        if(name == null || name.isEmpty() || password == null || password.isEmpty()){
            logger.error("Name and password are empty");
            throw new IllegalArgumentException("Name and password are empty");
        }
        User existing = userRepository.findById(id);
        if(existing != null){
            logger.error("User with id " + id + " already exists");
            throw new IllegalArgumentException("User with id " + id + " already exists");
        }
        User newUser = new User(0, name, password);
        User savedUser = userRepository.save(newUser);
        logger.info("Created user with id " + id + " and name " + name + " and password " + password);
        return savedUser;
    }

    public User findById(int id){
        logger.info("Finding user with id " + id);
        User user = userRepository.findById(id);
        if(user == null){
            logger.error("User with id " + id + " not found");
        }else{
            logger.info("Found user with id " + id);
        }
        return user;
    }

    public List<User> findAll(){
        logger.info("Finding all users");
        return userRepository.findAll();
    }

    public User updateUser(int id, String name, String password){
        logger.info("Updating user with id " + id + " and name " + name + " and password " + password);
        User user = userRepository.findById(id);
        if(user == null){
            logger.error("User with id " + id + " not found");
            return null;
        }
        user.setUsername(name);
        user.setPassword(password);
        User savedUser = userRepository.save(user);
        logger.info("Updated user with id " + id + " and name " + name + " and password " + password);
        return savedUser;
    }

    public User deleteUser(int id){
        logger.info("Deleting user with id " + id);
        User user = userRepository.findById(id);
        if(user == null){
            logger.error("User with id " + id + " not found");
            return null;
        }
        else{
            logger.info("Deleted user with id " + id);
            userRepository.delete(id);
        }
        return user;
    }

    public int getNextUserId() {
        List<User> allUsers = findAll();
        int maxId = 0;
        for (User u : allUsers) {
            if (u.getId() > maxId) {
                maxId = u.getId();
            }
        }
        logger.info("Calculated max user ID: {}", maxId);
        return maxId + 1;
    }
}
