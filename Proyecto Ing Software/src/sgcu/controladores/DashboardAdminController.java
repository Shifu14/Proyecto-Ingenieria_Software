package sgcu.controladores;

import javax.swing.JOptionPane;
import sgcu.modelos.DashboardAdminModel;
import sgcu.vistas.DashboardAdminView;

public class DashboardAdminController {
    
    private DashboardAdminView vista;
    private DashboardAdminModel modelo;

    public DashboardAdminController(String nombreUsuario) {
        this.modelo = new DashboardAdminModel(nombreUsuario);
        this.vista = new DashboardAdminView();

        //Configurar vista inicial
        vista.setNombreUsuario(modelo.getNombreUsuario());
        
        vista.addMenuListener(e -> {
            
            new MenuAdminController(modelo.getNombreUsuario()); 
            vista.dispose(); 
        });

        vista.addCostosListener(e -> {
            
            new CargaCostosController(nombreUsuario); 
            vista.dispose();
        });

        vista.addLogoutListener(e -> cerrarSesion());

        vista.setVisible(true);
    }

    private void cerrarSesion() {
        int confirm = JOptionPane.showConfirmDialog(vista, "¿Desea cerrar sesión?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            vista.dispose();
            
            System.exit(0); 
        }
    }
}