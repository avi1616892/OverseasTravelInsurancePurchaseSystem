package com.harel.utils;

import com.harel.config.FrameworkConfig;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

/**
 * Explicit wait utilities wrapping WebDriverWait.
 */
public class Waits {
    private final WebDriver driver;
    private final WebDriverWait wait;

    /**
     * Create a Waits helper using the explicit timeout from FrameworkConfig.
     */
    public Waits(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(FrameworkConfig.EXPLICIT_TIMEOUT_SEC));
    }

    /**
     * Wait until the element is clickable.
     */
    public WebElement clickable(By by) {
        return wait.until(ExpectedConditions.elementToBeClickable(by));
    }

    /**
     * Wait until the element is visible.
     */
    public WebElement visible(By by) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    /**
     * Wait until the element becomes invisible.
     */
    public boolean invisible(By by) {
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
    }

    /**
     * Temporarily set a shorter timeout on the underlying WebDriverWait.
     */
    public void setShortTimeout() {
        wait.withTimeout(Duration.ofSeconds(FrameworkConfig.SHORT_TIMEOUT_SEC));
    }

    /**
     * Restore the default explicit timeout.
     */
    public void setDefaultTimeout() {
        wait.withTimeout(Duration.ofSeconds(FrameworkConfig.EXPLICIT_TIMEOUT_SEC));
    }
}
