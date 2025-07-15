package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import stepdefinitions.Hooks;

import java.time.Duration;
import java.util.List;

public class RegistroPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Localizadores
    private By emailField = By.id("emailControl");
    private By passwordField = By.id("passwordControl");
    private By repeatPasswordField = By.id("repeatPasswordControl");
    private By securityQuestionSelect = By.cssSelector("mat-select[name='securityQuestion']");
    private By securityAnswerField = By.id("securityAnswerControl");
    private By registerButton = By.id("registerButton");


    public RegistroPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    }

    public void open() {
        driver.get("http://localhost:3000/#/register");
        waitUntilRegistrationPageIsLoaded();
        closeWelcomeBanner();
        cerrarOverlaysSiExisten();
    }

    public void waitUntilRegistrationPageIsLoaded() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailField));
    }

    public void closeWelcomeBanner() {
        try {
            WebElement closeBanner = driver.findElement(By.cssSelector("button[aria-label='Close Welcome Banner']"));
            if (closeBanner.isDisplayed()) {
                closeBanner.click();
            }
        } catch (Exception e) {
            // Ignorar si no existe
        }
    }

    public void enterEmail(String email) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailField)).sendKeys(email);
    }

    public void enterPassword(String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField)).sendKeys(password);
    }

    public void enterRepeatPassword(String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(repeatPasswordField)).sendKeys(password);
    }

    public void selectSecurityQuestion(String questionText) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        By dropdownLocator = By.cssSelector("mat-select[name='securityQuestion']");
        By overlayLocator = By.cssSelector(".cdk-overlay-backdrop");
        By optionLocator = By.xpath("//mat-option//span[contains(text(), '" + questionText + "')]");

        // Esperar que el dropdown sea clickeable
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(dropdownLocator));

        // Si el overlay está visible, esperar que desaparezca
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(overlayLocator));
        } catch (TimeoutException e) {
            // No pasa nada si no estaba presente
        }

        // Hacer scroll
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", dropdown);

        // Clic en el dropdown
        dropdown.click();

        // Esperar que aparezcan las opciones
        wait.until(ExpectedConditions.visibilityOfElementLocated(optionLocator));

        // Clic en la opción deseada
        WebElement option = driver.findElement(optionLocator);
        option.click();
    }

    public void enterSecurityAnswer(String answer) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(securityAnswerField)).sendKeys(answer);
    }

    public void clickRegister() {
        // Esperar que deje de estar deshabilitado
        wait.until(ExpectedConditions.not(
                ExpectedConditions.attributeToBe(registerButton, "disabled", "true")
        ));
        // Esperar que sea clickable
        wait.until(ExpectedConditions.elementToBeClickable(registerButton)).click();
        // Esperar confirmación de éxito o cambio de página
        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(By.cssSelector("simple-snack-bar")),
                    ExpectedConditions.urlContains("login")
            ));
        } catch (TimeoutException e) {
            throw new RuntimeException("No se detectó confirmación tras hacer clic en Registrar.");
        }
    }



    public boolean verifySuccessMessageDisplayed() {
        By successMessage = By.cssSelector("simple-snack-bar");
        try {
            WebElement message = wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage));
            return message.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    public void cerrarOverlaysSiExisten() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Cerrar banner de bienvenida si existe
        List<WebElement> dismissWelcome = driver.findElements(By.cssSelector("button[aria-label='Close Welcome Banner']"));
        if (!dismissWelcome.isEmpty()) {
            dismissWelcome.get(0).click();
            wait.until(ExpectedConditions.invisibilityOf(dismissWelcome.get(0)));
        }

        // Cerrar snackbar si existe
        List<WebElement> snackbars = driver.findElements(By.cssSelector(".mat-simple-snackbar"));
        if (!snackbars.isEmpty()) {
            wait.until(ExpectedConditions.invisibilityOfAllElements(snackbars));
        }

        // Esperar que no queden overlays
        List<WebElement> backdrops = driver.findElements(By.cssSelector(".cdk-overlay-backdrop.cdk-overlay-backdrop-showing"));
        if (!backdrops.isEmpty()) {
            wait.until(ExpectedConditions.invisibilityOfAllElements(backdrops));
        }
    }
    public void cerrarWelcomeBannerSiEsVisible() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement dismissBtn = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[aria-label='Close Welcome Banner']")));
            dismissBtn.click();
            // Esperar hasta que desaparezca el overlay
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("cdk-overlay-backdrop")));
        } catch (Exception e) {
            System.out.println("No se mostró el banner de bienvenida o ya fue cerrado.");
        }
    }

}
