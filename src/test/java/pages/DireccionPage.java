package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.List;

public class DireccionPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Selectores
    private By addAddressButton = By.cssSelector("button[aria-label='Add a new address']");
    private By countryField = By.cssSelector("input[placeholder='Please provide a country.']");
    private By nameField = By.cssSelector("input[placeholder='Please provide a name.']");
    private By mobileField = By.cssSelector("input[placeholder='Please provide a mobile number.']");
    private By zipField = By.cssSelector("input[placeholder='Please provide a ZIP code.']");
    private By addressField = By.cssSelector("textarea[placeholder='Please provide an address.']");
    private By cityField = By.cssSelector("input[placeholder='Please provide a city.']");
    private By stateField = By.cssSelector("input[placeholder='Please provide a state.']");
    private By submitButton = By.cssSelector("button#submitButton");
    private By addressCells = By.cssSelector("mat-cell.cdk-column-Address");


    public DireccionPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void navigateToAddressSection() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        Actions actions = new Actions(driver);

        // Cerrar overlays si los hay
        cerrarOverlays();

        //Click en Account
        WebElement accountButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("navbarAccount")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", accountButton);

        //Localizar Orders & Payment por aria-label
        By ordersPaymentLocator = By.xpath("//button[@aria-label='Show Orders and Payment Menu']");
        WebElement ordersPayment = wait.until(ExpectedConditions.visibilityOfElementLocated(ordersPaymentLocator));

        //Mover el mouse para que aparezca el submenú
        actions.moveToElement(ordersPayment).perform();

        //Esperar que el botón "My saved addresses" sea visible
        By myAddressesButtonLocator = By.xpath("//button[@aria-label='Go to saved address page']");
        WebElement myAddressesButton = wait.until(ExpectedConditions.visibilityOfElementLocated(myAddressesButtonLocator));

        //Esperar que no haya overlays visibles que bloqueen el clic
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".cdk-overlay-backdrop")));
        } catch (Exception e) {
            System.out.println("No había overlay visible.");
        }

        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".mat-mdc-snack-bar-container")));
        } catch (Exception e) {
            System.out.println("No había snackbar visible.");
        }

        //Scroll al botón y clic con JavaScript
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", myAddressesButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", myAddressesButton);

        //Confirmar que cargó la sección
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//button//span[contains(., 'Add New Address')]")));
    }


    public void clickAddNewAddress() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement addNewAddressButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@aria-label='Add a new address']")
        ));
        addNewAddressButton.click();
    }


    public void fillAddressForm(String country, String name, String mobile, String zip, String address, String city, String state) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(countryField)).sendKeys(country);
        driver.findElement(nameField).sendKeys(name);
        driver.findElement(mobileField).sendKeys(mobile);
        driver.findElement(zipField).sendKeys(zip);
        driver.findElement(addressField).sendKeys(address);
        driver.findElement(cityField).sendKeys(city);
        driver.findElement(stateField).sendKeys(state);
    }
    public void waitForSnackbarToDisappear() {
        try {
            By snackbar = By.cssSelector("simple-snack-bar, .mat-mdc-snack-bar-container");
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            shortWait.until(ExpectedConditions.invisibilityOfElementLocated(snackbar));
        } catch (TimeoutException e) {
            // Si no aparece, continúa sin error
        }
    }

    public void submitAddress() {
        waitForSnackbarToDisappear();
        wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
    }

    public boolean isAddressVisible(String addressText) {
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.visibilityOfElementLocated(addressCells));

        List<WebElement> cells = driver.findElements(addressCells);
        return cells.stream().anyMatch(cell -> cell.getText().contains(addressText));
    }

    public void cerrarOverlays() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.cssSelector(".cdk-overlay-backdrop")));
        } catch (Exception e) {
            // Ignorar si no hay overlay
        }
    }

}
