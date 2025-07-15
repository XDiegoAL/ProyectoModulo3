package stepdefinitions;

import io.cucumber.java.en.*;
import io.cucumber.datatable.DataTable;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.CestaPage;
import pages.CheckoutPage;
import pages.HomePage;
import pages.LoginPage;

import java.time.Duration;
import java.util.List;

public class CompraSteps {

    private LoginPage loginPage;
    private HomePage homePage;
    private CestaPage cestaPage;
    private CheckoutPage checkoutPage;

    @Given("que el usuario inicia sesion con email {string} y contraseña {string}")
    public void iniciarSesion(String email, String pass) {
        WebDriver driver = Hooks.getDriver();
        loginPage = new LoginPage(driver);
        loginPage.cerrarSesionSiEsNecesario();
        homePage = new HomePage(driver);          // <-- INICIALIZA TODOS
        cestaPage = new CestaPage(driver);
        checkoutPage = new CheckoutPage(driver);

        loginPage.open();
        loginPage.login(email, pass);
        homePage.cerrarBannersYEsperarBusqueda();

    }
    @When("busca y agrega a la cesta los productos:")
    public void agregarProductos(DataTable dataTable) {
        List<String> listaProductos = dataTable.asList();
        StringBuilder errores = new StringBuilder();

        for (String producto : listaProductos) {
            try {
                homePage.buscarProducto(producto); // Ya no pasas la bandera
            } catch (RuntimeException e) {
                errores.append("Error agregando producto: ").append(producto).append("\n")
                        .append(e.getMessage()).append("\n");
            }
        }

        if (errores.length() > 0) {
            throw new RuntimeException("Se produjeron errores al agregar productos:\n" + errores);
        }
    }


    @And("procede al checkout")
    public void procederCheckout() {
        cestaPage.irACesta();

        int cantidad = cestaPage.obtenerCantidadProductosEnCesta();
        System.out.println("Cantidad de productos en cesta antes de checkout: " + cantidad);
        if (cantidad == 0) {
            throw new RuntimeException("No hay productos en la cesta. Verifica si se agregaron correctamente.");
        }

        cestaPage.procederCheckout();
    }

    @And("selecciona la segunda dirección de envío")
    public void seleccionarSegundaDireccion() {
        checkoutPage.seleccionarSegundaDireccion();
    }

    @And("selecciona el tipo de entrega {string}")
    public void seleccionarMetodoEntrega(String tipoEntrega) {
        checkoutPage.seleccionarDeliveryYContinuar(tipoEntrega);
    }

    @And("selecciona el primer método de pago")
    public void seleccionarMetodoPago() {
        checkoutPage.seleccionarMetodoPago(1);
        checkoutPage.procederResumen();
    }


    @Then("la orden debería completarse exitosamente")
    public void verificarOrdenCompleta() {
        checkoutPage.confirmarOrden();
        Assert.assertTrue(checkoutPage.mensajeConfirmacionVisible());
    }

    @When("agrega aleatoriamente 2 productos a la cesta")
    public void agregarDosProductosAleatorios() {
        homePage.agregarDosProductosAleatorios();
    }

}

