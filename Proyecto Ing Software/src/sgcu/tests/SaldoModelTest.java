package sgcu.tests;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import sgcu.modelos.SaldoModel;

public class SaldoModelTest {

    private SaldoModel saldoModel;

    @Before
    public void setUp() {
        
        saldoModel = new SaldoModel("johan"); 
    }

    @Test
    public void testFlujoCompletoSaldo() {
        String usuario = "johan";
        

        //1 obtener saldo inicial

        double inicial = saldoModel.obtenerSaldo(usuario);
        

        //2 realizar una recarga

        double recarga = 25.50;
        boolean exito = saldoModel.actualizarSaldo(usuario, recarga);
        

        //3 verificar resultados

        assertTrue("La actualización debe ser exitosa", exito);
        double esperado = inicial + recarga;
        assertEquals("El saldo debe haberse incrementado", esperado, saldoModel.obtenerSaldo(usuario), 0.01);
    }
}