package repositories.interfaces;

import domain.UserLoginData;
import domain.models.User;

public interface IUserRepository extends IEntityRepository<User> {
    User getUserByID(long id);

    User findUserByLogin(UserLoginData data);

    User getUserByUsername(String username);
}
