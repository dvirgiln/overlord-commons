package org.overlord.commons.test.ui.pages;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

public abstract class AbstractPage {

    @Drone
    protected WebDriver driver;


    public AbstractPage() {
    }


    protected boolean isElementPresent(By by) {
        try {
          driver.findElement(by);
          return true;
        } catch (NoSuchElementException e) {
          return false;
        }
      }

    public WebDriver getDriver() {
        return driver;
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

}

