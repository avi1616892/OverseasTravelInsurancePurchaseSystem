package com.harel.driver;

import com.harel.config.FrameworkConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * WebDriver factory.
 */
public class DriverFactory {

    private static final Logger logger = LogManager.getLogger(DriverFactory.class);

    /**
     * Create a configured Chrome WebDriver instance.
     */
    public static WebDriver createChrome() {
        logger.info("Setting up ChromeDriver (headless={}, lang={})", FrameworkConfig.HEADLESS, FrameworkConfig.BROWSER_LANG);
        WebDriverManager.chromedriver().setup();
        ChromeOptions co = new ChromeOptions();
        if (FrameworkConfig.HEADLESS) {
            co.addArguments("--headless=new");
        }
        co.addArguments("--start-maximized");
        co.addArguments("--lang=" + FrameworkConfig.BROWSER_LANG);
        WebDriver driver = new ChromeDriver(co);
        logger.info("ChromeDriver created");
        return driver;
    }
}
