package comedor;

public class SaldoController {

    private SaldoView vista;
    private SaldoModel modelo;

    public SaldoController() {
        this.vista = new SaldoView();
        this.modelo = new SaldoModel();

        actualizarVistaSaldo();

        // Eventos
        this.vista.addSumarListener(e -> procesarRecarga());
        this.vista.addVolverMenuListener(e -> irAlMenu());
        this.vista.addDashboardListener(e -> irAlDashboard()); // Nuevo evento

        this.vista.setVisible(true);
    }

    private void actualizarVistaSaldo() {
        double saldo = modelo.obtenerSaldo();
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

            if (modelo.actualizarSaldo(monto)) {
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
        vista.dispose();
        new MenuConsumidorController(); 
    }
    
    private void irAlDashboard() {
        vista.dispose();
        new DashboardConsumidorController("Estudiante");
    }
}