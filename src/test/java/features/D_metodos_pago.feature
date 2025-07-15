Feature: Gestión de métodos de pago

  Scenario: Agregar dos métodos de pago
    Given que el usuario accede con email "prueba3@gmail.com" y contraseña "prueba1234!"
    When navega a la sección de métodos de pago
    And agrega un método de pago con los siguientes datos:
      | cardName       | cardNumber        | expiryMonth | expiryYear |
      | Tarjeta Prueba | 4111111111111111  | 12          | 2090       |
    And agrega un segundo método de pago con los siguientes datos:
      | cardName       | cardNumber        | expiryMonth | expiryYear |
      | Tarjeta Secund | 4000056655665556  | 11          | 2091       |
    Then deberían mostrarse ambos métodos de pago en la lista