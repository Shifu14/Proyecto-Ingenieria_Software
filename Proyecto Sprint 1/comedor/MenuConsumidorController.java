package comedor;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class MenuConsumidorController {
    
    private MenuConsumidorView vista;
    private MenuConsumidorModel modelo;

    public MenuConsumidorController() {
        this.vista = new MenuConsumidorView();
        this.modelo = new MenuConsumidorModel();

        // 1. Cargar datos del archivo menu.txt
        actualizarDatos();

        // 2. Eventos
        this.vista.addMonederoListener(e -> abrirMonedero());
        this.vista.addFiltrarListener(e -> filtrarTabla());
        this.vista.addActualizarListener(e -> actualizarDatos());
        this.vista.addDashboardListener(e -> volverAlDashboard()); // Nuevo evento

        // 3. Mostrar
        this.vista.setVisible(true);
    }

    private void actualizarDatos() {
        DefaultTableModel datos = modelo.obtenerDatosMenu();
        vista.setTablaModelo(datos);
    }

    private void abrirMonedero() {
        vista.dispose(); 
        new SaldoController(); 
    }
    
    private void volverAlDashboard() {
        vista.dispose();
        // Regresa al Dashboard (Asumiendo usuario "Estudiante" por defecto)
        new DashboardConsumidorController("Consumidor"); 
    }

    private void filtrarTabla() {
        String texto = vista.getTextoBusqueda().toLowerCase();
        if (texto.isEmpty()) {
            actualizarDatos();
        } else {
            JOptionPane.showMessageDialog(vista, "Filtrando por: " + texto);
        }
    }

}
