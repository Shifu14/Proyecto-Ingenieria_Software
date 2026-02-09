package comedor;
import java.util.List;

public class MenuAdminController {
    
    private MenuAdminView vista;
    private List<MenuAdminModel> platos;
    
    public MenuAdminController() {
        this.vista = new MenuAdminView();
        this.platos = MenuAdminModel.cargarTodos();
        
        vista.mostrarDatosTabla(platos);

        // Listeners
        vista.addAgregarListener(e -> procesarAgregar());
        vista.addEditarListener(e -> procesarEditar());
        vista.addEliminarListener(e -> procesarEliminar());
        
        // Navegación
        vista.addCostosListener(e -> irACostos());
        vista.addDashboardListener(e -> irAlDashboard());

        vista.setVisible(true);
    }

    private void procesarAgregar() {
        if (!validarCampos()) return;
        try {
            int nuevoId = platos.isEmpty() ? 1 : platos.stream().mapToInt(MenuAdminModel::getId).max().orElse(0) + 1;
            MenuAdminModel nuevo = new MenuAdminModel(
                nuevoId, vista.getNombre(), vista.getDescripcion(),
                Double.parseDouble(vista.getPrecio()), vista.getDia(), vista.getHora()
            );
            platos.add(nuevo);
            MenuAdminModel.guardarTodos(platos);
            vista.mostrarDatosTabla(platos);
            vista.limpiarFormulario();
            vista.mostrarMensaje("Plato agregado correctamente.");
        } catch (Exception e) { vista.mostrarError("Error al guardar: " + e.getMessage()); }
    }

    private void procesarEditar() {
        if (!validarCampos() || vista.getId().isEmpty()) return;
        try {
            int id = Integer.parseInt(vista.getId());
            for (MenuAdminModel p : platos) {
                if (p.getId() == id) {
                    p.setNombre(vista.getNombre());
                    p.setDescripcion(vista.getDescripcion());
                    p.setPrecio(Double.parseDouble(vista.getPrecio()));
                    p.setDia(vista.getDia());
                    p.setHora(vista.getHora());
                    break;
                }
            }
            MenuAdminModel.guardarTodos(platos);
            vista.mostrarDatosTabla(platos);
            vista.limpiarFormulario();
            vista.mostrarMensaje("Plato actualizado.");
        } catch (Exception e) { vista.mostrarError("Error al editar."); }
    }

    private void procesarEliminar() {
        if (vista.getId().isEmpty()) return;
        int confirm = javax.swing.JOptionPane.showConfirmDialog(vista, "¿Eliminar este plato?", "Confirmar", javax.swing.JOptionPane.YES_NO_OPTION);
        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(vista.getId());
            platos.removeIf(p -> p.getId() == id);
            MenuAdminModel.guardarTodos(platos);
            vista.mostrarDatosTabla(platos);
            vista.limpiarFormulario();
        }
    }

    private void irACostos() {
        vista.dispose();
        new CargaCostosController();
    }

    private void irAlDashboard() {
        vista.dispose();
        new DashboardAdminController("Administrador"); // Volver al inicio
    }

    private boolean validarCampos() {
        if (vista.getNombre().trim().isEmpty() || vista.getPrecio().trim().isEmpty()) {
            vista.mostrarError("Nombre y Precio son obligatorios.");
            return false;
        }
        try {
            if (Double.parseDouble(vista.getPrecio()) <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            vista.mostrarError("El precio debe ser mayor a 0.");
            return false;
        }
        return true;
    }
}