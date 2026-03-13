package sgcu.controladores;

import sgcu.modelos.RecargaModel;
import sgcu.modelos.SaldoModel;
import sgcu.vistas.RecargaView;
import javax.swing.JOptionPane;

public class RecargaController {
    private RecargaView vista;
    private RecargaModel modelo;
    private SaldoModel saldoModel;
    private String usuario;

    public RecargaController(String usuario) {
        this.usuario = usuario;
        this.modelo = new RecargaModel();
        this.saldoModel = new SaldoModel();
        this.vista = new RecargaView();

        this.vista.addGenerarListener(e -> generarPago());
        this.vista.addVerificarListener(e -> verificarPago());
        
        this.vista.addVolverListener(e -> {
            vista.dispose();
            new SaldoController(usuario); // Regresa al monedero actualizado
        });

        this.vista.setVisible(true);
    }

    private void generarPago() {
        String tel = vista.getTelefono();
        String ci = vista.getCedula();
        String montoTxt = vista.getMonto();

        if (modelo.validarCampos(tel, ci, montoTxt)) {
            double monto = Double.parseDouble(montoTxt);
            String referencia = modelo.generarReferenciaUnica();
            
            // Guardamos temporalmente en la bóveda de SaldoModel
            SaldoModel.registrarReferencia(referencia, monto);

            // Mostramos la referencia en el campo de texto para que sea copiable
            vista.setRefGenerada(referencia);
            
            JOptionPane.showMessageDialog(vista, "Pago Emitido. Copie la Referencia Generada y péguela abajo para validar.");
        } else {
            JOptionPane.showMessageDialog(vista, "Datos inválidos. El teléfono debe tener 7 dígitos y el monto debe ser mayor a 0.");
        }
    }

    private void verificarPago() {
        String refInput = vista.getRefIngresada();
        
        if (refInput.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Pegue la referencia en el campo de texto.");
            return;
        }

        // Llamamos a SaldoModel para que verifique y aplique el dinero al archivo .txt
        if (saldoModel.verificarYAplicarRecarga(usuario, refInput)) {
            JOptionPane.showMessageDialog(vista, "¡Recarga Verificada! El saldo se ha sumado a su monedero.");
            vista.dispose();
            new SaldoController(usuario); // Redirige automáticamente al monedero para ver el saldo nuevo
        } else {
            JOptionPane.showMessageDialog(vista, "Referencia inválida o ya utilizada.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}