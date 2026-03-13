package sgcu.tests;

import static org.junit.Assert.*;
import org.junit.Test;
import sgcu.modelos.CargaCostosModel;

public class CargaCostosModelTest {
    @Test
    public void testCalcularCCB_Estandar() {
        CargaCostosModel modelo = new CargaCostosModel("Enero 2025", 100.0, 200.0, 50, 20.0);
        
        double esperado = 7.2;
        assertEquals("El cálculo del CCB fallo", esperado, modelo.getCcB(), 0.001);
    }
}