package com.cib.demoblaze.steps;

import com.cib.demoblaze.pages.CartPage;
import com.cib.demoblaze.pages.HomePage;
import com.cib.demoblaze.pages.LoginPage;
import com.cib.demoblaze.pages.ProductDetailsPage;
import com.cib.demoblaze.pages.SignUpPage;
import com.cib.demoblaze.utils.DriverFactory;
import com.cib.demoblaze.utils.WaitHelper;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.util.Arrays;
import java.util.List;

/**
 * Step definitions for product browsing, category filtering,
 * and the shared "clicks the X button" step.
 */
public class ProductSteps {

    private WebDriver driver;
    private HomePage homePage;
    private ProductDetailsPage productDetailsPage;
    private SignUpPage signUpPage;
    private LoginPage loginPage;
    private CartPage cartPage;

    private void initPages() {
        driver = DriverFactory.getDriver();
        homePage = new HomePage(driver);
        productDetailsPage = new ProductDetailsPage(driver);
        signUpPage = new SignUpPage(driver);
        loginPage = new LoginPage(driver);
        cartPage = new CartPage(driver);
    }

    // ── Known product-to-category mapping ───────────────────

    private static final List<String> PHONE_PRODUCTS = Arrays.asList(
            "Samsung galaxy s6", "Nokia lumia 1520", "Nexus 6",
            "Samsung galaxy s7", "Iphone 6 32gb", "Sony xperia z5",
            "HTC One M9");

    private static final List<String> LAPTOP_PRODUCTS = Arrays.asList(
            "Sony vaio i5", "Sony vaio i7", "MacBook air",
            "Dell i7 8gb", "2017 Dell 15.6 Inch", "MacBook Pro");

    private static final List<String> MONITOR_PRODUCTS = Arrays.asList(
            "Apple monitor 24", "ASUS Full HD");

    // ── Universal button click handler ──────────────────────
    // Cucumber requires exactly one step definition per pattern.
    // All "clicks the X button" steps route through here.

    @When("the user clicks the {string} button")
    public void userClicksButton(String buttonText) {
        initPages();
        switch (buttonText) {
            case "Sign up":
                signUpPage.clickSignUpButton();
                break;
            case "Log in":
                loginPage.clickLoginButton();
                break;
            case "Add to cart":
                productDetailsPage.clickAddToCart();
                break;
            case "Place Order":
                cartPage.clickPlaceOrder();
                break;
            case "Purchase":
                cartPage.clickPurchase();
                break;
            default:
                throw new IllegalArgumentException("Unknown button: " + buttonText);
        }
    }

    // ── Category filtering ──────────────────────────────────

    @When("the user clicks the {string} category link")
    public void userClicksCategoryLink(String category) {
        initPages();
        homePage.selectCategory(category);
    }

    @Then("the product grid should display only products in {string}")
    public void productGridShouldShowCategory(String category) {
        initPages();
        List<String> displayed = homePage.getDisplayedProductNames();
        Assert.assertFalse(displayed.isEmpty(),
                "No products displayed for category: " + category);

        List<String> expected;
        switch (category.toLowerCase()) {
            case "phones":   expected = PHONE_PRODUCTS;  break;
            case "laptops":  expected = LAPTOP_PRODUCTS; break;
            case "monitors": expected = MONITOR_PRODUCTS; break;
            default:
                throw new IllegalArgumentException("Unknown category: " + category);
        }

        for (String product : displayed) {
            boolean belongs = expected.stream()
                    .anyMatch(e -> e.equalsIgnoreCase(product));
            Assert.assertTrue(belongs,
                    "Product '" + product + "' does not belong in category '" + category + "'");
        }
    }

    // ── Product card assertions ─────────────────────────────

    @Then("the product card for {string} should display price {string}")
    public void productCardShouldShowPrice(String productName, String expectedPrice) {
        initPages();
        WaitHelper.briefPause(1000);
        String actualPrice = homePage.getProductPrice(productName);
        Assert.assertNotNull(actualPrice,
                "Product card for '" + productName + "' was not found on the grid");
        Assert.assertTrue(actualPrice.contains(expectedPrice),
                "Expected price '" + expectedPrice + "' but card shows '" + actualPrice + "'");
    }

    // ── Product details ─────────────────────────────────────

    @When("the user clicks on the product {string}")
    public void userClicksOnProduct(String productName) {
        initPages();
        homePage.clickProduct(productName);
        WaitHelper.briefPause(2000);
    }

    @Then("the product details page should show name {string}")
    public void detailsPageShouldShowName(String expectedName) {
        initPages();
        String actualName = productDetailsPage.getProductName();
        Assert.assertEquals(actualName, expectedName,
                "Product name on details page does not match");
    }

    @Then("the product details page should show price {string}")
    public void detailsPageShouldShowPrice(String expectedPrice) {
        initPages();
        String actualPrice = productDetailsPage.getProductPrice();
        Assert.assertTrue(actualPrice.contains(expectedPrice),
                "Expected price '" + expectedPrice + "' on details page but got '" + actualPrice + "'");
    }
}
