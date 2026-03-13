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
        String numCedula = vista.getNumeroCedula().trim();
        String tipoCedula = vista.getTipoCedula();
        String corr = vista.getCorreo().trim();
        String tipoCuenta = vista.getTipoCuenta(); 
        String pass = vista.getContraseña();
        String confirmPass = vista.getConfirmarContraseña();
        File foto = vista.getFotoSeleccionada();

        if (nom.isEmpty() || numCedula.isEmpty() || corr.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {
            vista.mostrarError("Todos los campos (incluyendo la cédula) son obligatorios.");
            return;
        }

        if (!numCedula.matches("\\d+")) {
            vista.mostrarError("La cédula solo debe contener números.");
            return;
        }
        
        String cedulaCompleta = tipoCedula + numCedula;

        if (tipoCuenta == null || tipoCuenta.trim().isEmpty() || tipoCuenta.equals(RegistroView.PLACEHOLDER_TIPO)) {
            vista.mostrarError("Debes escoger uno de los tipos de cuenta (Estudiante, Profesor o Trabajador).");
            return;
        }

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

        if (RegistroModel.existeCorreo(corr)) {
            vista.mostrarError("Este correo electrónico ya se encuentra registrado.");
            return;
        }

        RegistroModel nuevoUsuario = new RegistroModel(nom, cedulaCompleta, corr, pass, tipoCuenta, foto);
        
        if (nuevoUsuario.guardarUsuario()) {
            vista.mostrarMensaje("¡Registro Exitoso!\nSus datos han sido guardados.");
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