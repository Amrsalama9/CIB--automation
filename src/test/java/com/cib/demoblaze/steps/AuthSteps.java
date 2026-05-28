package com.cib.demoblaze.steps;

import com.cib.demoblaze.pages.HomePage;
import com.cib.demoblaze.pages.LoginPage;
import com.cib.demoblaze.pages.SignUpPage;
import com.cib.demoblaze.utils.ConfigReader;
import com.cib.demoblaze.utils.DriverFactory;
import com.cib.demoblaze.utils.WaitHelper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.util.UUID;

/**
 * Step definitions for authentication-related scenarios:
 * sign up, log in, log out, and credential validation.
 */
public class AuthSteps {

    private WebDriver driver;
    private HomePage homePage;
    private SignUpPage signUpPage;
    private LoginPage loginPage;
    private WaitHelper waitHelper;

    // Holds the username generated at runtime for registration tests
    private static String generatedUsername;
    private static String generatedPassword;

    private void initPages() {
        driver = DriverFactory.getDriver();
        homePage = new HomePage(driver);
        signUpPage = new SignUpPage(driver);
        loginPage = new LoginPage(driver);
        waitHelper = new WaitHelper(driver);
    }

    // Navigation

    @Given("the user navigates to {string}")
    public void userNavigatesTo(String url) {
        initPages();
        homePage.navigateTo(url);
        WaitHelper.briefPause(1500);
    }

    // Header link clicks

    @When("the user clicks the {string} link in the header")
    public void userClicksHeaderLink(String linkText) {
        initPages();
        switch (linkText) {
            case "Sign up":
                homePage.clickSignUp();
                break;
            case "Log in":
                homePage.clickLogin();
                break;
            case "Log out":
                homePage.clickLogout();
                break;
            case "Cart":
                homePage.clickCart();
                WaitHelper.briefPause(2000);
                break;
            default:
                throw new IllegalArgumentException("Unknown header link: " + linkText);
        }
    }

    // Sign Up - fill fields only (button click is a separate step)

    @When("the user enters a unique username and password")
    public void userEntersUniqueCredentials() {
        initPages();
        generatedUsername = "cib_auto_" + UUID.randomUUID().toString().substring(0, 8);
        generatedPassword = "Test@" + System.currentTimeMillis();
        signUpPage.waitForModal();
        signUpPage.enterUsername(generatedUsername);
        signUpPage.enterPassword(generatedPassword);
    }

    @When("the user enters an already-registered username and a password")
    public void userEntersExistingCredentials() {
        initPages();
        signUpPage.waitForModal();
        signUpPage.enterUsername(ConfigReader.getUsername());
        signUpPage.enterPassword(ConfigReader.getPassword());
    }

    // Log In - fill fields only

    @When("the user enters valid username and password")
    public void userEntersValidCredentials() {
        initPages();
        loginPage.waitForModal();
        loginPage.enterUsername(ConfigReader.getUsername());
        loginPage.enterPassword(ConfigReader.getPassword());
    }

    @When("the user enters an incorrect username or password")
    public void userEntersInvalidCredentials() {
        initPages();
        loginPage.waitForModal();
        loginPage.enterUsername("nonexistent_user_xyz_999");
        loginPage.enterPassword("totallyWrongPwd!");
    }

    // Composite login step (used in Background / Given)

    @Given("the user is logged in with valid credentials")
    public void userIsLoggedIn() {
        initPages();
        homePage.clickLogin();
        loginPage.loginWith(ConfigReader.getUsername(), ConfigReader.getPassword());
        WaitHelper.briefPause(2000);
        Assert.assertTrue(homePage.getWelcomeText().contains("Welcome"),
                "Login failed - welcome text not visible");

        // Clear any leftover cart items from previous test runs
        // DemoBlaze persists cart server-side per user account
        homePage.clickCart();
        WaitHelper.briefPause(2000);
        com.cib.demoblaze.pages.CartPage cart = new com.cib.demoblaze.pages.CartPage(driver);
        cart.clearCart();
        homePage.clickHome();
        WaitHelper.briefPause(1000);
    }


    @Then("the alert message {string} should be displayed")
    public void alertMessageShouldBeDisplayed(String expectedMessage) {
        initPages();
        Alert alert = waitHelper.waitForAlert();
        String alertText = alert.getText();
        alert.accept();
        Assert.assertTrue(alertText.contains(expectedMessage),
                "Expected alert to contain '" + expectedMessage + "' but got '" + alertText + "'");
    }

    @Then("an error alert indicating the user already exists should be displayed")
    public void errorAlertUserAlreadyExists() {
        initPages();
        Alert alert = waitHelper.waitForAlert();
        String alertText = alert.getText();
        alert.accept();
        Assert.assertTrue(
                alertText.toLowerCase().contains("already exist"),
                "Expected 'already exist' error but got: " + alertText);
    }

    @Then("the header should display {string}")
    public void headerShouldDisplay(String expectedText) {
        initPages();
        WaitHelper.briefPause(2000);
        String welcomeText = homePage.getWelcomeText();
        if (expectedText.contains("<username>")) {
            expectedText = expectedText.replace("<username>", ConfigReader.getUsername());
        }
        Assert.assertTrue(welcomeText.contains(expectedText),
                "Expected header to show '" + expectedText + "' but found '" + welcomeText + "'");
    }

    @Then("the welcome message should NOT be displayed in the header")
    public void welcomeMessageShouldNotBeDisplayed() {
        initPages();
        WaitHelper.briefPause(3000);

        // DemoBlaze may show an alert on bad login - dismiss it if present
        try {
            Alert alert = driver.switchTo().alert();
            alert.accept();
        } catch (NoAlertPresentException ignored) {
        }

        WaitHelper.briefPause(1000);
        Assert.assertFalse(homePage.isWelcomeDisplayed(),
                "Welcome message should not be visible after invalid login");
    }

    @Then("the header should display the {string} and {string} links")
    public void headerShouldShowGuestLinks(String link1, String link2) {
        initPages();
        WaitHelper.briefPause(2000);
        Assert.assertTrue(homePage.isSignUpLinkDisplayed(),
                "'" + link1 + "' link should be visible in guest state");
        Assert.assertTrue(homePage.isLoginLinkDisplayed(),
                "'" + link2 + "' link should be visible in guest state");
    }


    public static String getGeneratedUsername() {
        return generatedUsername;
    }

    public static String getGeneratedPassword() {
        return generatedPassword;
    }
}
