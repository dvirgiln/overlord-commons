package org.overlord.commons.test.ui.pages;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.overlord.commons.test.ui.SuiteConstants;
import org.overlord.commons.test.ui.SuiteProperties;

public class LoginPage extends AbstractPage {


    @FindBy(name = "j_username")
    private WebElement user;

    @FindBy(name = "j_password")
    private WebElement password;

    @FindBy(css = "input[type=\"submit\"]")
    private WebElement loginButton;

    public LoginPage() {
        super();

    }

    public boolean login() throws Exception {
        driver.get(super.getBaseUrl());
        user.sendKeys((String) SuiteProperties.getProperty(SuiteConstants.USERNAME));
        password.sendKeys((String) SuiteProperties.getProperty(SuiteConstants.PASSWORD));
        loginButton.click();
        assertEquals(SuiteProperties.getProperty(SuiteConstants.USERNAME),
                driver.findElement(By.cssSelector("span.overlord-nav-username.overlord-header-username"))
                        .getText());
        return true;
    }

    public void assertLoginPage() {
        Assert.assertTrue(user != null && password != null && loginButton != null);
    }
}
