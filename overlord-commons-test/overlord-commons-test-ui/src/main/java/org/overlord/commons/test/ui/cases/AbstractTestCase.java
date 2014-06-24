package org.overlord.commons.test.ui.cases;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.openqa.selenium.WebDriver;

public abstract class AbstractTestCase {

    @Drone
    protected WebDriver driver;



}
