package comedor;
import java.awt.event.*;

public class LoginAdminController implements ActionListener {
    
    private LoginAdminView vista;
    private LoginAdminModel modelo;

    public LoginAdminController() {
        this.vista = new LoginAdminView();
        this.modelo = new LoginAdminModel();
        
        this.vista.setLoginListener(this);
        this.vista.setIrARegistroListener(e -> abrirRegistroAdmin());
        
        this.vista.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String user = vista.getUsuario().trim();
        String pass = vista.getPassword().trim();

        if (user.isEmpty() || pass.isEmpty()) {
            vista.mostrarMensaje("Ingrese sus credenciales.");
            return;
        }

        Usuario adminValidado = modelo.autenticarAdmin(user, pass);

        if (adminValidado != null) {
            vista.dispose();
            // Abre directamente el Dashboard de Administrador
            new DashboardAdminController(adminValidado.getNombre());
        } else {
            vista.mostrarMensaje("Acceso denegado. Verifique usuario o contraseña.");
        }
    }

    private void abrirRegistroAdmin() {
        vista.dispose();
        new RegistroAdminController();
    }
}