package org.overlord.commons.test.ui;

import org.apache.log4j.Logger;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.overlord.commons.test.ui.pages.LoginPage;

@RunWith(Arquillian.class)
public class AbstractOverlordTestSuite {

    @Drone
    protected WebDriver driver;

    protected final Logger log = Logger.getLogger(AbstractOverlordTestSuite.class);


    public AbstractOverlordTestSuite() {
        super();
    }

    @Page
    LoginPage loginPage;

    @Test
    @InSequence(1)
    @OperateOnDeployment("ui")
    public void init() throws Exception {
        loginPage.login();
        loginPage.assertLoginPage();
    }


}
