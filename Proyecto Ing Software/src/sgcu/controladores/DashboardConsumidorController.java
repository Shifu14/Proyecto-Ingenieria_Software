package sgcu.controladores;

import javax.swing.JOptionPane;
import sgcu.modelos.DashboardConsumidorModel;
import sgcu.modelos.SaldoModel;
import sgcu.vistas.DashboardConsumidorView;

public class DashboardConsumidorController {
    
    private DashboardConsumidorView vista;
    private DashboardConsumidorModel modelo;
    private SaldoModel saldoModel; 

    // Constructor único y limpio
    public DashboardConsumidorController(String nombreUsuario) {
        this.modelo = new DashboardConsumidorModel(nombreUsuario);
        this.vista = new DashboardConsumidorView();
        this.saldoModel = new SaldoModel(nombreUsuario); 

        vista.setNombreUsuario(modelo.getNombreUsuario());
        vista.actualizarSaldoDisplay(saldoModel.obtenerSaldo(nombreUsuario));

        // Todos los botones abren sus ventanas directamente sin restricciones
        vista.addSaldoListener(e -> { 
            vista.dispose();
            new SaldoController(nombreUsuario);
        });

        vista.addSaldoPanaListener(e -> {
            vista.dispose();
            new SaldoPanaController(nombreUsuario);
        });

        vista.addMenuListener(e -> {
            vista.dispose(); 
            new MenuConsumidorController(nombreUsuario); 
        });

        vista.addReservasListener(e -> {
            vista.dispose();
            new ReservaConsumidorController(nombreUsuario);
        });

        vista.addHistorialListener(e -> { //recordar quitar esta funcion
            JOptionPane.showMessageDialog(null, "Módulo de historial en construcción");
        });

        vista.addLogoutListener(e -> cerrarSesion());
        this.vista.setVisible(true);
    }

    private void cerrarSesion() {
        if (JOptionPane.showConfirmDialog(vista, "¿Desea cerrar sesión?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            vista.dispose();
            new LoginController(); 
        }
    }
}