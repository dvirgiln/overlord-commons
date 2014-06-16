package org.overlord.commons.test.ui.cases;

import static org.testng.AssertJUnit.assertEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.overlord.commons.test.ui.AbstractTestSuite;
import org.overlord.commons.test.ui.SuiteConstants;
import org.overlord.commons.test.ui.SuiteProperties;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

public class LoginTestCase extends AbstractTestCase {

    @Factory(dataProviderClass = AbstractTestSuite.class, dataProvider = "browser")
    public LoginTestCase(WebDriver browser) {
        super(browser);

    }

    @Test
    public void testLogin() throws Exception {
        driver.get(super.getBaseUrl());
        Thread.sleep(500);
        WebElement user = driver.findElement(By.name("j_username"));
        user.sendKeys((String) SuiteProperties.getProperty(SuiteConstants.USERNAME));
        WebElement password = driver.findElement(By.name("j_password"));
        password.sendKeys((String) SuiteProperties.getProperty(SuiteConstants.PASSWORD));
        Thread.sleep(500);
        driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
        assertEquals(SuiteProperties.getProperty(SuiteConstants.USERNAME),
                driver.findElement(By.cssSelector("span.overlord-nav-username.overlord-header-username"))
                        .getText());
    }
}
