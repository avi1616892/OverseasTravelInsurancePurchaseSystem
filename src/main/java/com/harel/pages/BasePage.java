package com.harel.pages;

import com.harel.utils.Clicks;
import com.harel.utils.SendKeys;
import com.harel.utils.Waits;
import com.harel.utils.Js;
import org.openqa.selenium.WebDriver;

/**
 * Base page wiring common utilities.
 */
public abstract class BasePage {
    protected final WebDriver driver;
    protected final Waits waits;
    protected final Clicks clicks;
    protected final Js js;
    protected final SendKeys sendKeys;

    /**
     * Construct a base page binding the driver and utility helpers.
     */
    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.waits = new Waits(driver);
        this.clicks = new Clicks(driver, waits);
        this.js = new Js(driver);
        this.sendKeys = new SendKeys(driver, waits);
    }
}
