package sgcu.controladores;

import java.time.LocalTime;
import sgcu.modelos.MenuAdminModel;
import sgcu.vistas.MenuAdminView;

public class MenuAdminController {
    private MenuAdminView vista;
    private MenuAdminModel modelo;
    private String usuarioLogueado; 

    public MenuAdminController(String nombreUsuario) {
        this.usuarioLogueado = nombreUsuario;
        this.vista = new MenuAdminView();
        this.modelo = new MenuAdminModel();
        
        actualizarTabla();

        //Listeners 
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

    private void procesarEdicion() {
        LocalTime ahora = LocalTime.now();
        if (ahora.getHour() < 6 || ahora.getHour() >= 18) {
            vista.mostrarError("SISTEMA CERRADO: Solo de 06:00 AM a 06:00 PM.");
            return; // Detiene la ejecución y no deja que el botón se ponga verde
        }
        // -----------------------------------------------------

        String id = vista.getId();
        if (id == null || id.isEmpty()) {
            vista.mostrarError("Seleccione un plato de la tabla primero para editarlo.");
            return;
        }

        if (!vista.isModoEdicion()) {
            vista.setModoEdicion(true); // Se vuelve verde
        } else {
            ejecutarAccion("EDITAR"); // Ejecuta el guardado
        }
    }

    private void ejecutarAccion(String operacion) {
        // Mantenemos la validación aquí también por seguridad (por si cambia la hora mientras edita)
        LocalTime ahora = LocalTime.now();
        if (ahora.getHour() < 6 || ahora.getHour() >= 18) {
            vista.mostrarError("SISTEMA CERRADO: Solo de 06:00 AM a 06:00 PM.");
            return;
        }

        String nombre = vista.getNombre().trim();
        if (nombre.isEmpty() || !nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) {
            vista.mostrarError("Error: El nombre solo debe contener letras.");
            return;
        }

        boolean resultado = false;
        if (operacion.equals("AGREGAR")) {
            String nuevoId = String.valueOf(System.currentTimeMillis()).substring(7);
            resultado = modelo.guardarPlato(nuevoId, nombre, vista.getDescripcion(), vista.getPrecio(), vista.getDia(), vista.getTipoServicio());
        } else if (operacion.equals("EDITAR")) {
            resultado = modelo.actualizarPlato(vista.getId(), nombre, vista.getDescripcion(), vista.getPrecio(), vista.getDia(), vista.getTipoServicio());
        }

        if (resultado) {
            vista.mostrarMensaje("¡Éxito!");
            limpiarYResetear(); 
            actualizarTabla();
        }
    }

    private void eliminarPlato() {
        //  Validación del horario (Bloqueo si está cerrado)
        LocalTime ahora = LocalTime.now();
        if (ahora.getHour() < 6 || ahora.getHour() >= 18) {
            vista.mostrarError("SISTEMA CERRADO: Solo de 06:00 AM a 06:00 PM.");
            return; // Detiene el código aquí, no elimina nada
        }

        // Validación de selección
        String id = vista.getId();
        if (id == null || id.isEmpty()) {
            vista.mostrarError("Seleccione un plato de la tabla primero para eliminarlo.");
            return;
        }

        // Proceso de eliminación
        int confirmacion = javax.swing.JOptionPane.showConfirmDialog(vista, 
            "¿Está seguro que desea eliminar este plato?", "Confirmar Eliminación", 
            javax.swing.JOptionPane.YES_NO_OPTION);
            
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
    }

    private void actualizarTabla() {
        vista.limpiarTabla();
        for (MenuAdminModel plato : modelo.obtenerTodos()) {
            vista.agregarFilaTabla(new Object[]{
                plato.getId(), plato.getNombre(), plato.getDescripcion(), 
                plato.getPrecio(), plato.getDia(), plato.getHora()
            });
        }
    }
}