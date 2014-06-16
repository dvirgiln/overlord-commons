package org.overlord.commons.test.ui.cases;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.overlord.commons.test.ui.AbstractTestSuite;
import org.overlord.commons.test.ui.SuiteConstants;
import org.overlord.commons.test.ui.SuiteProperties;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

public class LogoutTestCase extends AbstractTestCase {

    @Factory(dataProviderClass = AbstractTestSuite.class, dataProvider = "browser")
    public LogoutTestCase(WebDriver browser) {
        super(browser);

    }

    @Test
    public void testLogout() throws Exception {
        assertEquals(SuiteProperties.getProperty(SuiteConstants.USERNAME),
                driver.findElement(By.cssSelector("span.overlord-nav-username.overlord-header-username"))
                        .getText());
        driver.findElement(By.cssSelector("span.overlord-nav-username.overlord-header-username")).click();
        Thread.sleep(500);
        driver.findElement(By.linkText("Logout")).click();
        Thread.sleep(500);

        /* TO BE REMOVED WHEN THE LOGOUT REDIRECT CORRECTLY TO LOGIN PAGE */
        driver.get(super.getBaseUrl());
        Thread.sleep(500);

        assertTrue(isElementPresent(By.id("username")));
        assertTrue(isElementPresent(By.name("j_password")));
        assertTrue(isElementPresent(By.cssSelector("input[type=\"submit\"]")));

    }
}
