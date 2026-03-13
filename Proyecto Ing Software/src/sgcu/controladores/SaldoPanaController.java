package sgcu.controladores;

import sgcu.modelos.SaldoPanaModel;
import sgcu.modelos.SaldoModel;
import sgcu.vistas.SaldoPanaView;

public class SaldoPanaController {
    
    private SaldoPanaView vista;
    private SaldoPanaModel modelo;
    private SaldoModel saldoModel; 
    
    private String usuarioLogueado;

    // Constructor limpio
    public SaldoPanaController(String usuarioLogueado) {
        this.usuarioLogueado = usuarioLogueado;
        
        this.vista = new SaldoPanaView();
        this.modelo = new SaldoPanaModel();
        this.saldoModel = new SaldoModel(usuarioLogueado);

        this.vista.addTransferirListener(e -> procesarTransferencia());
        this.vista.addVolverListener(e -> {
            vista.dispose();
            new DashboardConsumidorController(this.usuarioLogueado);
        });

        this.vista.setVisible(true);
    }

    private void procesarTransferencia() {
        if (vista.getCedulaNumeros().isEmpty() || vista.getMonto().isEmpty()) {
            vista.mostrarError("Debe ingresar la cédula de su compañero y el monto a enviar.");
            return;
        }

        String cedulaReceptor = vista.getCedulaCompleta();
        double montoATransferir;

        try {
            montoATransferir = Double.parseDouble(vista.getMonto());
            if (montoATransferir <= 0) {
                vista.mostrarError("El monto a transferir debe ser mayor a 0.");
                return;
            }
        } catch (NumberFormatException ex) {
            vista.mostrarError("Ingrese un monto válido.");
            return;
        }

        String nombreReceptor = modelo.obtenerUsuarioPorCedulaEstudiante(cedulaReceptor);

        if (nombreReceptor == null) {
            vista.mostrarError("La cédula ingresada no existe o no pertenece a un estudiante activo.");
            return;
        }

        if (nombreReceptor.equalsIgnoreCase(usuarioLogueado)) {
            vista.mostrarError("No puedes transferirte saldo a ti mismo.");
            return;
        }

        double miSaldo = saldoModel.obtenerSaldo(usuarioLogueado);
        if (miSaldo < montoATransferir) {
            vista.mostrarError("Saldo insuficiente. Tu saldo actual es: " + miSaldo + " Bs.");
            return;
        }

        boolean restaExitosa = saldoModel.actualizarSaldo(usuarioLogueado, -montoATransferir);
        if (restaExitosa) {
            boolean sumaExitosa = saldoModel.actualizarSaldo(nombreReceptor, montoATransferir);
            
            if (sumaExitosa) {
                vista.mostrarMensaje("¡Transferencia exitosa!\nHas enviado " + montoATransferir + " Bs a " + nombreReceptor + ".");
                vista.limpiarCampos();
            } else {
                saldoModel.actualizarSaldo(usuarioLogueado, montoATransferir);
                vista.mostrarError("Error al procesar la recarga del compañero. Se ha devuelto su dinero.");
            }
        } else {
            vista.mostrarError("Error al descontar el saldo de tu cuenta.");
        }
    }
}