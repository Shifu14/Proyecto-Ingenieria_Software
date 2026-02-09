package comedor;
import java.util.List;

public class CargaCostosController {
    
    private CargaCostosView vista;
    private List<CargaCostosModel> listaCostos;
    
    public CargaCostosController() {
        this.vista = new CargaCostosView();
        this.listaCostos = CargaCostosModel.cargarTodos();
        
        vista.mostrarDatosTabla(listaCostos);

        // Listeners
        vista.addAgregarListener(e -> procesarGuardar());
        vista.addEliminarListener(e -> procesarEliminar());
        vista.addMenuAdminListener(e -> irAMenuAdmin());
        vista.addDashboardListener(e -> irAlDashboard());

        vista.setVisible(true);
    }

    private void procesarGuardar() {
        try {
            if(vista.getPeriodo().isEmpty()) throw new Exception("Periodo vacío");

            String periodo = vista.getPeriodo();
            double cf = Double.parseDouble(vista.getCostosFijos());
            double cv = Double.parseDouble(vista.getCostosVariables());
            int nb = Integer.parseInt(vista.getNumeroBandejas());
            double merma = Double.parseDouble(vista.getMerma());

            CargaCostosModel nuevo = new CargaCostosModel(periodo, cf, cv, nb, merma);
            listaCostos.add(nuevo);
            CargaCostosModel.guardarTodos(listaCostos);
            
            vista.mostrarDatosTabla(listaCostos);
            vista.limpiarCampos();
            vista.mostrarMensaje("Costo calculado y guardado.");
        } catch (NumberFormatException e) {
            vista.mostrarError("Error: Ingrese números válidos.");
        } catch (Exception e) {
            vista.mostrarError("Error: " + e.getMessage());
        }
    }

    private void procesarEliminar() {
        String periodo = vista.getPeriodoSeleccionado();
        if (periodo != null) {
            int confirm = javax.swing.JOptionPane.showConfirmDialog(vista, "¿Eliminar " + periodo + "?", "Confirmar", javax.swing.JOptionPane.YES_NO_OPTION);
            if(confirm == javax.swing.JOptionPane.YES_OPTION) {
                listaCostos.removeIf(c -> c.getPeriodo().equals(periodo));
                CargaCostosModel.guardarTodos(listaCostos);
                vista.mostrarDatosTabla(listaCostos);
                vista.limpiarCampos();
            }
        } else {
            vista.mostrarError("Seleccione una fila.");
        }
    }

    private void irAMenuAdmin() {
        vista.dispose();
        new MenuAdminController(); 
    }

    private void irAlDashboard() {
        vista.dispose();
        new DashboardAdminController("Administrador"); // Volver al inicio
    }
}