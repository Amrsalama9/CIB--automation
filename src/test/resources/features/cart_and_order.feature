@web @cart
Feature: DemoBlaze Cart and Order Placement
  As a logged-in customer on DemoBlaze
  I want to manage items in my cart and place orders
  So that I can complete my purchase successfully

  Background:
    Given the user navigates to "https://demoblaze.com"
    And the user is logged in with valid credentials
    And the user has added "Apple monitor 24" to the cart

  @smoke @P0
  Scenario: WEB-10 Cart displays the correct product and price
    When the user clicks the "Cart" link in the header
    Then the cart should display product "Apple monitor 24"
    And the cart should display the price for "Apple monitor 24"

  @P1
  Scenario: WEB-11 Cart total equals sum of item prices
    When the user clicks the "Cart" link in the header
    Then the cart total price should equal the sum of all item prices

  @smoke @P0 @order @e2e
  Scenario: WEB-12 Place an order successfully
    When the user clicks the "Cart" link in the header
    And the user clicks the "Place Order" button
    And the user fills in the order form with valid details:
      | field   | value            |
      | Name    | John             |
      | Country | Egypt            |
      | City    | Cairo            |
      | Card    | 4111111111111111 |
      | Month   | January          |
      | Year    | 2026             |
    And the user clicks the "Purchase" button
    Then the confirmation message "Thank you for your purchase!" is displayed
    And the confirmation should display the correct total price "$400"

  @P2 @order @negative
  Scenario: WEB-13 Submit empty order form shows validation message
    When the user clicks the "Cart" link in the header
    And the user clicks the "Place Order" button
    And the user clicks the "Purchase" button without filling any field
    Then an error or validation message should be displayed

  @e2e @smoke @P0
  Scenario: WEB-14 Full end-to-end purchase journey
    Given the user navigates to "https://demoblaze.com"
    When the user registers with a new unique username and password
    And the user logs in with those credentials
    And the user selects "Monitors" category and clicks "Apple monitor 24"
    And the user validates name "Apple monitor 24" and price "$400" on details page
    And the user clicks "Add to cart" and confirms the alert "Product added."
    And the user opens the Cart and validates "Apple monitor 24" is listed
    And the user validates the cart total equals the sum of all item prices
    And the user places an order with valid payment details
    Then the confirmation message "Thank you for your purchase!" is displayed
    And the user logs out
    And the header should display the "Sign up" and "Log in" links
