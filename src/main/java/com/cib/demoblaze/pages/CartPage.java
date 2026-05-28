package com.cib.demoblaze.pages;

import com.cib.demoblaze.utils.WaitHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Page object for the shopping cart page (cart.html).
 */
public class CartPage extends BasePage {

    private final By cartItems      = By.cssSelector("#tbodyid tr");
    private final By itemNames      = By.cssSelector("#tbodyid tr td:nth-child(2)");
    private final By itemPrices     = By.cssSelector("#tbodyid tr td:nth-child(3)");
    private final By totalPrice     = By.id("totalp");
    private final By placeOrderBtn  = By.cssSelector("button[data-target='#orderModal']");
    private final By deleteLinks    = By.cssSelector("#tbodyid tr td:nth-child(4) a");

    // ── Order form fields inside the modal ──────────────────
    private final By orderModal     = By.id("orderModal");
    private final By nameField      = By.id("name");
    private final By countryField   = By.id("country");
    private final By cityField      = By.id("city");
    private final By cardField      = By.id("card");
    private final By monthField     = By.id("month");
    private final By yearField      = By.id("year");
    private final By purchaseBtn    = By.cssSelector("#orderModal .btn-primary");

    // ── Confirmation overlay ────────────────────────────────
    private final By confirmTitle   = By.cssSelector(".sweet-alert h2");
    private final By confirmBody    = By.cssSelector(".sweet-alert p.lead");
    private final By confirmOkBtn   = By.cssSelector(".sweet-alert .confirm");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public void waitForCartToLoad() {
        WaitHelper.briefPause(2000);
    }

    // ── Cart item queries ───────────────────────────────────

    public List<String> getCartItemNames() {
        waitForCartToLoad();
        return driver.findElements(itemNames)
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public List<String> getCartItemPrices() {
        return driver.findElements(itemPrices)
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public String getPriceForProduct(String productName) {
        List<WebElement> rows = driver.findElements(cartItems);
        for (WebElement row : rows) {
            String name = row.findElement(By.cssSelector("td:nth-child(2)")).getText();
            if (name.equalsIgnoreCase(productName)) {
                return row.findElement(By.cssSelector("td:nth-child(3)")).getText();
            }
        }
        return null;
    }

    /**
     * Reads the total displayed at the bottom of the cart.
     */
    public int getDisplayedTotal() {
        String text = waitHelper.waitForVisible(totalPrice).getText().trim();
        return Integer.parseInt(text);
    }

    /**
     * Sums every individual item price in the cart table.
     */
    public int calculateSumOfItemPrices() {
        return driver.findElements(itemPrices)
                .stream()
                .mapToInt(el -> Integer.parseInt(el.getText().trim()))
                .sum();
    }

    // ── Order flow ──────────────────────────────────────────

    public void clickPlaceOrder() {
        waitHelper.waitForClickable(placeOrderBtn).click();
        waitHelper.waitForVisible(orderModal);
        WaitHelper.briefPause(500);
    }

    public void fillOrderForm(String name, String country, String city,
                              String card, String month, String year) {
        waitHelper.waitForVisible(nameField).sendKeys(name);
        driver.findElement(countryField).sendKeys(country);
        driver.findElement(cityField).sendKeys(city);
        driver.findElement(cardField).sendKeys(card);
        driver.findElement(monthField).sendKeys(month);
        driver.findElement(yearField).sendKeys(year);
    }

    public void clickPurchase() {
        waitHelper.waitForClickable(purchaseBtn).click();
    }

    /**
     * Submits the order without entering any data — used for the
     * negative/validation scenario.
     */
    public void clickPurchaseWithoutFilling() {
        waitHelper.waitForClickable(purchaseBtn).click();
    }

    // ── Confirmation overlay ────────────────────────────────

    public String getConfirmationTitle() {
        return waitHelper.waitForVisible(confirmTitle).getText();
    }

    public String getConfirmationBody() {
        return waitHelper.waitForVisible(confirmBody).getText();
    }

    public void clickConfirmOk() {
        waitHelper.waitForClickable(confirmOkBtn).click();
    }
}
