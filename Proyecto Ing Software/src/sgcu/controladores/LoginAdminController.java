package sgcu.controladores;

import java.awt.event.*;
import sgcu.modelos.LoginAdminModel;
import sgcu.modelos.Usuario;
import sgcu.vistas.LoginAdminView;

public class LoginAdminController implements ActionListener {
    private LoginAdminView vista;
    private LoginAdminModel modelo;

    public LoginAdminController() {
        this.vista = new LoginAdminView();
        this.modelo = new LoginAdminModel();
        
        this.vista.setLoginListener(this);
        this.vista.setIrARegistroListener(e -> abrirRegistro());
        this.vista.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String user = vista.getUsuario().trim();
        String correo = vista.getCorreo().trim();
        String pass = vista.getPassword();

        if (user.isEmpty() || correo.isEmpty() || pass.isEmpty()) {
            vista.mostrarMensaje("Complete todos los campos para acceder.");
            return;
        }

        Usuario adminValidado = modelo.autenticarAdmin(user, correo, pass);

        if (adminValidado != null) {
            vista.dispose();
            new DashboardAdminController(adminValidado.getNombre());
        } else {
            vista.mostrarMensaje("Acceso denegado. Credenciales incorrectas o no autorizadas.");
        }
    }

    private void abrirRegistro() {
        vista.dispose();
        new RegistroAdminController();
    }
}