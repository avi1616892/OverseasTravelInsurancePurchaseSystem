package com.harel.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Small JS helpers.
 */
public class Js {
    private final WebDriver driver;

    /**
     * Create a Js helper bound to a driver.
     */
    public Js(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Scroll the element into view (center block).
     */
    public void scrollIntoViewCenter(WebElement el) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", el);
    }
}
