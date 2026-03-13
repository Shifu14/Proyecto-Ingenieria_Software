package sgcu.controladores;

import java.time.LocalTime;
import java.util.Locale;
import sgcu.modelos.MenuAdminModel;
import sgcu.vistas.MenuAdminView;

public class MenuAdminController {
    private MenuAdminView vista;
    private MenuAdminModel modelo;
    private String usuarioLogueado;

    public MenuAdminController(String nombreUsuario) {
        this.usuarioLogueado = nombreUsuario;
        this.vista  = new MenuAdminView();
        this.modelo = new MenuAdminModel();

        actualizarCCBEnVista();
        actualizarTabla();

        this.vista.addAgregarListener(e -> ejecutarAccion("AGREGAR"));
        this.vista.addEditarListener(e -> procesarEdicion());
        this.vista.addEliminarListener(e -> eliminarPlato());
        this.vista.addLimpiarListener(e -> limpiarYResetear());

        this.vista.addDashboardListener(e -> {
            new DashboardAdminController(this.usuarioLogueado);
            vista.dispose();
        });

        this.vista.addCostosListener(e -> {
            new CargaCostosController(this.usuarioLogueado);
            vista.dispose();
        });

        this.vista.setVisible(true);
    }

    private void actualizarCCBEnVista() {
        double ccb = MenuAdminModel.obtenerCCBActual();
        if (ccb > 0.0) {
            vista.setPrecioReadOnly(String.format(Locale.US, "%.2f", ccb));
        } else {
            vista.setPrecioReadOnly("Sin CCB registrado");
        }
    }

    private boolean dentroDeHorario() {
        int hora = LocalTime.now().getHour();
        return hora >= 6 && hora < 18;
    }

    private void mostrarErrorHorario() {
        vista.mostrarError("SISTEMA CERRADO: Las modificaciones al menú solo están\n" +
                           "permitidas de 06:00 AM a 06:00 PM.");
    }

    private void procesarEdicion() {
        if (!dentroDeHorario()) { mostrarErrorHorario(); return; }

        String id = vista.getId();
        if (id == null || id.isEmpty()) {
            vista.mostrarError("Seleccione un plato de la tabla para editarlo.");
            return;
        }

        if (!vista.isModoEdicion()) {
            vista.setModoEdicion(true);
        } else {
            ejecutarAccion("EDITAR");
        }
    }

    private void ejecutarAccion(String operacion) {
        if (!dentroDeHorario()) { mostrarErrorHorario(); return; }

        String nombre = vista.getNombre().trim();
        if (nombre.isEmpty()) {
            vista.mostrarError("El nombre del plato no puede estar vacío.");
            return;
        }
        if (!nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) {
            vista.mostrarError("El nombre solo debe contener letras y espacios.");
            return;
        }

        if (operacion.equals("AGREGAR")) {
            double ccb = MenuAdminModel.obtenerCCBActual();
            if (ccb <= 0.0) {
                vista.mostrarError("No hay ningún CCB registrado en el sistema.\n" +
                                   "Registre los costos del período antes de agregar platos.");
                return;
            }
        }

        boolean resultado = false;

        if (operacion.equals("AGREGAR")) {
            String nuevoId = String.valueOf(System.currentTimeMillis() % 1_000_000);
            resultado = modelo.guardarPlato(
                nuevoId, nombre, vista.getDescripcion().trim(),
                "", vista.getDia(), vista.getTipoServicio()
            );
        } else if (operacion.equals("EDITAR")) {
            resultado = modelo.actualizarPlato(
                vista.getId(), nombre, vista.getDescripcion().trim(),
                "", vista.getDia(), vista.getTipoServicio()
            );
        }

        if (resultado) {
            vista.mostrarMensaje(operacion.equals("AGREGAR")
                ? "¡Plato agregado exitosamente!"
                : "¡Plato actualizado exitosamente!");
            limpiarYResetear();
            actualizarTabla();
        } else {
            vista.mostrarError("Error al guardar los cambios. Verifique que exista un CCB registrado.");
        }
    }

    private void eliminarPlato() {
        if (!dentroDeHorario()) { mostrarErrorHorario(); return; }

        String id = vista.getId();
        if (id == null || id.isEmpty()) {
            vista.mostrarError("Seleccione un plato de la tabla para eliminarlo.");
            return;
        }

        int confirmacion = javax.swing.JOptionPane.showConfirmDialog(
            vista,
            "¿Está seguro que desea eliminar el plato \"" + vista.getNombre() + "\"?",
            "Confirmar eliminación",
            javax.swing.JOptionPane.YES_NO_OPTION
        );

        if (confirmacion == javax.swing.JOptionPane.YES_OPTION) {
            if (modelo.eliminarPlato(id)) {
                vista.mostrarMensaje("Plato eliminado exitosamente.");
                limpiarYResetear();
                actualizarTabla();
            } else {
                vista.mostrarError("Error al eliminar el plato.");
            }
        }
    }

    private void limpiarYResetear() {
        vista.limpiarFormulario();
        vista.setModoEdicion(false);
        actualizarCCBEnVista();
    }

    private void actualizarTabla() {
        vista.limpiarTabla();
        for (MenuAdminModel plato : modelo.obtenerTodos()) {
            vista.agregarFilaTabla(new Object[]{
                plato.getId(),
                plato.getNombre(),
                plato.getDescripcion(),
                String.format(Locale.US, "%.2f", plato.getPrecio()),
                plato.getDia(),
                plato.getHora()
            });
        }
    }
}
