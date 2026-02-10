package comedor;
import javax.swing.table.DefaultTableModel;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MenuConsumidorModel {

    // Apunta al mismo archivo que genera el Administrador
    private static final String RUTA_ARCHIVO = "menu.txt";

    public DefaultTableModel obtenerDatosMenu() {
        // Definimos las columnas que verá el consumidor
        String[] columnas = {"Nombre", "Descripción", "Precio", "Día", "Hora"};
        
        // Creamos el modelo de tabla no editable
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Verificamos si existe el archivo
        File archivo = new File(RUTA_ARCHIVO);
        if (!archivo.exists()) {
            return modelo; // Retorna tabla vacía si no hay menú creado aún
        }

        // Leemos el archivo menu.txt
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                MenuAdminModel p = MenuAdminModel.fromCSV(linea);
                
                if (p != null) {
                    // Agregamos a la tabla solo la información relevante para el consumidor
                    modelo.addRow(new Object[]{
                        p.getNombre(),
                        p.getDescripcion(),
                        p.getPrecio() + " Bs", // Agregamos el símbolo de moneda para visualización
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
