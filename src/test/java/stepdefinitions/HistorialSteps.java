package stepdefinitions;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import pages.HomePage;

import java.util.List;

public class HistorialSteps {

    private HomePage homePage;

    public HistorialSteps() {
        WebDriver driver = Hooks.getDriver();
        homePage = new HomePage(driver);
    }

    @When("el usuario navega al historial de pedidos")
    public void navegarHistorialPedidos() {
        homePage.irAlHistorialPedidos();
    }

    @Then("toma capturas de pantalla de los pedidos completados")
    public void capturarPedidosCompletados() {
        List<String> pedidos = homePage.obtenerPedidosCompletadosYCapturarEvidencia();
        if (pedidos.isEmpty()) {
            throw new RuntimeException("No se encontraron pedidos completados.");
        }
    }

}

