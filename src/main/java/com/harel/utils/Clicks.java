package com.harel.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

/**
 * Click helpers with visibility/clickability wait and graceful fallbacks.
 */
public class Clicks {
    private static final Logger logger = LogManager.getLogger(Clicks.class);

    private final WebDriver driver;
    private final Waits waits;
    private final Js js;

    /**
     * Create a Clicks helper bound to a driver and waits.
     */
    public Clicks(WebDriver driver, Waits waits) {
        this.driver = driver;
        this.waits = waits;
        this.js = new Js(driver);
    }

    /**
     * Click using standard click; fallback to Actions then JS if needed.
     */
    public void click(By by) {
        WebElement el = waits.clickable(by);
        js.scrollIntoViewCenter(el);
        try {
            el.click();
            logger.info("Clicked {}", by);
        } catch (ElementClickInterceptedException | StaleElementReferenceException e1) {
            logger.debug("Standard click failed, trying Actions. {}", e1.toString());
            try {
                new Actions(driver).moveToElement(el).click().perform();
                logger.info("Clicked via Actions {}", by);
            } catch (Exception e2) {
                logger.debug("Actions click failed, falling back to JS. {}", e2.toString());
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
                logger.info("Clicked via JS {}", by);
            }
        }
    }

    /**
     * Click any visible element that contains the given text (buttons/anchors/spans/divs).
     */
    public void clickVisibleText(String text) {
        By by = By.xpath("//*[self::button or self::a or self::div or self::span]" +
                "[contains(normalize-space(.),'" + text + "') and not(@disabled)]");
        click(by);
    }
}
