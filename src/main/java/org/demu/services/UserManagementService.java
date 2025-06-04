package org.demu.services;

import org.demu.exceptions.AuthException;
import org.demu.exceptions.UserRoleException;
import org.demu.models.User;
import org.demu.repos.impl.UserRepo;

import java.util.List;

public class UserManagementService {
    // Browse users
    public List<User> getAllUsers(User loggedUser){
        if(loggedUser == null)
            throw new AuthException("Must be logged in to access this method!");

        return new UserRepo().getAllUsers();
    }

    public void assignAdmin(User loggedUser, User user){
        if(loggedUser == null)
            throw new AuthException("Must be logged in to access this method!");

        if(!loggedUser.getRole().equals("Admin"))
            throw new UserRoleException("Must be admin to access this method!");

        if(user == null)
            throw new IllegalArgumentException("User cannot be null");

        user.setRole("Admin");
    }
}
