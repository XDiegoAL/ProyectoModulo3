package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class CheckoutPage {
    private WebDriver driver;
    private WebDriverWait wait;

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    public void seleccionarSegundaDireccion() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        // Esperar que la segunda fila aparezca Y sea visible
        By segundaFilaLocator = By.xpath("(//mat-row)[2]");
        WebElement segundaFila = wait.until(ExpectedConditions.visibilityOfElementLocated(segundaFilaLocator));

        // Dentro de la fila, buscar el radio button
        WebElement radioButton = segundaFila.findElement(By.tagName("mat-radio-button"));

        // Hacer clic al radio button
        radioButton.click();

        // Clic en el siguiente botón Continue
        clickBotonContinue();
    }

    public void procederEntrega() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        // Esperar que overlay desaparezca si existiera
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector(".cdk-overlay-backdrop"))
        );

        // Localizar botón robusto
        WebElement boton = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//button[contains(., 'Continue') or contains(., 'Proceed')]")
        ));

        wait.until(ExpectedConditions.elementToBeClickable(boton));

        boton.click();

        System.out.println("Botón Continue clickeado.");
    }

    // NUEVO MÉTODO para click en "Continue"
    public void clickBotonContinue() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        By botonLocator = By.xpath("//button[.//span[contains(., 'Continue')]]");

        // 1. Espera visibilidad del botón
        WebElement boton = wait.until(ExpectedConditions.visibilityOfElementLocated(botonLocator));

        // 2. Espera que esté habilitado
        wait.until(driver -> boton.isEnabled());

        // 3. Espera que sea clickeable
        wait.until(ExpectedConditions.elementToBeClickable(botonLocator));

        // 4. Clic
        boton.click();
    }
    public void seleccionarDeliveryYContinuar(String metodoEntrega) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(40));

        // Esperar a que desaparezca el spinner
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector(".mat-spinner, .mat-progress-spinner")));

        // Esperar a que aparezca la tabla
        WebElement tablaEntrega = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//mat-row//mat-cell[contains(text(),'" + metodoEntrega + "')]")));

        // Hacer clic en el método de entrega
        tablaEntrega.click();

        // Esperar que desaparezca el spinner de carga si aparece
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector(".mat-spinner, .mat-progress-spinner")));

        // Localizar el botón Continue
        WebElement btnContinue = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button//span[contains(text(),'Continue')]/..")));

        btnContinue.click();
    }

    public void seleccionarMetodoPago(int index) {
        List<WebElement> radios = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                By.cssSelector("mat-radio-button")
        ));
        radios.get(index - 1).click();
    }


    public void procederResumen() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(40));
        WebElement botonContinue = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[.//span[contains(text(), 'Continue')]]")
        ));
        botonContinue.click();
    }



    public void confirmarOrden() {
        WebElement confirm = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("checkoutButton")
        ));
        confirm.click();
    }

    public boolean mensajeConfirmacionVisible() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("mat-card .confirmation")
        )).isDisplayed();
    }
}
