package ru.itis.unogame.repository.api;


import ru.itis.unogame.model.User;

public interface UserRepository {

    /**
     * @return saved user ID*/
    int save();

    User findById();

    User findByUsername(String username);

    boolean update(User user);

}
