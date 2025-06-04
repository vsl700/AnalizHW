package org.demu.repos.impl;

import org.demu.exceptions.RecordExistsException;
import org.demu.models.Item;
import org.demu.models.ShoppingCart;
import org.demu.models.User;
import org.demu.repos.IShoppingCartRepo;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCartRepo implements IShoppingCartRepo {

    private static List<ShoppingCart> shoppingCartList = new ArrayList<>();

    @Override
    public List<ShoppingCart> getAllByUser(User user) {
        return shoppingCartList.stream()
                .filter(s -> s.getUserId() == user.getId())
                .toList();
    }

    @Override
    public List<ShoppingCart> getAllByItem(Item item) {
        return shoppingCartList.stream()
                .filter(s -> s.getItemId() == item.getId())
                .toList();
    }

    @Override
    public ShoppingCart findByUserAndItem(User user, Item item) {
        return shoppingCartList.stream()
                .filter(s -> s.getUserId() == user.getId() && s.getItemId() == item.getId())
                .findFirst()
                .orElse(null);
    }

    @Override
    public void addNewShoppingCart(ShoppingCart shoppingCart) {
        if(findByUserAndItem(new UserRepo().findById(shoppingCart.getUserId()), new ItemRepo().findById(shoppingCart.getItemId())) != null)
            throw new RecordExistsException("Item already in shopping cart!");

        shoppingCartList.add(shoppingCart);
    }

    @Override
    public void removeShoppingCart(ShoppingCart shoppingCart) {
        shoppingCartList.remove(shoppingCart);
    }

    @Override
    public void removeAllByUser(User user) {
        shoppingCartList.removeAll(getAllByUser(user));
    }

    @Override
    public void removeAllByItem(Item item) {
        shoppingCartList.removeAll(getAllByItem(item));
    }
}
