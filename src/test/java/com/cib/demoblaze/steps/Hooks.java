package com.cib.demoblaze.steps;

import com.cib.demoblaze.utils.DriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/**
 * Cucumber lifecycle hooks.
 * <p>
 * Every scenario gets a fresh browser instance so tests never
 * depend on leftover state from a previous run.
 */
public class Hooks {

    @Before
    public void setUp() {
        DriverFactory.initDriver();
    }

    @After
    public void tearDown(Scenario scenario) {
        WebDriver driver = DriverFactory.getDriver();

        // Attach a screenshot when a scenario fails — makes debugging much easier
        if (scenario.isFailed() && driver != null) {
            try {
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", scenario.getName() + "_failure");
            } catch (Exception e) {
                System.err.println("[Hooks] Could not capture failure screenshot: " + e.getMessage());
            }
        }

        DriverFactory.quitDriver();
    }
}
