Feature: Flujo completo de pruebas de Juice Shop

  Scenario Outline: Registro con datos válidos
    Given que el usuario abre la página de registro
    When ingresa el email "<email>", contraseña "<password>" y confirma contraseña "<repeatPassword>"
    And hace clic en el botón de registrar
    Then debería ver el mensaje de registro exitoso

    Examples:
      | email             | password    | repeatPassword |
      | primertest@gmail.com  | test1234$$   | test1234$$      |
      | segundotest@hotmail.com  | secondtest23$   | secondtest23$      |

  Scenario: Login con credenciales válidas
    Given que el usuario abre la página de inicio de sesión
    When ingresa el correo "primertest@gmail.com" y la contraseña "test1234$$"
    And hace clic en Login
    Then debería ver el botón de Logout


  Scenario: Login con credenciales inválidas
    Given que el usuario abre la página de inicio de sesión
    When ingresa el correo "testprueba@gmail.com" y la contraseña "incorrecto123$"
    And hace clic en Login
    Then debería ver un mensaje de error de credenciales inválidas

  Scenario: Agregar dos direcciones de envío
    Given que el usuario inicia sesión con email "primertest@gmail.com" y contraseña "test1234$$"
    When navega a la sección de direcciones
    And agrega una dirección con los siguientes datos:
      | country | name      | mobile     | zip   | address       | city | state |
      | Perú    | Pedro Arias | 987654765 | 15000 | Calle 1, 123  | Lima | Lima  |
    And agrega una segunda dirección con los siguientes datos:
      | country | name      | mobile     | zip   | address       | city | state |
      | Perú    | Ana López | 987654322 | 15001 | Calle 2, 456  | Lima | Lima  |
    Then deberían mostrarse ambas direcciones en la lista

  Scenario: Agregar dos métodos de pago
    Given que el usuario accede con email "primertest@gmail.com" y contraseña "test1234$$"
    When navega a la sección de métodos de pago
    And agrega un método de pago con los siguientes datos:
      | cardName       | cardNumber        | expiryMonth | expiryYear |
      | Tarjeta Prueba | 4111111111111111  | 12          | 2090       |
    And agrega un segundo método de pago con los siguientes datos:
      | cardName       | cardNumber        | expiryMonth | expiryYear |
      | Tarjeta Secund | 4000056655665556  | 11          | 2091       |
    Then deberían mostrarse ambos métodos de pago en la lista

  Scenario: Agregar productos a la cesta y realizar compra con dirección y método de pago
    Given que el usuario inicia sesion con email "primertest@gmail.com" y contraseña "test1234$$"
    When busca y agrega a la cesta los productos:
      | apple juice|
      | banana juice|
      | t-shirt|
    And procede al checkout
    And selecciona la segunda dirección de envío
    And selecciona el tipo de entrega "Standard Delivery"
    And selecciona el primer método de pago
    Then la orden debería completarse exitosamente

  Scenario: Comprar 2 productos aleatorios del catálogo
    Given que el usuario inicia sesion con email "primertest@gmail.com" y contraseña "test1234$$"
    When agrega aleatoriamente 2 productos a la cesta
    And procede al checkout
    And selecciona la segunda dirección de envío
    And selecciona el tipo de entrega "Standard Delivery"
    And selecciona el primer método de pago
    Then la orden debería completarse exitosamente

  Scenario: Consultar historial y capturar capturas de los pedidos completados
    Given que el usuario inicia sesion con email "primertest@gmail.com" y contraseña "test1234$$"
    When el usuario navega al historial de pedidos
    Then toma capturas de pantalla de los pedidos completados
