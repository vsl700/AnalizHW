package org.demu.repos;

import org.demu.models.User;

import java.util.List;

public interface IUserRepo {
    List<User> getAllUsers();
    User findById(int id);
    void addUser(User user);
}
