package com.harel.actions;

import com.harel.config.FrameworkConfig;
import com.harel.pages.BasePage;
import com.harel.utils.AssertUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static com.harel.locators.TravelLocators.*;

/**
 * High-level flow actions for the travel insurance purchase process.
 * Logs only key steps and validations.
 */
public class TravelActions extends BasePage {

    private static final Logger logger = LogManager.getLogger(TravelActions.class);

    public TravelActions(WebDriver driver) {
        super(driver);
    }

    /**
     * Open a given URL.
     */
    public void openBaseUrl(String url) {
        logger.info("Opening URL: {}", url);
        driver.get(url);
    }

    /**
     * Click the "first purchase" button.
     */
    public void clickFirstPurchase() {
        logger.info("Click: first purchase");
        clicks.click(FIRST_PURCHASE_BTN);
    }

    /**
     * Pick any continent/destination.
     */
    public void pickAnyContinent() {
        logger.info("Selecting a destination card");
        clicks.click(ANY_CONTINENT_CARD);
    }

    /**
     * Proceed to the dates step.
     */
    public void goToDates() {
        logger.info("Navigating to dates step");
        clicks.click(TO_DATES_BTN);
    }

    /**
     * Fill departure and return dates using direct inputs.
     */
    public void chooseDates(LocalDate depart, LocalDate ret) {
        logger.info("Filling dates: depart={}, return={}", depart, ret);
        sendKeys.setText(departureDateField, depart);
        sendKeys.setText(returnDateField,    ret);
    }

    /**
     * Validate total days text with tolerance of 1 day.
     */
    public void verifyTotalDays(LocalDate depart, LocalDate ret) {
        long between = ChronoUnit.DAYS.between(depart, ret);
        long expected = FrameworkConfig.COUNT_INCLUSIVE ? between + 1 : between;

        String text = waits.visible(TOTAL_DAYS_TEXT).getText();
        int found = AssertUtils.extractLastInteger(text);

        AssertUtils.assertTrue(found > 0, "Total days number not found in text: " + text);
        AssertUtils.assertEqualsWithTolerance(found, (int) expected, 1);
        logger.info("Total days validated. expected≈{}, actual={}", expected, found);
    }

    /**
     * Proceed to the passengers step.
     */
    public void goToPassengers() {
        logger.info("Navigating to passengers step");
        clicks.click(TO_PASSENGERS_BTN);
    }

    /**
     * Assert passengers page is visible.
     */
    public void assertPassengersPage() {
        AssertUtils.assertVisible(
                waits.visible(PASSENGERS_HEADER),
                "Passengers page not visible"
        );
        logger.info("Passengers page is visible");
    }
}
