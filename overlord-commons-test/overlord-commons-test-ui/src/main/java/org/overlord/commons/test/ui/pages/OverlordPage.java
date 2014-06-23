package org.overlord.commons.test.ui.pages;

import static org.junit.Assert.assertEquals;

import org.apache.commons.configuration.ConfigurationException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.overlord.commons.test.ui.SuiteConstants;
import org.overlord.commons.test.ui.SuiteProperties;

public class OverlordPage extends AbstractPage {
    @FindBy(css = "span.overlord-nav-username.overlord-header-username")
    private WebElement logout_header;

    @FindBy(linkText = "Logout")
    private WebElement logout_link;

    protected String username;

    public OverlordPage() throws ConfigurationException {
        super();
        username = (String) SuiteProperties.getProperty(SuiteConstants.USERNAME);

    }

    public void logout() {

        logout_header.click();
        logout_link.click();

        /* TO BE REMOVED WHEN THE LOGOUT REDIRECT CORRECTLY TO LOGIN PAGE */

        driver.get(super.getBaseUrl());
    }

    public void assertLogoutButton() {
        assertEquals(username, logout_header.getText());
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
