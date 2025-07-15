package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebElement;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomePage {
    private WebDriver driver;
    private WebDriverWait wait;
    private By inputBusqueda = By.cssSelector("input[matinput]");
    private By botonLupa = By.xpath("//mat-icon[normalize-space()='search']");


    public HomePage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    public void buscarProducto(String producto) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        Actions actions = new Actions(driver);

        // 1. Esperar que no haya overlays
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector(".cdk-overlay-backdrop")));

        WebElement campoBusqueda;

        // 2. Intentar encontrar la lupa
        List<WebElement> lupas = driver.findElements(
                By.xpath("//mat-icon[normalize-space()='search']")
        );

        if (!lupas.isEmpty()) {
            WebElement searchIcon = lupas.get(0);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", searchIcon);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchIcon);

            campoBusqueda = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("input[matinput]")));
        } else {
            campoBusqueda = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("input[matinput]")));
        }

        // 3. Limpiar input
        wait.until(ExpectedConditions.visibilityOf(campoBusqueda));
        ((JavascriptExecutor) driver).executeScript("arguments[0].focus();", campoBusqueda);

        new Actions(driver)
                .moveToElement(campoBusqueda)
                .click()
                .keyDown(Keys.CONTROL)
                .sendKeys("a")
                .keyUp(Keys.CONTROL)
                .sendKeys(Keys.BACK_SPACE)
                .perform();

        campoBusqueda.sendKeys(producto);
        campoBusqueda.sendKeys(Keys.ENTER);

        // 4. Esperar a que aparezcan los mat-card con los resultados
        List<WebElement> cards = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                By.cssSelector("mat-card")));

        //boolean productoAgregado = false;

        // 5. Recorrer todos los mat-card
        for (WebElement card : cards) {
            System.out.println("Cantidad de mat-card encontrados: " + cards.size());

            boolean productoAgregado = false;

            for (int i = 0; i < cards.size(); i++) {
                card = cards.get(i);
                String nombreProducto = "";

                try {
                    nombreProducto = card.findElement(By.cssSelector(".item-name")).getText();
                    System.out.println("Producto encontrado en card: [" + nombreProducto + "]");
                } catch (NoSuchElementException e) {
                    System.out.println("Card #" + i + " no tiene '.item-name'. Saltando...");
                    continue;
                }

                if (nombreProducto.toLowerCase().contains(producto.toLowerCase())) {
                    System.out.println("Coincide con [" + producto + "]. Intentando agregar...");

                    try {
                        WebElement botonAgregar = card.findElement(
                                By.xpath(".//button[@aria-label='Add to Basket']")
                        );

                        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", botonAgregar);
                        botonAgregar.click();
                        System.out.println("Producto agregado: " + nombreProducto);
                        productoAgregado = true;

                        // Esperar snackbar
                        try {
                            WebElement snackbar = wait.until(ExpectedConditions.visibilityOfElementLocated(
                                    By.xpath("//simple-snack-bar")));
                            wait.until(ExpectedConditions.invisibilityOf(snackbar));
                            System.out.println("Snackbar procesado correctamente.");
                        } catch (TimeoutException e) {
                            System.out.println("Snackbar no apareció o no desapareció.");
                        }

                        break;
                    } catch (Exception e) {
                        System.out.println("Error al hacer click en 'Add to Basket' para " + nombreProducto);
                    }
                } else {
                    System.out.println("Producto [" + producto + "] no coincide con [" + nombreProducto + "]");
                }
            }
        }
    }



    public void closeWelcomeBanner() {
        try {
            WebElement closeButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("button[aria-label='Close Welcome Banner']")
            ));
            closeButton.click();
        } catch (TimeoutException e) {
            // El banner no apareció, no pasa nada
        }
    }
    public void closeWelcomeBannerIfPresent() {
        try {
            WebElement closeButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("button[aria-label='Close Welcome Banner']")
            ));
            closeButton.click();
        } catch (TimeoutException e) {
            // El banner no apareció, no pasa nada
        }
    }
    public void closeCookieBannerIfPresent() {
        try {
            WebElement dismissButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("a[aria-label='dismiss cookie message']")
            ));
            dismissButton.click();
        } catch (TimeoutException e) {
            // Si no aparece, está bien
        }
    }
    public void cerrarBannersYEsperarBusqueda() {
        WebDriverWait waitShort = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebDriverWait waitLong = new WebDriverWait(driver, Duration.ofSeconds(20));

        // 1. Esperar que desaparezca el overlay si existe
        try {
            boolean desaparecio = waitShort.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.cssSelector(".cdk-overlay-backdrop")));
            if (desaparecio) {
                System.out.println("Overlay desapareció solo.");
            } else {
                System.out.println("Overlay sigue visible, intentando cerrarlo...");
            }
        } catch (TimeoutException e) {
            System.out.println("Overlay no desapareció en 10s, buscando botón de cierre...");
            // 2. Intentar cerrar el overlay manualmente
            try {
                WebElement closeButton = driver.findElement(By.cssSelector(".close-dialog"));
                closeButton.click();
                System.out.println("Botón de cerrar overlay presionado.");
                waitShort.until(ExpectedConditions.invisibilityOfElementLocated(
                        By.cssSelector(".cdk-overlay-backdrop")));
                System.out.println("Overlay desapareció tras cerrar manualmente.");
            } catch (NoSuchElementException ex) {
                System.out.println("No se encontró botón de cerrar overlay.");
            }
        }

        // 3. Esperar que la lupa sea clickable (usando nuevo selector que funciona)
        try {
            WebElement searchIcon = waitLong.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//mat-icon[normalize-space()='search']")));
            System.out.println("Botón de búsqueda (lupa) listo para usarse.");
            searchIcon.click();
        } catch (TimeoutException e) {
            System.out.println("No se encontró el botón de búsqueda en el tiempo esperado.");
            throw e; // relanza si prefieres que el test falle aquí
        }
    }

    public void waitForSearchIcon() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//mat-icon[normalize-space()='search']")
        ));
    }

    public void waitForOverlayToDisappear() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector("div.cdk-overlay-backdrop")
        ));
    }

    public void agregarProductoPrimero() {
        WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("mat-card button[aria-label='Add to Basket']")
        ));
        addButton.click();
    }
    public void agregarDosProductosAleatorios() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Esperar a que aparezcan los productos del catálogo
        List<WebElement> productos = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.cssSelector("mat-card")
        ));

        if (productos.size() < 2) {
            throw new RuntimeException("No hay suficientes productos en el catálogo para elegir 2.");
        }

        // Mezclar aleatoriamente
        Collections.shuffle(productos);

        // Agregar los dos primeros
        for (int i = 0; i < 2; i++) {
            WebElement card = productos.get(i);
            WebElement botonAgregar = card.findElement(By.cssSelector("button[aria-label='Add to Basket']"));
            botonAgregar.click();

            // Esperar confirmación del snackbar
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("simple-snack-bar")
            ));

            // Opcional: esperar que desaparezca el snackbar antes del siguiente clic
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.cssSelector("simple-snack-bar")
            ));
        }
    }
    public void irAlHistorialPedidos() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // 1️⃣ Clic en el botón "Account"
        WebElement botonCuenta = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("button[aria-label='Show/hide account menu']")));
        botonCuenta.click();
        System.out.println("✅ Menú de cuenta abierto.");

        // 2️⃣ Clic en "Orders & Payment"
        WebElement ordersPaymentBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button//span[contains(text(),'Orders & Payment')]")));
        ordersPaymentBtn.click();
        System.out.println("✅ 'Orders & Payment' desplegado.");

        // 3️⃣ Clic en "Order History"
        WebElement orderHistoryBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button//span[contains(text(),'Order History')]")));
        orderHistoryBtn.click();
        System.out.println("✅ 'Order History' seleccionado.");

        // 4️⃣ Confirmar que se carga la página de historial
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(text(),'Order History')]")));
        System.out.println("✅ Página de historial de pedidos cargada.");
    }
    public List<String> obtenerPedidosCompletadosYCapturarEvidencia() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        List<String> pedidosCapturados = new ArrayList<>();

        // Espera que al menos una fila de pedido sea visible
        List<WebElement> pedidos = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                By.cssSelector("mat-row") // Cambiado el selector
        ));

        if (pedidos.isEmpty()) {
            System.out.println("❌ No hay pedidos para capturar.");
            return pedidosCapturados;
        }

        int contador = 1;
        for (WebElement pedido : pedidos) {
            String descripcionPedido;
            try {
                // Busca el Order ID desde el encabezado superior (opcional)
                descripcionPedido = driver.findElement(By.cssSelector("div.table-container .heading div:nth-child(1) div:nth-child(2)")).getText();
            } catch (NoSuchElementException e) {
                descripcionPedido = "Pedido_" + contador;
            }

            // Resaltar el pedido antes de capturar
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].style.border='3px solid red'", pedido);

            // Tomar screenshot de toda la pantalla
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            // Guardar la imagen con un nombre único
            String rutaArchivo = "screenshots/pedido_" + descripcionPedido.replaceAll("[^a-zA-Z0-9]", "_") + ".png";
            try {
                File destino = new File(rutaArchivo);
                destino.getParentFile().mkdirs(); // Crear carpeta si no existe
                Files.copy(srcFile.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("✅ Screenshot capturado: " + rutaArchivo);
            } catch (IOException ioe) {
                throw new RuntimeException("Error guardando screenshot: " + ioe.getMessage());
            }

            pedidosCapturados.add(descripcionPedido);
            contador++;
        }

        return pedidosCapturados;
    }
}
