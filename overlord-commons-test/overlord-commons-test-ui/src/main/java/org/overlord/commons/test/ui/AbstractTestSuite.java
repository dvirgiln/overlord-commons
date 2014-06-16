package org.overlord.commons.test.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.DataProvider;

public abstract class AbstractTestSuite {
    private static final List<WebDriver> drivers = new ArrayList<WebDriver>();

    @DataProvider(name = "browser")
    public static Object[][] createData1() {
        if (drivers.isEmpty()) {
            WebDriver firefox = new FirefoxDriver();
            drivers.add(firefox);
        }
        Object[][] toReturn = new Object[drivers.size()][1];
        int i = 0;
        for (WebDriver driver : drivers) {
            toReturn[i][0] = driver;
            i++;
        }
        return toReturn;
    }

    @AfterSuite(alwaysRun = true)
    public void endSuite() throws ConfigurationException {
        if (drivers != null) {
            for (WebDriver driver : drivers) {
                driver.quit();
            }
        }

    }




}
