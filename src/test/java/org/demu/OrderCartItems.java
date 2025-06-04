package org.demu;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.demu.exceptions.DemuRuntimeException;
import org.demu.models.Item;
import org.demu.models.ShoppingCart;
import org.demu.models.User;
import org.demu.repos.impl.ItemRepo;
import org.demu.repos.impl.ShoppingCartRepo;
import org.demu.repos.impl.UserRepo;
import org.demu.services.ShoppingService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class OrderCartItems {
    private final MessageContext messageContext;
    private User selectedUser;
    private UserRepo userRepo;
    private ItemRepo itemRepo;
    private ShoppingCartRepo shoppingCartRepo;
    private ShoppingService shoppingService;

    private List<Item> orderedItems;

    public OrderCartItems(MessageContext messageContext, UserRepo userRepo, ItemRepo itemRepo, ShoppingCartRepo shoppingCartRepo){
        this.messageContext = messageContext;
        this.userRepo = userRepo;
        this.itemRepo = itemRepo;
        this.shoppingCartRepo = shoppingCartRepo;
        shoppingService = new ShoppingService();

        orderedItems = new ArrayList<>();
    }

    private void loadUsersAndItems(){
        userRepo.addUser(new User("ivan40", "abc123456", "iivanov23@abv.bg", "Ivan", "Ivanov"));
        userRepo.addUser(new User("goshk0", "fjls10843", "georgievg@abv.bg", "Georgi", "Georgiev"));
        selectedUser = userRepo.getAllUsers().get(userRepo.getAllUsers().size() - 1);
        selectedUser.setBalance(200);

        itemRepo.addItem(new Item("iPhone 16 Pro Max", "Almost brand new", "/images/jfsllj.jpg", 999999.99));
        itemRepo.addItem(new Item("Arduino Kit", "A beginner-friendly Arduino kit", "/images/kfjhdslkhha123.jpg", 100.49));
        itemRepo.addItem(new Item("LEGO Constructor", "Your kids will like it!!! (and their dad too)", "/images/342fdsa1.jpg", 50.99));
    }

    private User getAnotherUser(){
        return userRepo.getAllUsers().stream().filter(u -> !u.equals(selectedUser)).findFirst().orElseThrow();
    }

    @Given("User has items in shopping cart")
    public void addItemsToUserShoppingCart(){
        loadUsersAndItems();

        Item item1 = itemRepo.getAllItems().get(1);
        shoppingCartRepo.addNewShoppingCart(new ShoppingCart(item1.getId(), selectedUser.getId()));
        orderedItems.add(item1);

        Item item2 = itemRepo.getAllItems().get(2);
        shoppingCartRepo.addNewShoppingCart(new ShoppingCart(item2.getId(), selectedUser.getId()));
        orderedItems.add(item2);
    }

    @Given("our User doesn't have Items in their shopping cart")
    public void loadItemsWithoutLoadingShoppingCart(){
        loadUsersAndItems();
    }

    @When("our User doesn't have money in his balance")
    public void emptySelectedUserBalance(){
        selectedUser.setBalance(0);
    }

    @When("User is not logged in")
    public void unselectUser(){
        selectedUser = null;
    }

    @When("our User doesn't exist in database")
    public void makeUpAndSelectUser(){
        selectedUser = new User("phantom1999", "jfdskj4839", "nobody@abv.bg", "Danny", "Phantom");
    }

    @When("another User has some of the Items the orderer has")
    public void addMutualItemToAnotherUser(){
        User anotherUser = getAnotherUser();
        shoppingCartRepo.addNewShoppingCart(new ShoppingCart(itemRepo.getAllItems().get(0).getId(), anotherUser.getId()));
        shoppingCartRepo.addNewShoppingCart(new ShoppingCart(itemRepo.getAllItems().get(1).getId(), anotherUser.getId()));
    }

    @When("our User hits the Order button")
    @When("User hits the Order button")
    @When("our User tries to order")
    public void userHitsOrderButton(){
        try{
            shoppingService.orderAllItemsFromShoppingCart(selectedUser);
        }catch (DemuRuntimeException e){
            messageContext.setMessage(e.getMessage());
        }
    }

    @Then("Items should be marked as unavailable")
    public void checkIfOrderedItemsAreMarkedAsUnavailable(){
        for (Item item : orderedItems){
            if(item.isAvailable())
                fail();
        }
    }

    @Then("User's balance decreases according to the order")
    public void checkIfSelectedUserBalanceDecreasedAccordingly(){
        double total = orderedItems.stream().mapToDouble(Item::getPrice).sum();
        double initialBalance = 200;

        assertEquals(selectedUser.getBalance(), initialBalance - total, 0.0);
    }

    @Then("Items should no longer be in other User's cart")
    public void checkIfItemsAreNoLongerInOtherUsersCart(){
        for (Item item : orderedItems)
            assertTrue(shoppingCartRepo.getAllByItem(item).isEmpty());
    }

    @Then("we get an error message {string}")
    public void errorMessage(String expectedMessage){
        assertEquals(expectedMessage, messageContext.getMessage());
    }
}
