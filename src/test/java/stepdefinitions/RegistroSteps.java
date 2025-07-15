package stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.RegistroPage;

import java.time.Duration;

public class RegistroSteps {

    private WebDriver driver;
    private RegistroPage registroPage;



    @Given("que el usuario abre la página de registro")
    public void abrirPaginaRegistro() {

        registroPage = new RegistroPage(Hooks.getDriver());
        registroPage.open();
    }

    @When("ingresa el email {string}, contraseña {string} y confirma contraseña {string}")
    public void ingresarDatosDeRegistro(String email, String password, String repeatPassword) {
        registroPage.enterEmail(email);
        registroPage.enterPassword(password);
        registroPage.enterRepeatPassword(repeatPassword);

        registroPage.cerrarWelcomeBannerSiEsVisible(); // <- CIERRA EL BANNER

        registroPage.selectSecurityQuestion("Your eldest siblings middle name?");
        registroPage.enterSecurityAnswer("TestAnswer");
    }



    @And("hace clic en el botón de registrar")
    public void hacerClicEnRegistrar() {
        registroPage.cerrarOverlaysSiExisten();
        registroPage.clickRegister();
    }

    @Then("debería ver el mensaje de registro exitoso")
    public void verificarRegistroExitoso() {
        boolean mensajeVisible = registroPage.verifySuccessMessageDisplayed();
        Assert.assertTrue("El mensaje de registro no se muestra.", mensajeVisible);
    }
}
