package com.cib.demoblaze.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * Cucumber + TestNG runner.
 * <p>
 * Run all scenarios:
 *   mvn test
 * <p>
 * Run only smoke tests:
 *   mvn test -Dcucumber.filter.tags="@smoke"
 * <p>
 * Run a specific scenario by ID:
 *   mvn test -Dcucumber.filter.tags="@e2e"
 */
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "com.cib.demoblaze.steps",
        plugin = {
                "pretty",
                "html:target/cucumber-reports/cucumber.html",
                "json:target/cucumber-reports/cucumber.json",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        },
        monochrome = true
)
public class TestRunner extends AbstractTestNGCucumberTests {

    /**
     * Enables parallel scenario execution when configured in testng.xml.
     * Each scenario runs as an independent data-provider entry.
     */
    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
