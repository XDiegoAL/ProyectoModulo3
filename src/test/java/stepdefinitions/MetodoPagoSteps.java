package stepdefinitions;

import io.cucumber.java.en.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.Assert;
import pages.LoginPage;
import pages.MetodoPagoPage;
import pages.RegistroPage;

import java.util.Map;

public class MetodoPagoSteps {

    private WebDriver driver;
    private LoginPage loginPage;
    private MetodoPagoPage metodoPagoPage;

    @Given("que el usuario accede con email {string} y contraseña {string}")
    public void iniciarSesionMetodoPago(String email, String password) {
        WebDriver driver = Hooks.getDriver();
        loginPage = new LoginPage(driver);
        loginPage.cerrarSesionSiEsNecesario();
        loginPage.open();
        loginPage.login(email, password);

        metodoPagoPage = new MetodoPagoPage(driver);
    }

    @When("navega a la sección de métodos de pago")
    public void navegarSeccionMetodosPago() {
        metodoPagoPage.navigateToPaymentMethodsSection();
        metodoPagoPage.clickAddNewCard();
    }

    @And("agrega un método de pago con los siguientes datos:")
    public void agregarMetodoPago1(io.cucumber.datatable.DataTable dataTable) {
        Map<String, String> data = dataTable.asMaps().get(0);
        metodoPagoPage.fillPaymentForm(
                data.get("cardName"),
                data.get("cardNumber"),
                data.get("expiryMonth"),
                data.get("expiryYear")
        );
        metodoPagoPage.submitPayment();
    }

    @And("agrega un segundo método de pago con los siguientes datos:")
    public void agregarMetodoPago2(io.cucumber.datatable.DataTable dataTable) {
        metodoPagoPage.clickAddNewCard();
        Map<String, String> data = dataTable.asMaps().get(0);
        metodoPagoPage.fillPaymentForm(
                data.get("cardName"),
                data.get("cardNumber"),
                data.get("expiryMonth"),
                data.get("expiryYear")
        );
        metodoPagoPage.submitPayment();
    }

    @Then("deberían mostrarse ambos métodos de pago en la lista")
    public void verificarMetodosPago() {
        Assert.assertTrue("No se encontró la tarjeta terminada en 1111",
                metodoPagoPage.isCardVisible("1111"));
        Assert.assertTrue("No se encontró la tarjeta terminada en 5556",
                metodoPagoPage.isCardVisible("5556"));
    }
}
