package com.harel.locators;

import org.openqa.selenium.By;

/**
 * Page locators for the travel flow.
 */
public class TravelLocators {

    public static final By FIRST_PURCHASE_BTN =
            By.xpath("//button[@class='MuiButtonBase-root MuiButton-root jss34 jss13 MuiButton-contained jss12 MuiButton-containedPrimary']");

    public static final By ANY_CONTINENT_CARD =
            By.xpath("//div[text()='ארה\"ב']");

    public static final By TO_DATES_BTN =
            By.xpath("//button[@class='MuiButtonBase-root MuiButton-root jss208 MuiButton-contained jss164 MuiButton-containedPrimary']");

    public static final By departureDateField =
            By.xpath("(//input[@placeholder='dd/mm/yyyy'])[1]");

    public static final By returnDateField =
            By.xpath("(//input[@placeholder='dd/mm/yyyy'])[2]");

    public static final By TOTAL_DAYS_TEXT =
            By.xpath("//span[text()='סה\"כ: 30 ימים']");

    public static final By TO_PASSENGERS_BTN =
            By.xpath("//button[@class='MuiButtonBase-root MuiButton-root jss208 MuiButton-contained MuiButton-containedPrimary']");

    public static final By PASSENGERS_HEADER =
            By.xpath("//h2[text()='נשמח להכיר את הנוסעים שנבטח הפעם']");
}
