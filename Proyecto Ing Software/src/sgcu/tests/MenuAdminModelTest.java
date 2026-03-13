package sgcu.tests;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import sgcu.modelos.MenuAdminModel;

public class MenuAdminModelTest {

    private MenuAdminModel menuModel;

    @Before
    public void setUp() {
        menuModel = new MenuAdminModel();
    }

    //caso 1: verificar que los platos se carguen correctamente desde Menu.txt
    @Test
    public void testCargarMenuExitoso() {
        List<MenuAdminModel> lista = MenuAdminModel.cargarTodos();
        
        //verificamos que la lista no sea nula (debe devolver una lista vacía si el archivo no existe)
        assertNotNull("La lista de platos no debe ser nula", lista);
    }

    //caso 2: verificar la conversión de objeto a CSV 
    @Test
    public void testToCSVFormat() {
        //creamos un plato de prueba
        MenuAdminModel plato = new MenuAdminModel(1, "Pabellon", "Plato tipico", 150.0, "Lunes", "Almuerzo");
        
        String resultadoEsperado = "1;Pabellon;Plato tipico;150.0;Lunes;Almuerzo";
        
        
        assertEquals("El formato CSV debe usar ';' como separador", resultadoEsperado, plato.toCSV());
    }

    //caso 3: Probar la logica de guardado de un nuevo plato
    @Test
    public void testGuardarPlato() {
        //aqui intentamos guardar un plato de prueba
        boolean exito = menuModel.guardarPlato("99", "Test Plato", "Desc", "50.0", "Martes", "Desayuno");
        
        assertTrue("El plato debería guardarse correctamente en el archivo", exito);
        
        //esto verifica que ahora aparezca en la lista
        List<MenuAdminModel> listaActualizada = MenuAdminModel.cargarTodos();
        boolean encontrado = listaActualizada.stream().anyMatch(p -> p.getNombre().equals("Test Plato"));
        
        assertTrue("El plato guardado debe encontrarse en la carga de datos", encontrado);
    }
}