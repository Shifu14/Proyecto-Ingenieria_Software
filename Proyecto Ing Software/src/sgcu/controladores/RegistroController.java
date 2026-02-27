package sgcu.controladores;

import sgcu.modelos.RegistroModel;
import sgcu.vistas.RegistroView;
import java.io.File;

public class RegistroController {
    private RegistroView vista;

    public RegistroController() {
        this.vista = new RegistroView();
        this.vista.addRegistroListener(e -> procesarRegistro());
        this.vista.addVolverListener(e -> volverAlLogin());
        this.vista.setVisible(true);
    }

    private void procesarRegistro() {
        String nom = vista.getNombre().trim();
        String corr = vista.getCorreo().trim();
        String pass = vista.getContraseña();
        String confirmPass = vista.getConfirmarContraseña();
        File foto = vista.getFotoSeleccionada();

        if (nom.isEmpty() || corr.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {
            vista.mostrarError("Todos los campos son obligatorios.");
            return;
        }

        // Validacion: Comprobar que haya subido una foto
        if (foto == null) {
            vista.mostrarError("Debe subir una foto de perfil (JPG o PNG).");
            return;
        }

        if (!nom.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) {
            vista.mostrarError("El nombre no puede contener números ni caracteres especiales.");
            return;
        }

        if (!corr.toLowerCase().matches("^[\\w-\\.]+@(ucv)\\.(ve)$")) {
            vista.mostrarError("Correo inválido. Use dominios reales, solo se permite @ucv.ve ");
            return;
        }

        if (!pass.equals(confirmPass)) {
            vista.mostrarError("Las contraseñas no coinciden.");
            return;
        }

        if (pass.length() < 8) {
            vista.mostrarError("La contraseña debe tener al menos 8 caracteres.");
            return;
        }

        if (RegistroModel.existeUsuario(nom)) {
            vista.mostrarError("El nombre de usuario ya existe.");
            return;
        }

        // Le pasamos el archivo de la foto al Modelo
        RegistroModel nuevoUsuario = new RegistroModel(nom, corr, pass, foto);
        
        if (nuevoUsuario.guardarUsuario()) {
            vista.mostrarMensaje("¡Registro Exitoso!\nSu foto de perfil ha sido guardada.");
            volverAlLogin();
        } else {
            vista.mostrarError("Error crítico al guardar el usuario.");
        }
    }

    private void volverAlLogin() {
        vista.dispose();
        new LoginController();
    }
}