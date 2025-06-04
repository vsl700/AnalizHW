package org.demu.services;

import org.demu.exceptions.AuthException;
import org.demu.exceptions.UserRoleException;
import org.demu.models.Item;
import org.demu.models.User;
import org.demu.repos.impl.ItemRepo;

import java.util.List;

public class ItemService {
    public void addItem(User loggedUser, Item item){
        if(loggedUser == null)
            throw new AuthException("Must be logged in to access this method!");

        if(!loggedUser.getRole().equals("Admin"))
            throw new UserRoleException("Must be admin to access this method!");

        if(item == null)
            throw new IllegalArgumentException("Item cannot be null");

        new ItemRepo().addItem(item);
    }

    public List<Item> browseItems(){
        return new ItemRepo().getAllItems();
    }
}
