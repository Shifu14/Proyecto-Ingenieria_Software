package sgcu.tests;

import static org.junit.Assert.*;
import org.junit.Test;
import sgcu.modelos.RegistroModel;

public class RegistroModelTest {

    @Test
    public void testToCSVConFoto() {
        

        RegistroModel registro = new RegistroModel("Ana", "ana@ucv.ve", "123", null);
        
        String csv = registro.toCSV();
        String[] partes = csv.split(",");
        
        //verificamos que tenga al menos seis partes (Nombre, Correo, Pass, Tipo, Saldo, Foto)

        assertEquals("Debe tener 6 columnas incluyendo la foto", 6, partes.length);
        assertEquals("El saldo inicial debe ser 0.00", "0.00", partes[4]);
        assertEquals("Debe marcar 'Sin foto' si el archivo es null", "Sin foto", partes[5]);
    }

    @Test
    public void testValidacionExistencia() {
        //probamos la lógica estática de búsqueda
        boolean existe = RegistroModel.existeUsuario("johan");
        //esta prueba pasara si 'johan' está en Consumidores.txt
        assertTrue("Debería detectar usuarios existentes", existe);
    }
}