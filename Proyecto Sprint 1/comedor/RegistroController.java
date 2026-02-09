package comedor;

public class RegistroController {
    
    private RegistroView vista;

    public RegistroController() {
        this.vista = new RegistroView();
        
        // Evento: Registrarse
        this.vista.addRegistroListener(e -> procesarRegistro());
        
        // Evento: Volver al Login
        this.vista.addVolverListener(e -> volverAlLogin());
        
        this.vista.setVisible(true);
    }

    private void procesarRegistro() {
        String nom = vista.getNombre().trim();
        String corr = vista.getCorreo().trim();
        String pass = vista.getContraseña();

        // Validaciones
        if (nom.isEmpty() || corr.isEmpty() || pass.isEmpty()) {
            vista.mostrarError("Por favor, complete todos los campos.");
            return;
        }

        if (!corr.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            vista.mostrarError("El formato del correo electrónico no es válido.");
            return;
        }

        if (pass.length() < 8 || pass.length() > 16) { 
            vista.mostrarError("La contraseña debe tener entre 8 y 16 caracteres.");
            return;
        }

        if (RegistroModel.existeUsuario(nom)) {
            vista.mostrarError("El usuario '" + nom + "' ya está registrado.");
            return;
        }

        if (RegistroModel.existeCorreo(corr)) {
            vista.mostrarError("El correo ya está asociado a otra cuenta.");
            return;
        }

        // Guardar
        RegistroModel nuevoUsuario = new RegistroModel(nom, corr, pass);
        
        if (nuevoUsuario.guardarUsuario()) {
            vista.mostrarMensaje("¡Registro Exitoso!\nAhora puede iniciar sesión.");
            volverAlLogin(); // Reutilizamos el método para ir al login
        } else {
            vista.mostrarError("Error crítico al guardar en la base de datos.");
        }
    }

    private void volverAlLogin() {
        vista.dispose(); // Cerrar registro
        new LoginController(); // Abrir Login
    }
}