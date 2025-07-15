package stepdefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import pages.LoginPage;

public class LoginSteps {

    WebDriver driver;
    LoginPage loginPage;

    @Given("que el usuario abre la página de inicio de sesión")
    public void abrirLogin() {
        driver = Hooks.getDriver();
        driver.get("http://localhost:3000/#/login");
        loginPage = new LoginPage(driver);
        loginPage.closeWelcomeBanner();
    }

    @When("ingresa el correo {string} y la contraseña {string}")
    public void ingresarCredenciales(String correo, String contrasena) {
        loginPage.enterEmail(correo);
        loginPage.enterPassword(contrasena);
    }

    @When("hace clic en Login")
    public void clickLogin() {
        loginPage.clickLogin();
    }

    @Then("debería ver el botón de Logout")
    public void verificarLogout() {
        loginPage.verifyLogoutIsDisplayed();
    }

    @Then("debería ver un mensaje de error de credenciales inválidas")
    public void verificarMensajeDeError() {
        loginPage.verifyInvalidCredentialsMessageDisplayed();
    }

}
