package sgcu.controladores;

import sgcu.modelos.RegistroAdminModel;
import sgcu.vistas.RegistroAdminView;

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
        String numCedula = vista.getNumeroCedula().trim();
        String tipoCedula = vista.getTipoCedula();
        String corr = vista.getCorreo().trim();
        String pass = vista.getContraseña();
        String confirmPass = vista.getConfirmarContraseña();

        if (nom.isEmpty() || numCedula.isEmpty() || corr.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {
            vista.mostrarError("Debe completar todos los datos administrativos (incluyendo la cédula).");
            return;
        }

        if (!numCedula.matches("\\d+")) {
            vista.mostrarError("La cédula solo debe contener números.");
            return;
        }
        
        String cedulaCompleta = tipoCedula + numCedula;

        if (!nom.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) {
            vista.mostrarError("Nombre de administrador no válido (solo letras).");
            return;
        }

        if (!corr.toLowerCase().matches("^[\\w-\\.]+@ucv\\.ve$")) {
            vista.mostrarError("ACCESO DENEGADO: Los administradores requieren correo @ucv.ve.");
            return;
        }

        if (!pass.equals(confirmPass)) {
            vista.mostrarError("La confirmación de clave no coincide.");
            return;
        }

        if (RegistroAdminModel.existeCorreo(corr)) {
            vista.mostrarError("Este correo electrónico de administrador ya se encuentra registrado.");
            return;
        }

        RegistroAdminModel nuevoAdmin = new RegistroAdminModel(nom, cedulaCompleta, corr, pass);
        if (nuevoAdmin.guardarAdmin()) {
            vista.mostrarMensaje("Administrador registrado correctamente.");
            volverAlLogin();
        } else {
            vista.mostrarError("Error al escribir en el archivo de administradores.");
        }
    }

    private void volverAlLogin() {
        vista.dispose();
        new LoginAdminController();
    }
}