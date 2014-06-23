package org.overlord.commons.test.ui;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.overlord.commons.test.ui.pages.LoginPage;


public class AbstractOverlordTestSuite extends AbstractTestSuite {

    boolean logged;


    /*
     * @Page private OverlordPage overlordPage;
     */
    @Drone
    protected WebDriver driver;

    @Page
    private LoginPage loginPage;
    protected final Logger log = Logger.getLogger(AbstractOverlordTestSuite.class);

    public AbstractOverlordTestSuite() {
        super();
        logged = false;
    }

    /*
     * @PostConstruct public static void setUp() {
     *
     * driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS); wait =
     * new WebDriverWait(driver, 60);
     *
     * }
     *
     * @PreDestroy public void tearDown() { overlordPage.assertLogoutButton();
     * overlordPage.logout(); }
     */


    /*
     * Webdriver hack for wait.
     */
    public void before() {
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

    }

    protected void dummyWait(int sec) {
        try {
            new WebDriverWait(driver, sec).until(ExpectedConditions.visibilityOfElementLocated(By
                    .className("dummy-wait")));
        } catch (TimeoutException ex) {
            log.debug("=== Dummy wait ===");
        }
    }

    protected void login() throws Exception {
        if (!logged) {
            // LoginPage loginPage = PageFactory.initElements(driver,
            // LoginPage.class);
            if (loginPage != null) {
                logged = loginPage.login();
            }
        }
    }

    public boolean isLogged() {
        return logged;
    }



}
