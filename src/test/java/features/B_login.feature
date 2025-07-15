Feature: Login en Juice Shop

  Como usuario registrado
  Quiero iniciar sesión en la plataforma
  Para acceder a mi cuenta y realizar compras

  Scenario: Login con credenciales válidas
    Given que el usuario abre la página de inicio de sesión
    When ingresa el correo "prueba3@gmail.com" y la contraseña "prueba1234!"
    And hace clic en Login
    Then debería ver el botón de Logout


  Scenario: Login con credenciales inválidas
    Given que el usuario abre la página de inicio de sesión
    When ingresa el correo "testprueba1@gmail.com" y la contraseña "incorrecto123$"
    And hace clic en Login
    Then debería ver un mensaje de error de credenciales inválidas