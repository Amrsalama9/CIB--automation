@web @auth
Feature: DemoBlaze User Authentication
  As a user of the DemoBlaze e-commerce website
  I want to register, log in, and log out
  So that I can manage my account and make purchases securely

  Background:
    Given the user navigates to "https://demoblaze.com"

  @smoke @P0
  Scenario: WEB-01 Successful user registration
    When the user clicks the "Sign up" link in the header
    And the user enters a unique username and password
    And the user clicks the "Sign up" button
    Then the alert message "Sign up successful." should be displayed

  @P1 @negative
  Scenario: WEB-02 Registration with an existing username
    When the user clicks the "Sign up" link in the header
    And the user enters an already-registered username and a password
    And the user clicks the "Sign up" button
    Then an error alert indicating the user already exists should be displayed

  @smoke @P0
  Scenario: WEB-03 Successful login with valid credentials
    When the user clicks the "Log in" link in the header
    And the user enters valid username and password
    And the user clicks the "Log in" button
    Then the header should display "Welcome <username>"

  @P1 @negative
  Scenario: WEB-04 Login with invalid credentials
    When the user clicks the "Log in" link in the header
    And the user enters an incorrect username or password
    And the user clicks the "Log in" button
    Then the welcome message should NOT be displayed in the header

  @P1
  Scenario: WEB-05 Successful logout
    Given the user is logged in with valid credentials
    When the user clicks the "Log out" link in the header
    Then the header should display the "Sign up" and "Log in" links
