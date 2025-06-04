package org.demu.repos.impl;

import org.demu.models.Item;
import org.demu.repos.IItemRepo;

import java.util.ArrayList;
import java.util.List;

public class ItemRepo implements IItemRepo {

    private static List<Item> items = new ArrayList<>();
    private static int idCount = 1;

    @Override
    public List<Item> getAllItems() {
        return items;
    }

    @Override
    public Item findById(int id) {
        return items.stream().filter(i -> i.getId() == id).findFirst()
                .orElseThrow();
    }

    @Override
    public void addItem(Item item) {
        item.setId(idCount);
        idCount++;
        items.add(item);
    }
}
