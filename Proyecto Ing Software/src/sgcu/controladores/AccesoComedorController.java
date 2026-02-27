package sgcu.controladores;

import sgcu.modelos.AccesoComedorModel;
import sgcu.modelos.SaldoModel;
import sgcu.vistas.AccesoComedorView;
import java.io.File;

public class AccesoComedorController {
    private AccesoComedorView vista;
    private AccesoComedorModel modelo;
    private SaldoModel saldoModel;
    private String usuario;

    public AccesoComedorController(String usuario) {
        this.usuario = usuario;
        this.vista = new AccesoComedorView();
        this.modelo = new AccesoComedorModel();
        this.saldoModel = new SaldoModel(usuario);

        this.vista.addVerificarListener(e -> procesarAcceso());
        this.vista.addCancelarListener(e -> volverAlDashboard(false));
        this.vista.setVisible(true);
    }

    private void procesarAcceso() {
        File foto = vista.getFotoSeleccionada();
        if (foto == null) {
            vista.mostrarMensaje("Debe subir una imagen para el reconocimiento.", true);
            return;
        }

        // 1. Verificación Facial
        String rutaBD = modelo.obtenerRutaFotoGuardada(usuario);
        if (!modelo.validarIdentidad(foto, rutaBD)) {
            vista.mostrarMensaje("Identificación inválida. El rostro no coincide con la base de datos.", true);
            return;
        }

        // 2. Verificación de Saldo (Tarifa 500 Bs)
        double saldoActual = saldoModel.obtenerSaldo(usuario);
        if (saldoActual < 500.0) {
            vista.mostrarMensaje("Saldo insuficiente. Necesita al menos 500.00 Bs.\nSu saldo es: " + saldoActual + " Bs.", true);
            return;
        }

        // 3. Cobro y Acceso
        if (saldoModel.actualizarSaldo(usuario, -500.0)) { // Usamos -500 para restar
            vista.mostrarMensaje("¡Identidad verificada!\nSe han cobrado 500.00 Bs de su monedero.\nAcceso concedido a todas las funciones.", false);
            volverAlDashboard(true);
        } else {
            vista.mostrarMensaje("Error al procesar el pago.", true);
        }
    }

    private void volverAlDashboard(boolean concedido) {
        vista.dispose();
        new DashboardConsumidorController(usuario, concedido); // Le pasamos el estado de desbloqueo
    }
}