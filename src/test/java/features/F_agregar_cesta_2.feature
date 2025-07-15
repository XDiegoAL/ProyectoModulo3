Feature: Compra de productos en Juice Shop

  Scenario: Comprar 2 productos aleatorios del catálogo
    Given que el usuario inicia sesion con email "prueba3@gmail.com" y contraseña "prueba1234!"
    When agrega aleatoriamente 2 productos a la cesta
    And procede al checkout
    And selecciona la segunda dirección de envío
    And selecciona el tipo de entrega "Standard Delivery"
    And selecciona el primer método de pago
    Then la orden debería completarse exitosamente
