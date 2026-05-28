package com.cib.demoblaze.pages;

import com.cib.demoblaze.utils.WaitHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Handles the Log In modal dialog.
 */
public class LoginPage extends BasePage {

    private final By usernameField = By.id("loginusername");
    private final By passwordField = By.id("loginpassword");
    private final By loginButton   = By.cssSelector("#logInModal .btn-primary");
    private final By modalDialog   = By.id("logInModal");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void waitForModal() {
        waitHelper.waitForVisible(modalDialog);
        WaitHelper.briefPause(500);
    }

    public void enterUsername(String username) {
        waitHelper.waitForVisible(usernameField).clear();
        driver.findElement(usernameField).sendKeys(username);
    }

    public void enterPassword(String password) {
        driver.findElement(passwordField).clear();
        driver.findElement(passwordField).sendKeys(password);
    }

    public void clickLoginButton() {
        waitHelper.waitForClickable(loginButton).click();
    }

    /**
     * Full login flow in one call.
     */
    public void loginWith(String username, String password) {
        waitForModal();
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
    }
}
