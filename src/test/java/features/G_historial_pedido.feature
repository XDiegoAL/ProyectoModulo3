Feature: Revisar historial de pedidos

  Como usuario autenticado
  Quiero poder revisar el historial de pedidos realizados
  Para validar que los pedidos se completaron correctamente y guardar evidencia

  Scenario: Consultar historial y capturar capturas de los pedidos completados
    Given que el usuario inicia sesion con email "prueba3@gmail.com" y contraseña "prueba1234!"
    When el usuario navega al historial de pedidos
    Then toma capturas de pantalla de los pedidos completados