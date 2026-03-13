package sgcu.controladores;

import sgcu.modelos.AccesoComedorModel;
import sgcu.modelos.SaldoModel;
import sgcu.modelos.ReservaModel;
import sgcu.vistas.AccesoComedorView;
import java.io.File;
import java.util.List;
import java.util.Locale;

public class AccesoComedorController {
    private AccesoComedorView vista;
    private AccesoComedorModel modelo;
    private SaldoModel saldoModel;
    
    private String usuario;
    private List<String> platosAPagar;
    
    private double montoTotalCCB; 
    private double tarifaFinalAPagar; 

    public AccesoComedorController(String usuario, List<String> platosAPagar, double montoTotal) {
        this.usuario = usuario;
        this.platosAPagar = platosAPagar;
        this.montoTotalCCB = montoTotal; // La suma de los precios (CCB)
        
        this.vista = new AccesoComedorView();
        this.modelo = new AccesoComedorModel();
        this.saldoModel = new SaldoModel(usuario);

        calcularTarifaSegunRol(); 

        this.vista.addVerificarListener(e -> procesarAcceso());
        this.vista.addCancelarListener(e -> volverAReservas());
        this.vista.setVisible(true);
    }

    private void calcularTarifaSegunRol() {
        String[] datosUsuario = modelo.obtenerDatosUsuario(usuario);
        String tipoUsuario = datosUsuario[0];
        String cedula = datosUsuario[1];

        double porcentajeAplicar = 1.0; 
        String infoBeneficio = "";

        if (tipoUsuario.equalsIgnoreCase("Profesor")) {
            porcentajeAplicar = 0.80; // Paga el 80% del CCB
            infoBeneficio = "Tarifa Profesor (80% del CCB)";
            
        } else if (tipoUsuario.equalsIgnoreCase("Empleado") || tipoUsuario.equalsIgnoreCase("Trabajador")) {
            porcentajeAplicar = 1.00; // Paga el 100% del CCB
            infoBeneficio = "Tarifa Empleado (100% del CCB)";
            
        } else if (tipoUsuario.equalsIgnoreCase("Estudiante")) {
            
            // Por defecto, un estudiante regular paga el 25% del CCB
            porcentajeAplicar = 0.25; 
            infoBeneficio = "Estudiante Regular (Tarifa: 25% del CCB)";

            // Buscamos si tiene algún beneficio asignado
            String[] beneficio = modelo.obtenerBeneficio(cedula);
            if (beneficio != null) {
                if (beneficio[0].equalsIgnoreCase("Exonerado")) {
                    porcentajeAplicar = 0.0; // Paga 0%
                    infoBeneficio = "Exonerado (Tarifa: 0 Bs - Subsidio Total)";
                    
                } else if (beneficio[0].equalsIgnoreCase("Becario")) {
                    // Leemos el porcentaje exacto que le asignó el Administrador (Ej: 5%)
                    double porcAdmin = 5.0; 
                    try { porcAdmin = Double.parseDouble(beneficio[1]); } catch(Exception e){}
                    
                    porcentajeAplicar = porcAdmin / 100.0;
                    infoBeneficio = "Becario (Tarifa Especial: " + porcAdmin + "% del CCB)";
                }
            }
        }

        // CÁLCULO FINAL: CCB Total del Carrito multiplicada por el porcentaje que le toca pagar
        tarifaFinalAPagar = montoTotalCCB * porcentajeAplicar;

        vista.actualizarInfoVista(tipoUsuario, infoBeneficio, platosAPagar.size(), montoTotalCCB, tarifaFinalAPagar);
    }

    private void procesarAcceso() {
        File foto = vista.getFotoSeleccionada();
        if (foto == null) {
            vista.mostrarMensaje("Debe subir una imagen para el reconocimiento facial.", true);
            return;
        }

        // 1. Verificación Facial
        String rutaBD = modelo.obtenerRutaFotoGuardada(usuario);
        if (!modelo.validarIdentidad(foto, rutaBD)) {
            vista.mostrarMensaje("Identificación inválida. El rostro no coincide con la base de datos.", true);
            return;
        }

        // 2. Verificación de Saldo (Sólo si no es exonerado)
        if (tarifaFinalAPagar > 0) {
            double saldoActual = saldoModel.obtenerSaldo(usuario);
            if (saldoActual < tarifaFinalAPagar) {
                vista.mostrarMensaje("Saldo insuficiente en su monedero.\nNecesita: " + String.format(Locale.US, "%.2f", tarifaFinalAPagar) + " Bs.\nSu saldo actual es: " + String.format(Locale.US, "%.2f", saldoActual) + " Bs.", true);
                return;
            }
            
            // 3. Descontar Saldo
            if (!saldoModel.actualizarSaldo(usuario, -tarifaFinalAPagar)) {
                vista.mostrarMensaje("Error al descontar el saldo. Intente nuevamente.", true);
                return;
            }
        }

        // 4. Acceso Exitoso
        ReservaModel.eliminarReservas(usuario, platosAPagar);
        
        if (tarifaFinalAPagar > 0) {
            vista.mostrarMensaje("¡Identidad verificada!\nSe han cobrado " + String.format(Locale.US, "%.2f", tarifaFinalAPagar) + " Bs y se han entregado " + platosAPagar.size() + " plato(s).", false);
        } else {
            vista.mostrarMensaje("¡Identidad verificada!\nSe han entregado " + platosAPagar.size() + " plato(s) de forma gratuita (Beneficio de Exoneración).", false);
        }
        volverAReservas();
    }

    private void volverAReservas() {
        vista.dispose();
        new ReservaConsumidorController(usuario);
    }
}