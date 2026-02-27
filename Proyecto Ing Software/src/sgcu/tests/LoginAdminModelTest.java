package sgcu.tests;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import sgcu.modelos.LoginAdminModel;
import sgcu.modelos.Usuario;

public class LoginAdminModelTest {

    private LoginAdminModel loginAdminModel;

    @Before
    public void setUp() {
        loginAdminModel = new LoginAdminModel();
    }

    // CASO 1: inicio de sesion exitoso
    @Test
    public void testAutenticarUsuarioExitoso() {
        // Datos
        String user = "prueba";
        String correo = "prueba@ucv.ve";
        String pass = "12345678";

        Usuario resultado = loginAdminModel.autenticarAdmin(user, correo, pass);

        // Asserts
        assertNotNull("Usuario encontrado", resultado);
        assertEquals("prueba", resultado.getNombre());
        assertEquals("Administrador", resultado.getTipo());
    }

     // CASO 2: inicio de sesion incorrecto
    @Test
    public void testAutenticarUsuarioFallido() {
        // Datos (incorrectos)
        String user = "prueba";
        String correo = "prueba@ucv.ve.com";
        String passErronea = "skjdkjsadasd";

        Usuario resultado = loginAdminModel.autenticarAdmin(user, correo, passErronea);

        // Verificación
        assertNull("Retorna null si la contraseña no es valida", resultado);
    }
}