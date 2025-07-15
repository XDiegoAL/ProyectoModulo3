Feature: Registro de usuario

  Scenario Outline: Registro con datos válidos
    Given que el usuario abre la página de registro
    When ingresa el email "<email>", contraseña "<password>" y confirma contraseña "<repeatPassword>"
    And hace clic en el botón de registrar
    Then debería ver el mensaje de registro exitoso

    Examples:
      | email             | password    | repeatPassword |
      | prueba3@gmail.com  | prueba1234!   | prueba1234!      |
      | prueba4@gmail.com  | prueba5678!   | prueba5678!      |

