package com.cib.demoblaze.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page object for the individual product details view.
 * <p>
 * URL pattern: https://www.demoblaze.com/prod.html?idp_=<id>
 */
public class ProductDetailsPage extends BasePage {

    private final By productName  = By.cssSelector(".name");
    private final By productPrice = By.cssSelector(".price-container");
    private final By addToCartBtn = By.cssSelector("a.btn-success");

    public ProductDetailsPage(WebDriver driver) {
        super(driver);
    }

    public String getProductName() {
        return waitHelper.waitForVisible(productName).getText();
    }

    /**
     * Returns the raw price text (e.g. "$400 *includes tax").
     * Callers can parse/trim as needed.
     */
    public String getProductPrice() {
        String rawPrice = waitHelper.waitForVisible(productPrice).getText();
        // The site shows something like "$400 *includes tax" — grab the dollar amount
        if (rawPrice.contains("*")) {
            rawPrice = rawPrice.substring(0, rawPrice.indexOf("*")).trim();
        }
        return rawPrice;
    }

    public void clickAddToCart() {
        waitHelper.waitForClickable(addToCartBtn).click();
    }
}
