package sgcu.controladores;

import sgcu.modelos.SaldoModel;
import sgcu.vistas.SaldoView;

public class SaldoController {

    private SaldoView vista;
    private SaldoModel modelo;
    private String nombreUsuario; 
    private boolean accesoConcedido; // Guardamos el estado del candado

    // Ahora recibe el estado de acceso
    public SaldoController(String nombreUsuario, boolean accesoConcedido) {
        this.nombreUsuario = nombreUsuario;
        this.accesoConcedido = accesoConcedido;
        this.vista = new SaldoView();
        
        this.modelo = new SaldoModel(nombreUsuario); 

        actualizarVistaSaldo();

        this.vista.addSumarListener(e -> procesarRecarga());
        this.vista.addVolverMenuListener(e -> irAlMenu());
        this.vista.addDashboardListener(e -> irAlDashboard());

        this.vista.setVisible(true);
    }

    private void actualizarVistaSaldo() {
        double saldo = modelo.obtenerSaldo(nombreUsuario);
        vista.actualizarEtiquetaSaldo(saldo);
    }

    private void procesarRecarga() {
        String texto = vista.getMontoIngresado().trim();
        texto = texto.replace(",", "."); 

        if (texto.isEmpty()) return;

        try {
            double monto = Double.parseDouble(texto);
            if (monto <= 0) {
                vista.mostrarError("El monto a ingresar debe ser mayor a 0.");
                return;
            }

            if (modelo.actualizarSaldo(this.nombreUsuario, monto)) {
                actualizarVistaSaldo();
                vista.limpiarCampo();
                vista.mostrarMensaje("¡Recarga exitosa!");
            } else {
                vista.mostrarError("Error al guardar en el archivo.");
            }

        } catch (NumberFormatException e) {
            vista.mostrarError("Por favor ingrese un número válido.");
        }
    }

    private void irAlMenu() {
        // Validación del candado: Si no está verificado, no lo deja pasar
        if (accesoConcedido) {
            vista.dispose();
            new MenuConsumidorController(this.nombreUsuario, this.accesoConcedido); 
        } else {
            vista.mostrarError("Debe volver al panel principal para verificar su identidad\ny desbloquear el acceso completo al comedor.");
        }
    }
    
    private void irAlDashboard() {
        vista.dispose();
        // Le devuelve el estado al Dashboard para que no se bloquee de nuevo
        new DashboardConsumidorController(this.nombreUsuario, this.accesoConcedido);
    }
}