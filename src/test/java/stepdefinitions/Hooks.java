package stepdefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

public class Hooks {
    private static WebDriver driver;

    public static WebDriver getDriver() {
        return driver;
    }

    public static void setDriver(WebDriver webDriver) {
        driver = webDriver;
    }

    @Before
    public void setUp() {
        // SIEMPRE crear un driver nuevo antes de cada escenario
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        System.out.println("[HOOK] WebDriver inicializado.");
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            driver = null;
            System.out.println("[HOOK] WebDriver finalizado.");
        }
    }
}
