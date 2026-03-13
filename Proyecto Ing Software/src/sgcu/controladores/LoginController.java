package sgcu.controladores;

import java.awt.event.*;
import sgcu.modelos.LoginModel;
import sgcu.modelos.Usuario;
import sgcu.vistas.LoginView;

public class LoginController implements ActionListener {
    private LoginView vista;
    private LoginModel modelo;

    public LoginController() {
        this.vista = new LoginView();
        this.modelo = new LoginModel();
        
        this.vista.setLoginListener(this);
        this.vista.setIrARegistroListener(e -> abrirRegistro());
        this.vista.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String correo = vista.getCorreo().trim();
        String pass = vista.getPassword();

        if (correo.isEmpty() || pass.isEmpty()) {
            vista.mostrarMensaje("Por favor, complete todos los campos (Correo y Contraseña).");
            return;
        }

        Usuario usuarioValidado = modelo.autenticarUsuario(correo, pass);

        if (usuarioValidado != null) {
            vista.dispose();
            if (usuarioValidado.getTipo().equalsIgnoreCase("Administrador")) {
                new DashboardAdminController(usuarioValidado.getNombre());
            } else {
                new DashboardConsumidorController(usuarioValidado.getNombre());
            }
        } else {
            vista.mostrarMensaje("Credenciales incorrectas. Verifique sus datos.");
        }
    }

    private void abrirRegistro() {
        vista.dispose();
        new RegistroController();
    }
}