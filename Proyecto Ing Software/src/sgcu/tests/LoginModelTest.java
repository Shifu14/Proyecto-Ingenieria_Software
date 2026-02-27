package sgcu.tests;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import sgcu.modelos.LoginModel;
import sgcu.modelos.Usuario;

public class LoginModelTest {

    private LoginModel loginModel;

    @Before
    public void setUp() {
        loginModel = new LoginModel();
    }

    // CASO 1: inicio de sesion exitoso
    @Test
    public void testAutenticarUsuarioExitoso() {
        // Datos
        String user = "johan";
        String correo = "johan@gmail.com";
        String pass = "12345678";

        Usuario resultado = loginModel.autenticarUsuario(user, correo, pass);

        // Asserts
        assertNotNull("Usuario encontrado", resultado);
        assertEquals("johan", resultado.getNombre());
        assertEquals("Consumidor", resultado.getTipo());
    }

    // CASO 2: inicio de sesion incorrecto
    @Test
    public void testAutenticarUsuarioFallido() {
        // Datos (incorrectos)
        String user = "prueba";
        String correo = "prueba@gmail.com";
        String passErronea = "skjdkjsadasd";

        Usuario resultado = loginModel.autenticarUsuario(user, correo, passErronea);

        // Verificación
        assertNull("Retorna null si la contraseña no es valida", resultado);
    }
}