package sgcu.controladores;

import javax.swing.JOptionPane;
import sgcu.modelos.DashboardConsumidorModel;
import sgcu.modelos.SaldoModel;
import sgcu.vistas.DashboardConsumidorView;

public class DashboardConsumidorController {
    
    private DashboardConsumidorView vista;
    private DashboardConsumidorModel modelo;
    private SaldoModel saldoModel; 
    private boolean accesoConcedido; 

    public DashboardConsumidorController(String nombreUsuario) {
        this(nombreUsuario, false); 
    }

    public DashboardConsumidorController(String nombreUsuario, boolean verificado) {
        this.accesoConcedido = verificado;
        this.modelo = new DashboardConsumidorModel(nombreUsuario);
        this.vista = new DashboardConsumidorView();
        this.saldoModel = new SaldoModel(nombreUsuario); 

        vista.setNombreUsuario(modelo.getNombreUsuario());
        vista.actualizarSaldoDisplay(saldoModel.obtenerSaldo(nombreUsuario));
        vista.aplicarBloqueo(accesoConcedido);

        vista.addVerificarAccesoListener(e -> {
            vista.dispose();
            new AccesoComedorController(nombreUsuario); 
        });

        vista.addSaldoListener(e -> { 
            vista.dispose();
            // Le pasamos el estado actual al Monedero
            new SaldoController(nombreUsuario, accesoConcedido);
        });

        vista.addMenuListener(e -> {
            if (accesoConcedido) {
                vista.dispose(); 
                // Le pasamos el estado actual al Menú
                new MenuConsumidorController(nombreUsuario, accesoConcedido); 
            } else { vista.mostrarErrorBloqueo(); }
        });

        vista.addReservasListener(e -> {
            if (!accesoConcedido) vista.mostrarErrorBloqueo();
        });

        vista.addHistorialListener(e -> {
            if (!accesoConcedido) vista.mostrarErrorBloqueo();
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