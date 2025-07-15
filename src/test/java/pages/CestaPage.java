package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class CestaPage {
    private WebDriver driver;
    private WebDriverWait wait;

    public CestaPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    public void irACesta() {
        WebElement cestaButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("button[aria-label='Show the shopping cart']")
        ));
        cestaButton.click();
    }

    public void procederCheckout() {
        // Esperar máximo 10s si hay un snackbar visible; si no hay, seguir de inmediato
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.or(
                ExpectedConditions.invisibilityOfElementLocated(By.xpath("//simple-snack-bar")),
                ExpectedConditions.not(ExpectedConditions.visibilityOfElementLocated(By.xpath("//simple-snack-bar")))
        ));

        // Clic en checkout
        WebElement checkoutButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("checkoutButton")
        ));
        checkoutButton.click();


    }
    public int obtenerCantidadProductosEnCesta() {
        // Esperar a que se cargue la tabla con productos
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("mat-table")
        ));

        List<WebElement> filas = driver.findElements(
                By.cssSelector("mat-row")
        );

        return filas.size();
    }

    public void agregarProductoPrimero() {
        WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("button[aria-label='Add to Basket']")
        ));
        addButton.click();
    }
}
