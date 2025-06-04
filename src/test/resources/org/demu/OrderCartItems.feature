Feature: Ordering all items from shopping cart

  Scenario: User orders, ordered Items vanish from other shopping carts
    Given User has items in shopping cart
    When another User has some of the Items the orderer has
    And our User hits the Order button
    Then Items should no longer be in other User's cart

  Scenario: User orders, all Items marked as unavailable
    Given User has items in shopping cart
    When User hits the Order button
    Then Items should be marked as unavailable

  Scenario: User orders, balance decreases accordingly
    Given User has items in shopping cart
    When User hits the Order button
    Then User's balance decreases according to the order

  Scenario: User orders, but their balance is insufficient
    Given User has items in shopping cart
    When our User doesn't have money in his balance
    And our User hits the Order button
    Then we get an error message "User's balance is too low to buy carted items!"

  Scenario: User orders, but they aren't logged in
    Given User has items in shopping cart
    When User is not logged in
    And User hits the Order button
    Then we get an error message "Must be logged in to access this method!"

  Scenario: User orders, but they don't exist in the database
    Given User has items in shopping cart
    When our User doesn't exist in database
    And our User tries to order
    Then we get an error message "User doesn't exist in database!"

  Scenario: User orders, but their cart is empty
    Given our User doesn't have Items in their shopping cart
    When our User tries to order
    Then we get an error message "Shopping cart is empty!"