package com.harel.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Utility class for sending text and dates into input fields.
 * Provides clearing strategies, date handling, and JS fallback when needed.
 */
public class SendKeys {

    private static final Logger logger = LogManager.getLogger(SendKeys.class);

    private final WebDriver driver;
    private final Waits waits;

    private static final DateTimeFormatter UI_DATE_FMT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy").withLocale(new Locale("he", "IL"));
    private static final DateTimeFormatter UI_DATE_DIGITS_FMT =
            DateTimeFormatter.ofPattern("ddMMyyyy").withLocale(new Locale("he", "IL"));

    /**
     * Create a SendKeys helper bound to a driver and waits helper.
     */
    public SendKeys(WebDriver driver, Waits waits) {
        this.driver = driver;
        this.waits = waits;
    }

    /**
     * Type a LocalDate into an element using dd/MM/yyyy format.
     */
    public void setText(By by, LocalDate date) {
        String value = UI_DATE_FMT.format(date);
        setText(by, value);
    }

    /**
     * Type a string into an element with robust clear and JS fallback.
     */
    public void setText(By by, String value) {
        WebElement el = new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.visibilityOfElementLocated(by));

        scrollIntoViewCenter(el);
        try {
            el.click();
        } catch (Exception ignored) {}

        hardClear(el);

        try {
            el.sendKeys(value);
        } catch (InvalidElementStateException ex) {
            logger.debug("InvalidElementState (incl. not interactable): fallback to JS set. {}", ex.toString());
            jsSetValue(el, value);
        } catch (StaleElementReferenceException stale) {
            logger.warn("Stale during sendKeys, retrying once...");
            WebElement fresh = new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(by));
            try {
                fresh.sendKeys(value);
            } catch (Exception e) {
                jsSetValue(fresh, value);
            }
        }

        String read = readValue(by);
        if (!value.equals(read)) {
            jsSetValue(el, value);
        }
        logger.info("Set '{}' into {}", value, by);
    }

    /**
     * Set a date into an input field.
     * If placeholder contains dd/mm/yyyy → types digits ddMMyyyy so the mask inserts slashes.
     * Otherwise → types dd/MM/yyyy.
     * Includes JS fallback if typing fails.
     */
    public void setDateAutodetect(By by, LocalDate date) {
        WebElement el = waits.visible(by);
        scrollIntoViewCenter(el);
        try {
            el.click();
        } catch (Exception ignored) {}

        String placeholder = safeAttr(el, "placeholder");
        boolean masked = placeholder != null && placeholder.toLowerCase().contains("dd/mm/yyyy");

        hardClear(el);

        if (masked) {
            String digits = UI_DATE_DIGITS_FMT.format(date);
            for (char c : digits.toCharArray()) {
                try {
                    el.sendKeys(String.valueOf(c));
                } catch (InvalidElementStateException ex) {
                    jsSetValue(el, UI_DATE_FMT.format(date));
                    break;
                }
                sleep(30);
            }
            try {
                el.sendKeys(Keys.TAB);
            } catch (Exception ignored) {}
        } else {
            String value = UI_DATE_FMT.format(date);
            try {
                el.sendKeys(value);
            } catch (InvalidElementStateException ex) {
                jsSetValue(el, value);
            }
        }

        String expected = UI_DATE_FMT.format(date);
        String read = readValue(by);
        if (!expected.equals(read)) {
            jsSetValue(el, expected);
        }
        logger.info("Date set (auto) '{}' into {}", expected, by);
    }

    /**
     * Press Enter on the given element.
     */
    public void pressEnter(By by) {
        waits.visible(by).sendKeys(Keys.ENTER);
    }

    /**
     * Press Tab on the given element.
     */
    public void pressTab(By by) {
        waits.visible(by).sendKeys(Keys.TAB);
    }

    /**
     * Press Escape on the given element.
     */
    public void pressEscape(By by) {
        waits.visible(by).sendKeys(Keys.ESCAPE);
    }

    /**
     * Read the input's value or fallback to its text.
     */
    public String readValue(By by) {
        WebElement el = waits.visible(by);
        String v = safeAttr(el, "value");
        if (v != null && !v.isEmpty()) return v;
        String t = safeText(el);
        return t == null ? "" : t;
    }

    /**
     * Clear the given element using multiple strategies.
     */
    public void clear(By by) {
        WebElement el = waits.visible(by);
        scrollIntoViewCenter(el);
        try {
            el.click();
        } catch (Exception ignored) {}
        hardClear(el);
    }

    private void hardClear(WebElement el) {
        try {
            el.sendKeys(Keys.chord(Keys.CONTROL, "a"));
            el.sendKeys(Keys.DELETE);
            el.sendKeys(Keys.chord(Keys.META, "a"));
            el.sendKeys(Keys.DELETE);
            String cur = safeAttr(el, "value");
            if (cur != null && !cur.isEmpty()) el.clear();
            sleep(50);
        } catch (Exception ignored) {}
    }

    private void scrollIntoViewCenter(WebElement el) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center', inline:'nearest'});", el);
    }

    private void jsSetValue(WebElement el, String v) {
        ((JavascriptExecutor) driver).executeScript(
                "const el=arguments[0], v=arguments[1];" +
                        "const tgt = (el.tagName||'').toLowerCase()==='input'||(el.tagName||'').toLowerCase()==='textarea' " +
                        "  ? el : el.querySelector('input,textarea');" +
                        "if(!tgt) return;" +
                        "const last = tgt.value;" +
                        "tgt.focus();" +
                        "tgt.value = v;" +
                        "if(last !== v) {" +
                        "  tgt.dispatchEvent(new Event('input', {bubbles:true}));" +
                        "  tgt.dispatchEvent(new Event('change',{bubbles:true}));" +
                        "}" +
                        "tgt.blur();",
                el, v
        );
    }

    private String safeAttr(WebElement el, String name) {
        try {
            return el.getAttribute(name);
        } catch (Exception e) {
            return null;
        }
    }

    private String safeText(WebElement el) {
        try {
            String t = el.getText();
            return t == null ? null : t.trim();
        } catch (Exception e) {
            return null;
        }
    }

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {}
    }
}
