package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.util.List;

public class MetodoPagoPage {

    private WebDriver driver;
    private WebDriverWait wait;

    public MetodoPagoPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    private By nameField = By.xpath("//input[@type='text' and @aria-required='true']");
    private By cardNumberField = By.xpath("//input[@type='number' and @aria-required='true']");
    private By expiryMonthField = By.xpath("(//select[@aria-required='true'])[1]");
    private By expiryYearField = By.xpath("(//select[@aria-required='true'])[2]");
    private By submitButton = By.id("submitButton");
    private By cardRows = By.cssSelector("mat-row.mat-row");
    private By addNewCardPanelHeader = By.xpath("//mat-panel-title[contains(normalize-space(.), 'Add new card')]");


    public void navigateToPaymentMethodsSection() {

        Actions actions = new Actions(driver);

        // Cerrar overlays si los hay
        cerrarOverlays();

        // 1. Click en Account
        WebElement accountButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("navbarAccount")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", accountButton);

        // 2. Localizar Orders & Payment por aria-label
        By ordersPaymentLocator = By.xpath("//button[@aria-label='Show Orders and Payment Menu']");
        WebElement ordersPayment = wait.until(ExpectedConditions.visibilityOfElementLocated(ordersPaymentLocator));



        // 3. Mover el mouse para que aparezca el submenú
        actions.moveToElement(ordersPayment).perform();

        // 4. Esperar que My Payment Options sea visible
        By myPaymentsLocator = By.xpath("//button//span[contains(normalize-space(.), 'My Payment Options')]");
        WebElement myPayments = wait.until(ExpectedConditions.visibilityOfElementLocated(myPaymentsLocator));

        // 5. Hacer click en My Payments Options
        myPayments.click();

        // 6. Confirmar que cargó la sección de métodos de pago
        By panelDescription = By.xpath("//mat-panel-description[contains(normalize-space(.), 'Add a credit or debit card')]");
        wait.until(ExpectedConditions.visibilityOfElementLocated(panelDescription));

    }

    public void clickAddNewCard() {
        By panelHeader = By.xpath("//mat-expansion-panel-header[.//mat-panel-title[contains(normalize-space(.), 'Add new card')]]");
        WebElement header = wait.until(ExpectedConditions.elementToBeClickable(panelHeader));

        // Revisa si está colapsado
        String expanded = header.getAttribute("aria-expanded");

        if (expanded == null || expanded.equals("false")) {
            header.click();
            // Esperar que el panel se expanda
            wait.until(ExpectedConditions.attributeToBe(panelHeader, "aria-expanded", "true"));
        }

        // Esperar que el campo "Name" esté visible
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='text' and @aria-required='true']")));
    }

    public void expandAddNewCardPanel() {
        WebElement header = wait.until(ExpectedConditions.elementToBeClickable(addNewCardPanelHeader));
        header.click();

        // Opcional: Esperar que el input Name sea visible
        wait.until(ExpectedConditions.visibilityOfElementLocated(nameField));
    }

    public void fillPaymentForm(String name, String number, String month, String year) {
        WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(nameField));
        nameInput.clear();
        nameInput.sendKeys(name);

        WebElement numberInput = driver.findElement(cardNumberField);
        numberInput.clear();
        numberInput.sendKeys(number);

        WebElement monthDropdown = driver.findElement(expiryMonthField);
        Select monthSelect = new Select(monthDropdown);
        monthSelect.selectByVisibleText(month);

        WebElement yearDropdown = driver.findElement(expiryYearField);
        Select yearSelect = new Select(yearDropdown);
        yearSelect.selectByVisibleText(year);
    }

    public void submitPayment() {
        wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
    }

    public boolean isCardVisible(String lastDigits) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//mat-row/mat-cell[contains(@class,'cdk-column-Number') and contains(text(),'" + lastDigits + "')]")
            ));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void cerrarOverlays() {
        try {
            WebDriverWait overlayWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            overlayWait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".cdk-overlay-backdrop")));
        } catch (Exception ignored) {
            // Si no hay overlay, continuar
        }
    }

    private void tomarCapturaPantalla(String nombreArchivo) {
        try {
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(src.toPath(), new File(nombreArchivo).toPath());
            System.out.println("Se tomó captura de pantalla: " + nombreArchivo);
        } catch (IOException ex) {
            System.out.println("No se pudo guardar la captura de pantalla: " + ex.getMessage());
        }
    }
}
