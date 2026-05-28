@web @products
Feature: DemoBlaze Product Browsing
  As a customer on the DemoBlaze website
  I want to browse products by category and view their details
  So that I can find the items I am interested in purchasing

  Background:
    Given the user navigates to "https://demoblaze.com"

  @smoke @P0
  Scenario Outline: WEB-06 Filter products by category
    When the user clicks the "<category>" category link
    Then the product grid should display only products in "<category>"
    Examples:
      | category |
      | Phones   |
      | Laptops  |
      | Monitors |

  @smoke @P0
  Scenario: WEB-07 Product listing shows correct name and price
    When the user clicks the "Monitors" category link
    Then the product card for "Apple monitor 24" should display price "$400"

  @P1
  Scenario: WEB-08 Product details page shows correct information
    When the user clicks the "Monitors" category link
    And the user clicks on the product "Apple monitor 24"
    Then the product details page should show name "Apple monitor 24"
    And the product details page should show price "$400"

  @smoke @P0
  Scenario: WEB-09 Add product to cart
    Given the user is logged in with valid credentials
    When the user clicks the "Monitors" category link
    And the user clicks on the product "Apple monitor 24"
    And the user clicks the "Add to cart" button
    Then the alert message "Product added." should be displayed
