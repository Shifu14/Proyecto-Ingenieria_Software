package comedor;
import javax.swing.JOptionPane;

public class DashboardConsumidorController {
    
    private DashboardConsumidorView vista;
    private DashboardConsumidorModel modelo;
    private SaldoModel saldoModel; // Modelo para leer el dinero real

    public DashboardConsumidorController(String nombreUsuario) {
        this.modelo = new DashboardConsumidorModel(nombreUsuario);
        this.vista = new DashboardConsumidorView();
        this.saldoModel = new SaldoModel(); // Instanciamos el modelo de saldo

        // Configurar nombre
        vista.setNombreUsuario(modelo.getNombreUsuario());
        
        // --- ACTUALIZAR SALDO EN TIEMPO REAL ---
        // Leemos el archivo saldo.txt y actualizamos la tarjeta del dashboard
        double saldoActual = saldoModel.obtenerSaldo();
        vista.actualizarSaldoDisplay(saldoActual);

        // --- EVENTOS ---

        vista.addMenuListener(e -> {
            vista.dispose(); 
            new MenuConsumidorController(); 
        });

        vista.addSaldoListener(e -> {
            vista.dispose();
            new SaldoController();
        });

        vista.addLogoutListener(e -> cerrarSesion());

        vista.setVisible(true);
    }

    private void cerrarSesion() {
        int confirm = JOptionPane.showConfirmDialog(vista, 
            "¿Desea cerrar sesión?", "Confirmar", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            vista.dispose();
            System.exit(0); 
        }
    }
}