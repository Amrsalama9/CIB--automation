package com.cib.demoblaze.pages;

import com.cib.demoblaze.utils.WaitHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Handles the Sign Up modal dialog.
 * <p>
 * The modal is part of the home page DOM but opens as an overlay,
 * so we model it as its own page object for clarity.
 */
public class SignUpPage extends BasePage {

    private final By usernameField = By.id("sign-username");
    private final By passwordField = By.id("sign-password");
    private final By signUpButton  = By.cssSelector("#signInModal .btn-primary");
    private final By modalDialog   = By.id("signInModal");

    public SignUpPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Waits for the modal to be visible before interacting.
     */
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

    public void clickSignUpButton() {
        waitHelper.waitForClickable(signUpButton).click();
    }

    /**
     * Convenience method - fills both fields and submits.
     */
    public void registerWith(String username, String password) {
        waitForModal();
        enterUsername(username);
        enterPassword(password);
        clickSignUpButton();
    }
}
