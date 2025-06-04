Feature: Adding an item to shopping cart

  Scenario: A normal User adds Item to cart
    Given we have Items and Users in the database
    When we add an Item to shopping cart
    Then we get no errors

  Scenario: An Admin adds Item to cart
    Given we have Items and Users in the database
    When we are logged in as an Admin
    And we add an Item to shopping cart
    Then we get no errors

  Scenario: Adding an unavailable item
    Given we have Items and Users in the database
    When one Item is not available
    And we select the unavailable Item
    And we try to add selected Item to cart
    Then we get the error message "Item is not available!"

  Scenario: Adding an Item that's already in User's cart
    Given we have Items and Users in the database
    When we add an Item to shopping cart
    And we try to add selected Item to cart
    Then we get the error message "Item already in shopping cart!"

  Scenario: Adding an Item that doesn't exist
    Given we have Items and Users in the database
    When we make up an Item and select it
    And we try to add selected Item to cart
    Then we get the error message "Item doesn't exist in database!"

  Scenario: Adding an Item as a non-existing User
    Given we have Items and Users in the database
    When we are logged in as non-existing User
    And we add an Item to shopping cart
    Then we get the error message "User doesn't exist in database!"

  Scenario: Trying to add an Item without logging in
    Given we have Items and Users in the database
    When we are not logged in
    And we add an Item to shopping cart
    Then we get the error message "Must be logged in to access this method!"
