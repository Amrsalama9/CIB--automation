package com.cib.demoblaze.pages;

import com.cib.demoblaze.utils.WaitHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.NoSuchElementException;

/**
 * Base class for every page object.
 * <p>
 * Holds the shared header elements (nav links, welcome text, logout)
 * and provides convenience methods the child pages inherit.
 */
public abstract class BasePage {

    protected WebDriver driver;
    protected WaitHelper waitHelper;

    // ── Header navigation locators ──────────────────────────
    private final By signUpLink   = By.id("signin2");
    private final By loginLink    = By.id("login2");
    private final By logoutLink   = By.id("logout2");
    private final By welcomeLabel = By.id("nameofuser");
    private final By cartLink     = By.id("cartur");
    private final By homeLink     = By.cssSelector("a.nav-link[href='index.html']");

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.waitHelper = new WaitHelper(driver);
    }

    // ── Header actions ──────────────────────────────────────

    public void clickSignUp() {
        waitHelper.waitForClickable(signUpLink).click();
    }

    public void clickLogin() {
        waitHelper.waitForClickable(loginLink).click();
    }

    public void clickLogout() {
        waitHelper.waitForClickable(logoutLink).click();
    }

    public void clickCart() {
        waitHelper.waitForClickable(cartLink).click();
    }

    public void clickHome() {
        waitHelper.waitForClickable(homeLink).click();
    }

    /**
     * Returns the "Welcome username" text shown after login.
     */
    public String getWelcomeText() {
        return waitHelper.waitForVisible(welcomeLabel).getText();
    }

    /**
     * Checks whether the welcome label is visible in the header,
     * which tells us the user is currently logged in.
     */
    public boolean isWelcomeDisplayed() {
        try {
            WebElement el = driver.findElement(welcomeLabel);
            return el.isDisplayed() && !el.getText().isEmpty();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Checks whether the Sign up link is visible — indicates guest state.
     */
    public boolean isSignUpLinkDisplayed() {
        try {
            return driver.findElement(signUpLink).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Checks whether the Log in link is visible.
     */
    public boolean isLoginLinkDisplayed() {
        try {
            return driver.findElement(loginLink).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public String getPageTitle() {
        return driver.getTitle();
    }
}
