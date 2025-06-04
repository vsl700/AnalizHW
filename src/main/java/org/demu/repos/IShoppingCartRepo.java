package org.demu.repos;

import org.demu.models.Item;
import org.demu.models.ShoppingCart;
import org.demu.models.User;

import java.util.List;

public interface IShoppingCartRepo {
    List<ShoppingCart> getAllByUser(User user);
    List<ShoppingCart> getAllByItem(Item item);
    ShoppingCart findByUserAndItem(User user, Item item);
    void addNewShoppingCart(ShoppingCart shoppingCart);
    void removeShoppingCart(ShoppingCart shoppingCart);
    void removeAllByUser(User user);
    void removeAllByItem(Item item);
}
