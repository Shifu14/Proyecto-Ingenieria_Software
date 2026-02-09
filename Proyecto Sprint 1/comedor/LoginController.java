package comedor;
import java.awt.event.*;

public class LoginController implements ActionListener {
    
    private LoginView vista;
    private LoginModel modelo;

    public LoginController() {
        this.vista = new LoginView();
        this.modelo = new LoginModel();
        
        // Asignar eventos
        this.vista.setLoginListener(this);
        this.vista.setIrARegistroListener(e -> abrirRegistro());
        
        this.vista.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String user = vista.getUsuario().trim();
        String pass = vista.getPassword().trim();

        if (user.isEmpty() || pass.isEmpty()) {
            vista.mostrarMensaje("Por favor ingrese usuario y contraseña.");
            return;
        }

        // 1. Validar contra el modelo (usuarios.txt)
        Usuario usuarioValidado = modelo.autenticarUsuario(user, pass);

        if (usuarioValidado != null) {
            vista.dispose(); // Cerrar login
            
            // 2. Redirigir según el tipo de usuario
            if (usuarioValidado.getTipo().equalsIgnoreCase("Administrador")) {
                new DashboardAdminController(usuarioValidado.getNombre());
            } else {
                // Por defecto o si es "Estudiante"/"Consumidor"
                new DashboardConsumidorController(usuarioValidado.getNombre());
            }
            
        } else {
            vista.mostrarMensaje("Usuario o contraseña incorrectos.");
        }
    }

    private void abrirRegistro() {
        vista.dispose(); // Cierra el login
        new RegistroController(); // Abre el controlador de Registro
    }
}