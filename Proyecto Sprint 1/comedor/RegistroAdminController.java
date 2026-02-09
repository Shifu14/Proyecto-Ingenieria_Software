package comedor;

public class RegistroAdminController {
    
    private RegistroAdminView vista;

    public RegistroAdminController() {
        this.vista = new RegistroAdminView();
        
        this.vista.addRegistroListener(e -> procesarRegistro());
        this.vista.addVolverListener(e -> volverAlLogin());
        
        this.vista.setVisible(true);
    }

    private void procesarRegistro() {
        String nom = vista.getNombre().trim();
        String corr = vista.getCorreo().trim();
        String pass = vista.getContraseña();

        if (nom.isEmpty() || corr.isEmpty() || pass.isEmpty()) {
            vista.mostrarError("Todos los campos son obligatorios.");
            return;
        }

        if (!corr.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            vista.mostrarError("Correo inválido.");
            return;
        }

        if (pass.length() < 8 || pass.length() > 16) { 
            vista.mostrarError("Contraseña debe tener entre 8 y 16 caracteres.");
            return;
        }

        if (RegistroAdminModel.existeAdmin(nom)) {
            vista.mostrarError("Este usuario administrador ya existe.");
            return;
        }

        if (RegistroAdminModel.existeCorreo(corr)) {
            vista.mostrarError("Este correo ya está registrado.");
            return;
        }

        RegistroAdminModel nuevoAdmin = new RegistroAdminModel(nom, corr, pass);
        
        if (nuevoAdmin.guardarAdmin()) {
            vista.mostrarMensaje("¡Administrador registrado con éxito!");
            volverAlLogin(); 
        } else {
            vista.mostrarError("Error al guardar en Admin.txt");
        }
    }

    private void volverAlLogin() {
        vista.dispose(); 
        new LoginAdminController(); // Vuelve al login de admins
    }
}