package org.overlord.commons.test.ui.cases;

import org.jboss.arquillian.graphene.page.Page;
import org.junit.Test;
import org.overlord.commons.test.ui.pages.LoginPage;


public class LoginTestCase extends AbstractTestCase {


    @Page
    private LoginPage loginPage;

    @Test
    public void assertLogin() throws Exception {
        loginPage.login();
        loginPage.assertLoginPage();
    }

}
