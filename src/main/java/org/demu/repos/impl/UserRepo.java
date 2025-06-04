package org.demu.repos.impl;

import org.demu.models.User;
import org.demu.repos.IUserRepo;

import java.util.ArrayList;
import java.util.List;

public class UserRepo
    implements IUserRepo {

    private static List<User> users = new ArrayList<>();
    private static int idCount = 1;

    @Override
    public List<User> getAllUsers() {
        //users.add(new User("ivan", "123", "ghsdfg@jhgfas.com", "Ivan", "Ivanov"));
        return users;
    }

    @Override
    public User findById(int id) {
        return users.stream().filter(u -> u.getId() == id).findFirst()
                .orElseThrow();
    }

    @Override
    public void addUser(User user) {
        user.setId(idCount);
        idCount++;
        users.add(user);
    }
}
