package com.harel.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Assertion helpers on top of TestNG.
 */
public final class AssertUtils {
    private AssertUtils() {}

    public static void assertTrue(boolean condition, String message) {
        Assert.assertTrue(condition, message);
    }

    public static void assertEquals(Object actual, Object expected, String message) {
        Assert.assertEquals(actual, expected, message);
    }

    public static void assertInAllowedSet(int actual, int... allowed) {
        boolean ok = Arrays.stream(allowed).anyMatch(v -> v == actual);
        Assert.assertTrue(ok, "Value " + actual + " not in allowed set " + Arrays.toString(allowed));
    }

    public static void assertEqualsWithTolerance(int actual, int expected, int tolerance) {
        int diff = Math.abs(actual - expected);
        Assert.assertTrue(diff <= tolerance,
                String.format("Expected ~%d (±%d), but was %d", expected, tolerance, actual));
    }

    public static void assertVisible(WebElement el, String message) {
        Assert.assertTrue(el != null && el.isDisplayed(), message);
    }

    public static void assertVisible(WebDriver driver, By by, String message) {
        WebElement el = driver.findElement(by);
        assertVisible(el, message);
    }

    public static void assertTextContains(String haystack, String needle, String message) {
        Assert.assertTrue(
                haystack != null && needle != null && haystack.contains(needle),
                message + " | text='" + haystack + "', expected to contain='" + needle + "'"
        );
    }

    /**
     * Extract the last integer appearing in the given text.
     */
    public static int extractLastInteger(String text) {
        if (text == null) return -1;
        Matcher m = Pattern.compile("(\\d+)(?!.*\\d)").matcher(text.replace('\u200f',' '));
        return m.find() ? Integer.parseInt(m.group(1)) : -1;
    }

    /**
     * Null-safe toString; returns empty string for null.
     */
    public static String nvl(Object o) {
        return Objects.toString(o, "");
    }
}
