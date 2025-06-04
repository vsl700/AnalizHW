package org.demu.services;

import org.demu.exceptions.*;
import org.demu.models.Item;
import org.demu.models.ShoppingCart;
import org.demu.models.User;
import org.demu.repos.impl.ItemRepo;
import org.demu.repos.impl.ShoppingCartRepo;
import org.demu.repos.impl.UserRepo;

import java.util.ArrayList;
import java.util.List;

public class ShoppingService { // Shopping cart & ordering
    public void addItemToShoppingCart(User loggedUser, Item item){
        if(loggedUser == null)
            throw new AuthException("Must be logged in to access this method!");

        if(!new UserRepo().getAllUsers().contains(loggedUser))
            throw new AuthException("User doesn't exist in database!");

        if(!new ItemRepo().getAllItems().contains(item))
            throw new AvailabilityException("Item doesn't exist in database!");

        if(!item.isAvailable())
            throw new AvailabilityException("Item is not available!");

        new ShoppingCartRepo().addNewShoppingCart(new ShoppingCart(item.getId(), loggedUser.getId()));
    }

    public void orderAllItemsFromShoppingCart(User loggedUser){
        if(loggedUser == null)
            throw new AuthException("Must be logged in to access this method!");

        if(!new UserRepo().getAllUsers().contains(loggedUser))
            throw new AuthException("User doesn't exist in database!");

        var shoppingCartRepo = new ShoppingCartRepo();
        if(shoppingCartRepo.getAllByUser(loggedUser).isEmpty())
            throw new EmptyCartException("Shopping cart is empty!");

        var itemRepo = new ItemRepo();
        double totalPrice = shoppingCartRepo.getAllByUser(loggedUser).stream().mapToDouble(s -> itemRepo.findById(s.getItemId()).getPrice()).sum();
        if(totalPrice > loggedUser.getBalance())
            throw new InsufficientBalanceException("User's balance is too low to buy carted items!");

        List<Item> unavailableItems = new ArrayList<>();
        for(ShoppingCart shoppingCart : shoppingCartRepo.getAllByUser(loggedUser)){
            Item item = itemRepo.findById(shoppingCart.getItemId());
            item.setAvailable(false);
            unavailableItems.add(item);
        }

        loggedUser.setBalance(loggedUser.getBalance() - totalPrice);

        shoppingCartRepo.removeAllByUser(loggedUser);
        for(Item item : unavailableItems){
            shoppingCartRepo.removeAllByItem(item);
        }
    }

    public void removeItemFromShoppingCart(User loggedUser, Item item){
        if(loggedUser == null)
            throw new AuthException("Must be logged in to access this method!");

        if(!new UserRepo().getAllUsers().contains(loggedUser))
            throw new AuthException("User doesn't exist in database!");

        if(!new ItemRepo().getAllItems().contains(item))
            throw new AvailabilityException("Item doesn't exist in database!");

        var shoppingCartRepo = new ShoppingCartRepo();
        if(shoppingCartRepo.getAllByUser(loggedUser).isEmpty())
            throw new EmptyCartException("Shopping cart is empty!");

        ShoppingCart shoppingCart = shoppingCartRepo.findByUserAndItem(loggedUser, item);
        shoppingCartRepo.removeShoppingCart(shoppingCart);
    }
}
