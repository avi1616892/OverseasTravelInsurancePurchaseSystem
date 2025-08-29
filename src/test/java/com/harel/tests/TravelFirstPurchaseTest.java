package com.harel.tests;

import com.harel.actions.TravelActions;
import com.harel.config.FrameworkConfig;
import com.harel.driver.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

import java.time.LocalDate;

public class TravelFirstPurchaseTest {

    private WebDriver driver;
    private TravelActions actions;

    private LocalDate departDate;
    private LocalDate returnDate;

    @BeforeClass
    public void setUp() {
        driver = DriverFactory.createChrome();
        actions = new TravelActions(driver);

        LocalDate today = LocalDate.now();
        departDate = today.plusDays(7);

        int requestedDays = 30; // כמה ימים רוצים שיופיעו ב-UI
        boolean uiCountsInclusive = FrameworkConfig.COUNT_INCLUSIVE; // שיהיה מקור אמת אחד

        // אם ה-UI סופר כוללני → ההפרש הבלעדי צריך להיות days-1
        returnDate = departDate.plusDays(uiCountsInclusive ? requestedDays - 1 : requestedDays);

        System.out.printf(
                "[TEST] Depart=%s | Return=%s | requestedDays=%d | inclusive=%s | diffExclusive=%d%n",
                departDate, returnDate, requestedDays, uiCountsInclusive,
                java.time.temporal.ChronoUnit.DAYS.between(departDate, returnDate)
        );
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (driver != null) driver.quit();
    }

    @Test(description = "E2E: רכישת ביטוח נסיעות - זרימה מלאה")
    public void travelPolicyFirstPurchaseFlow() {
        actions.openBaseUrl(FrameworkConfig.BASE_URL);
        actions.clickFirstPurchase();
        actions.pickAnyContinent();
        actions.goToDates();
        actions.chooseDates(departDate, returnDate);
        actions.verifyTotalDays(departDate, returnDate);
        actions.goToPassengers();
        actions.assertPassengersPage();
    }
}
