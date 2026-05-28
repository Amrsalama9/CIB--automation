package com.cib.demoblaze.pages;

import com.cib.demoblaze.utils.WaitHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Page object for the DemoBlaze home page.
 * <p>
 * Covers the product grid, category sidebar, and basic page assertions.
 */
public class HomePage extends BasePage {

    // ── Locators ────────────────────────────────────────────
    private final By productCards    = By.cssSelector("#tbodyid .card");
    private final By productNames   = By.cssSelector("#tbodyid .card-title a");
    private final By productPrices  = By.cssSelector("#tbodyid .card-block h5");
    private final By categoryPhones   = By.linkText("Phones");
    private final By categoryLaptops  = By.linkText("Laptops");
    private final By categoryMonitors = By.linkText("Monitors");
    private final By navBar           = By.id("navbarExample");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void navigateTo(String url) {
        driver.get(url);
    }

    /**
     * Waits until at least one product card is visible on the grid.
     */
    public boolean isProductGridDisplayed() {
        waitHelper.waitForVisible(productCards);
        return !driver.findElements(productCards).isEmpty();
    }

    public boolean isNavBarDisplayed() {
        return driver.findElement(navBar).isDisplayed();
    }

    // ── Category filtering ──────────────────────────────────

    public void selectCategory(String category) {
        switch (category.toLowerCase()) {
            case "phones":
                waitHelper.waitForClickable(categoryPhones).click();
                break;
            case "laptops":
                waitHelper.waitForClickable(categoryLaptops).click();
                break;
            case "monitors":
                waitHelper.waitForClickable(categoryMonitors).click();
                break;
            default:
                throw new IllegalArgumentException("Unknown category: " + category);
        }
        // The grid reloads via AJAX — give it a moment to settle
        WaitHelper.briefPause(2000);
    }

    // ── Product info helpers ────────────────────────────────

    public List<String> getDisplayedProductNames() {
        WaitHelper.briefPause(1000);
        return driver.findElements(productNames)
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public String getProductPrice(String productName) {
        List<WebElement> cards = driver.findElements(productCards);
        for (WebElement card : cards) {
            String name = card.findElement(By.cssSelector(".card-title a")).getText();
            if (name.equalsIgnoreCase(productName)) {
                return card.findElement(By.cssSelector("h5")).getText();
            }
        }
        return null;
    }

    public void clickProduct(String productName) {
        List<WebElement> names = driver.findElements(productNames);
        for (WebElement el : names) {
            if (el.getText().equalsIgnoreCase(productName)) {
                el.click();
                return;
            }
        }
        throw new RuntimeException("Product not found on the grid: " + productName);
    }
}
