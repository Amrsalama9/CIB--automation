# DemoBlaze QA Automation

Test automation project for the [DemoBlaze](https://demoblaze.com) online store, built with Selenium + Cucumber + TestNG as part of the CIB QA assessment.

Covers 14 scenarios across signup, login, browsing products, cart, checkout, and one full end-to-end flow.

## What you need

- Java 11 or newer
- Maven 3.8+
- Chrome browser

Or just use Codespaces (see below), everything is set up there already.

## How to run

```bash
git clone https://github.com/Amrsalama9/CIB--automation.git
cd CIB--automation

# run everything
mvn clean test

# only smoke tests
mvn clean test -Dcucumber.filter.tags="@smoke"

# only e2e
mvn clean test -Dcucumber.filter.tags="@e2e"

# headless mode
mvn clean test -Dheadless=true
```

## Codespaces

If you don't want to install anything locally, just open the repo on GitHub and go to **Code > Codespaces > Create codespace on main**. It comes with Java, Maven, and Chrome already installed.

Then just run:
```bash
mvn clean test -Dheadless=true
```

## Reports

After running the tests:

```bash
# open cucumber report
open target/cucumber-reports/cucumber.html

# or use allure
mvn allure:serve
```

On CI, both reports get uploaded as artifacts under the Actions tab.

## Project structure

```
src/
├── main/java/com/cib/demoblaze/
│   ├── pages/        # BasePage, HomePage, LoginPage, SignUpPage, ProductDetailsPage, CartPage
│   └── utils/        # ConfigReader, DriverFactory, WaitHelper
└── test/
    ├── java/com/cib/demoblaze/
    │   ├── runners/  # TestRunner
    │   └── steps/    # Hooks, AuthSteps, ProductSteps, CartOrderSteps
    └── resources/
        ├── config.properties
        └── features/ # auth, product_browsing, cart_and_order
```

## Scenarios

| ID | What it tests | Tags |
|----|--------------|------|
| WEB-01 | Register new user | @smoke @P0 |
| WEB-02 | Register with existing username | @P1 @negative |
| WEB-03 | Login with correct credentials | @smoke @P0 |
| WEB-04 | Login with wrong credentials | @P1 @negative |
| WEB-05 | Logout | @P1 |
| WEB-06 | Filter by category (phones/laptops/monitors) | @smoke @P0 |
| WEB-07 | Product card shows right price | @smoke @P0 |
| WEB-08 | Product details page | @P1 |
| WEB-09 | Add item to cart | @smoke @P0 |
| WEB-10 | Cart shows correct product and price | @smoke @P0 |
| WEB-11 | Cart total matches item prices | @P1 |
| WEB-12 | Place order with valid data | @smoke @P0 @e2e |
| WEB-13 | Submit empty order form | @P2 @negative |
| WEB-14 | Full journey: register > login > browse > cart > order > logout | @smoke @P0 @e2e |

## Config

Everything is in `src/test/resources/config.properties` - base URL, credentials, browser, timeouts. You can override any of them from command line like `-Dbrowser=firefox` or `-Dheadless=false`.

## Few things worth mentioning

- Registration tests generate random usernames every run so they don't clash with previous ones
- DriverFactory uses ThreadLocal so it's ready for parallel execution if needed later, but parallel is off for now
- DemoBlaze is heavy on JS modals and AJAX so there are explicit waits everywhere. If you get flaky results, increase `explicit.wait` in config.properties
- The test data uses "Apple monitor 24" at $400 which is what's on the site right now. If they change the catalogue, the feature files will need updating
- CI pipeline runs on every push to main/develop and uploads reports as build artifacts
