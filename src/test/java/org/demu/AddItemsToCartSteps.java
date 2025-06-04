package org.demu;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.demu.exceptions.DemuRuntimeException;
import org.demu.models.Item;
import org.demu.models.User;
import org.demu.repos.impl.ItemRepo;
import org.demu.repos.impl.ShoppingCartRepo;
import org.demu.repos.impl.UserRepo;
import org.demu.services.ShoppingService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AddItemsToCartSteps {
    private final MessageContext messageContext;
    private User selectedUser;
    private Item selectedItem;
    private UserRepo userRepo;
    private ItemRepo itemRepo;
    private ShoppingService shoppingService;

    public AddItemsToCartSteps(MessageContext messageContext, UserRepo userRepo, ItemRepo itemRepo){
        this.messageContext = messageContext;
        this.userRepo = userRepo;
        this.itemRepo = itemRepo;
        shoppingService = new ShoppingService();
    }

    @Given("we have Items and Users in the database")
    public void fillReposWithData(){
        userRepo.addUser(new User("ivan40", "abc123456", "iivanov23@abv.bg", "Ivan", "Ivanov"));
        userRepo.addUser(new User("goshk0", "fjls10843", "georgievg@abv.bg", "Georgi", "Georgiev"));
        selectedUser = userRepo.getAllUsers().get(userRepo.getAllUsers().size() - 1);

        itemRepo.addItem(new Item("iPhone 16 Pro Max", "Almost brand new", "/images/jfsllj.jpg", 999999.99));
        itemRepo.addItem(new Item("Arduino Kit", "A beginner-friendly Arduino kit", "/images/kfjhdslkhha123.jpg", 100.49));
        itemRepo.addItem(new Item("LEGO Constructor", "Your kids will like it!!! (and their dad too)", "/images/342fdsa1.jpg", 50.99));
        selectedItem = itemRepo.getAllItems().get(itemRepo.getAllItems().size() - 1);
    }

    @When("we are logged in as an Admin")
    public void changeUserRoleToAdmin(){
        selectedUser.setRole("Admin");
    }

    @When("we are logged in as non-existing User")
    public void makeUpAndSelectUser(){
        selectedUser = new User("phantom1999", "jfdskj4839", "nobody@abv.bg", "Danny", "Phantom");
    }

    @When("we are not logged in")
    public void unselectUser(){
        selectedUser = null;
    }

    @When("one Item is not available")
    public void setRandomNonSelectedItemAsUnavailable(){
        var items = itemRepo.getAllItems();
        Item tempItem;
        do{
            tempItem = items.stream().findAny().orElseThrow();
        }while(tempItem.equals(selectedItem));

        tempItem.setAvailable(false);
    }

    @When("we select the unavailable Item")
    public void selectUnavailableItem() throws Exception {
        for(Item item : itemRepo.getAllItems()){
            if(!item.isAvailable()) {
                selectedItem = item;
                return;
            }
        }

        throw new Exception("No Items are marked as unavailable!");
    }

    @When("we make up an Item and select it")
    public void makeUpAndSelectItem(){
        selectedItem = new Item("Danny the Phantom", "A (phantom) toy", "/images/ffjdks2039.jpg", 90.00);
    }

    @When("we add an Item to shopping cart")
    @When("we try to add selected Item to cart")
    public void addSelectedItemToShoppingCart(){
        try{
            shoppingService.addItemToShoppingCart(selectedUser, selectedItem);
        } catch (DemuRuntimeException e){
            messageContext.setMessage(e.getMessage());
        }
    }

    @Then("we get no errors")
    public void noErrors(){
        assertNull(messageContext.getMessage());
    }

    @Then("we get the error message {string}")
    public void error(String expectedMessage){
        assertEquals(expectedMessage, messageContext.getMessage());
    }
}
