package sgcu.modelos;
import javax.swing.table.DefaultTableModel;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MenuConsumidorModel {

    
    private static final String RUTA_ARCHIVO = "src/sgcu/data/data/Menu.txt";

    public DefaultTableModel obtenerDatosMenu() {
        //Definimos las columnas que vera el consumidor
        String[] columnas = {"Nombre", "Descripción", "Precio", "Día", "Hora"};
        
        // Creamos el modelo de tabla no editable
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        //Verificamos si existe el archivo
        File archivo = new File(RUTA_ARCHIVO);
        if (!archivo.exists()) {
            return modelo;
        }

        
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                MenuAdminModel p = MenuAdminModel.fromCSV(linea);
                
                if (p != null) {
                    
                    
                    modelo.addRow(new Object[]{
                        p.getNombre(),
                        p.getDescripcion(),
                        p.getPrecio() + " Bs", 
                        p.getDia(),
                        p.getHora()
                    });
                }
            }
        } catch (IOException e) {
            System.err.println("Error leyendo el menú: " + e.getMessage());
        }

        return modelo;
    }

}
