package pages;

import org.junit.Assert;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

import static org.junit.Assert.assertTrue;

public class LoginPage {
    WebDriver driver;

    // Constructor que inicializa el PageFactory
    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // Mapeo de elementos de la página
    @FindBy(id = "email")
    WebElement emailInput;

    @FindBy(id = "password")
    WebElement passwordInput;

    @FindBy(id = "loginButton")
    WebElement loginButton;

    @FindBy(xpath = "//button[@aria-label='Logout']")
    WebElement logoutButton;

    // Acciones sobre los elementos
    public void enterEmail(String email) {
        emailInput.clear();
        emailInput.sendKeys(email);
    }

    public void enterPassword(String password) {
        passwordInput.clear();
        passwordInput.sendKeys(password);
    }

    public void clickLogin() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Esperar que desaparezca el overlay si está visible
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector(".cdk-overlay-backdrop.cdk-overlay-backdrop-showing")));

        // Opcional: asegurarse de que el botón de login sea clickable
        wait.until(ExpectedConditions.elementToBeClickable(loginButton));

        // Hacer clic en el botón de login
        loginButton.click();
    }

    public void open() {
        driver.get("http://localhost:3000/#/login");
        closeWelcomeBanner();
    }

    public void login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickLogin();
    }

    public void verifyLogoutIsDisplayed() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Paso 1: Hacer clic en el botón de cuenta
        WebElement accountButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("navbarAccount")));
        accountButton.click();

        // Paso 2: Esperar que aparezca el botón de Logout
        WebElement logoutButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("navbarLogoutButton")));

        // Paso 3: Verificar que esté visible
        if (!logoutButton.isDisplayed()) {
            throw new AssertionError("El botón de Logout no está visible.");
        }
    }

    public void verifyInvalidCredentialsMessageDisplayed() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(text(),'Invalid email or password')]")
        ));
        Assert.assertTrue("El mensaje de error no está visible", errorMessage.isDisplayed());
    }


    public void closeWelcomeBanner() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            // Esperar a que el botón de cerrar esté visible
            WebElement dismissButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("button[aria-label='Close Welcome Banner']")));
            dismissButton.click();

            // Esperar que el overlay ya no esté en el DOM
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.cssSelector("div.cdk-overlay-backdrop")));
        } catch (Exception e) {
            // Si no aparece, no hacer nada
        }
    }

    public void cerrarSesionSiEsNecesario() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement accountBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("navbarAccount")));
            accountBtn.click();

            WebElement logoutBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("navbarLogoutButton")));
            if (logoutBtn.isDisplayed()) {
                logoutBtn.click();
                System.out.println("[INFO] Se cerró sesión desde LoginPage.");
            }
        } catch (TimeoutException e) {
            System.out.println("[INFO] No había sesión activa.");
        }
    }
}

