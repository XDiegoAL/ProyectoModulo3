Feature: Gestión de direcciones de envío

  Scenario: Agregar dos direcciones de envío
    Given que el usuario inicia sesión con email "prueba1@gmail.com" y contraseña "prueba1234!"
    When navega a la sección de direcciones
    And agrega una dirección con los siguientes datos:
      | country | name      | mobile     | zip   | address       | city | state |
      | Perú    | Pedro Arias | 987654765 | 15000 | Calle 1, 123  | Lima | Lima  |
    And agrega una segunda dirección con los siguientes datos:
      | country | name      | mobile     | zip   | address       | city | state |
      | Perú    | Ana López | 987654322 | 15001 | Calle 2, 456  | Lima | Lima  |
    Then deberían mostrarse ambas direcciones en la lista
