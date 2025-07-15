package stepdefinitions;

import io.cucumber.java.en.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.DireccionPage;
import pages.LoginPage;

import java.time.Duration;
import java.util.Map;

public class DireccionSteps {

    private WebDriver driver;
    private DireccionPage direccionPage;
    private LoginPage loginPage;


    @Given("que el usuario inicia sesión con email {string} y contraseña {string}")
    public void iniciarSesion(String email, String password) {
        WebDriver driver = Hooks.getDriver();
        loginPage = new LoginPage(driver);
        loginPage.cerrarSesionSiEsNecesario();
        loginPage.open();
        loginPage.login(email, password);
        direccionPage = new DireccionPage(driver);
    }


    @When("navega a la sección de direcciones")
    public void navegarSeccionDirecciones() {
        //direccionPage = new DireccionPage(driver);
        direccionPage.navigateToAddressSection();
        direccionPage.clickAddNewAddress();
    }

    @And("agrega una dirección con los siguientes datos:")
    public void agregarDireccion1(io.cucumber.datatable.DataTable dataTable) {
        Map<String, String> data = dataTable.asMaps().get(0);
        direccionPage.fillAddressForm(
                data.get("country"),
                data.get("name"),
                data.get("mobile"),
                data.get("zip"),
                data.get("address"),
                data.get("city"),
                data.get("state")
        );
        direccionPage.submitAddress();
    }

    @And("agrega una segunda dirección con los siguientes datos:")
    public void agregarDireccion2(io.cucumber.datatable.DataTable dataTable) {
        // Antes de llenar el formulario, haz clic en el botón
        direccionPage.clickAddNewAddress();

        // Llenar el formulario
        Map<String, String> data = dataTable.asMaps().get(0);
        direccionPage.fillAddressForm(
                data.get("country"),
                data.get("name"),
                data.get("mobile"),
                data.get("zip"),
                data.get("address"),
                data.get("city"),
                data.get("state")
        );
        direccionPage.submitAddress();

        direccionPage.navigateToAddressSection();
    }

    @Then("deberían mostrarse ambas direcciones en la lista")
    public void verificarDirecciones() {
        Assert.assertTrue(direccionPage.isAddressVisible("Calle 1, 123"));
        Assert.assertTrue(direccionPage.isAddressVisible("Calle 2, 456"));
    }
}

