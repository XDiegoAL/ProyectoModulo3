Feature: Compra de productos usando búsqueda y completando la orden

  Scenario: Agregar productos a la cesta y realizar compra con dirección y método de pago
    Given que el usuario inicia sesion con email "prueba3@gmail.com" y contraseña "prueba1234!"
    When busca y agrega a la cesta los productos:
      | apple juice|
      | banana juice|
      | t-shirt|
    And procede al checkout
    And selecciona la segunda dirección de envío
    And selecciona el tipo de entrega "Standard Delivery"
    And selecciona el primer método de pago
    Then la orden debería completarse exitosamente