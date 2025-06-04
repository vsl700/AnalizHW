package org.demu.repos;

import org.demu.models.Item;

import java.util.List;

public interface IItemRepo {
    List<Item> getAllItems();
    Item findById(int id);
    void addItem(Item item);
}
