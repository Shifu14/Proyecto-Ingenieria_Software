package comedor;
import javax.swing.JOptionPane;

public class DashboardAdminController {
    
    private DashboardAdminView vista;
    private DashboardAdminModel modelo;

    public DashboardAdminController(String nombreUsuario) {
        this.modelo = new DashboardAdminModel(nombreUsuario);
        this.vista = new DashboardAdminView();

        // Configurar vista inicial
        vista.setNombreUsuario(modelo.getNombreUsuario());

        // --- ASIGNAR EVENTOS DE NAVEGACIÓN ---
        
        // Ir a Gestión de Menú
        vista.addMenuListener(e -> {
            vista.dispose(); // Cerramos el dashboard temporalmente
            new MenuAdminController(); // Abrimos el controlador del menú
        });

        // Ir a Carga de Costos
        vista.addCostosListener(e -> {
            vista.dispose();
            new CargaCostosController(); // Abrimos el controlador de costos
        });

        // Cerrar Sesión
        vista.addLogoutListener(e -> cerrarSesion());

        vista.setVisible(true);
    }

    private void cerrarSesion() {
        int confirm = JOptionPane.showConfirmDialog(vista, "¿Desea cerrar sesión?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            vista.dispose();
            // Aquí llamarías a tu LoginController si lo tienes, por ejemplo:
            // new LoginController(); 
            System.exit(0); // Por ahora cerramos la app
        }
    }

}
