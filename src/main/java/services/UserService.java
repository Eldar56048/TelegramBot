package services;

import domain.models.User;
import repositories.UserRepository;
import repositories.interfaces.IUserRepository;
import services.interfaces.IUserService;

public class UserService implements IUserService {
    private IUserRepository userRepo = new UserRepository();

    public User getUserByID(long id) {
        return userRepo.getUserByID(id);
    }

    public User getUserByUsername(String username) {
        return userRepo.getUserByUsername(username);
    }

    public void addUser(User user) {
        userRepo.add(user);
    }

    @Override
    public void updateUser(User user) {
        userRepo.update(user);
    }
}
