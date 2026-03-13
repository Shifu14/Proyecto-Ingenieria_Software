package sgcu.controladores;

import sgcu.modelos.ReservaModel;
import sgcu.vistas.ReservaConsumidorView;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public class ReservaConsumidorController {

    private ReservaConsumidorView vista;
    private String usuarioLogueado;
    private List<ReservaModel> todasMisReservas;
    
    // PERSISTENCIA: Este conjunto recuerda qué platos seleccionaste para pagar
    // aunque filtres la tabla con el buscador o des a "Ver Todo".
    private Set<String> carritoPago = new HashSet<>();

    public ReservaConsumidorController(String usuarioLogueado) {
        this.usuarioLogueado = usuarioLogueado;
        this.vista = new ReservaConsumidorView();

        // 1. Cargar las reservas desde el archivo a través del modelo
        this.todasMisReservas = ReservaModel.cargarPorUsuario(usuarioLogueado);
        
        // 2. Mostrar la tabla inicial
        refrescarTabla(todasMisReservas);

        // 3. CONFIGURACIÓN DE EVENTOS (Listeners)

        // Se activa cuando haces clic en los botones "Seleccionar/Quitar" de la tabla
        this.vista.setOnSeleccionChanged(() -> {
            sincronizarSeleccion();
        });

        this.vista.addFiltrarListener(e -> filtrarReservas());

        this.vista.addVerTodoListener(e -> {
            vista.actualizarConteo(todasMisReservas.size(), carritoPago.size());
            refrescarTabla(todasMisReservas);
        });

        this.vista.addPagarListener(e -> iniciarProcesoPago());

        this.vista.addDashboardListener(e -> {
            vista.dispose();
            new DashboardConsumidorController(usuarioLogueado);
        });

        this.vista.setVisible(true);
    }

    /**
     * Sincroniza el estado visual de la tabla con el Set de persistencia.
     */
    private void sincronizarSeleccion() {
        DefaultTableModel modelo = vista.getModeloTabla();
        for (int i = 0; i < modelo.getRowCount(); i++) {
            String nombrePlato = modelo.getValueAt(i, 0).toString();
            boolean seleccionadoEnTabla = (boolean) modelo.getValueAt(i, 3);

            if (seleccionadoEnTabla) {
                carritoPago.add(nombrePlato);
            } else {
                carritoPago.remove(nombrePlato);
            }
        }
        vista.actualizarConteo(todasMisReservas.size(), carritoPago.size());
    }

    private void filtrarReservas() {
        String busqueda = vista.getTextoBusqueda().toLowerCase().trim();
        
        // Filtramos la lista cargada en memoria para mayor velocidad
        List<ReservaModel> filtradas = todasMisReservas.stream()
            .filter(r -> r.getPlato().toLowerCase().contains(busqueda))
            .collect(Collectors.toList());

        refrescarTabla(filtradas);
    }

    /**
     * Reconstruye el modelo de la tabla manteniendo el estado de los botones
     * según el Set de persistencia.
     */
    private void refrescarTabla(List<ReservaModel> listaAMostrar) {
        String[] columnas = {"Plato", "Precio (Bs)", "Fecha de Reserva", "Acción"};
        
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public Class<?> getColumnClass(int c) {
                // La columna 3 debe ser Boolean para que la Vista active el ButtonEditor
                return c == 3 ? Boolean.class : String.class;
            }
            @Override
            public boolean isCellEditable(int r, int c) {
                return c == 3; // Solo la columna de botones es interactuable
            }
        };

        for (ReservaModel r : listaAMostrar) {
            // PERSISTENCIA: Si el plato ya estaba en el Set, el botón saldrá en ROJO (true)
            boolean estadoBoton = carritoPago.contains(r.getPlato());
            
            modelo.addRow(new Object[]{
                r.getPlato(),
                String.format(Locale.US, "%.2f", r.getPrecio()),
                r.getHora(),
                estadoBoton
            });
        }

        vista.setTablaModelo(modelo);
        vista.actualizarConteo(todasMisReservas.size(), carritoPago.size());
    }

    private void iniciarProcesoPago() {
        if (carritoPago.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(vista, "Seleccione al menos una reserva para pagar.");
            return;
        }

        // Calculamos el monto total sumando solo lo seleccionado en el Set
        double montoTotal = 0;
        List<String> platosSeleccionados = new ArrayList<>(carritoPago);

        for (String nombre : platosSeleccionados) {
            for (ReservaModel r : todasMisReservas) {
                if (r.getPlato().equalsIgnoreCase(nombre)) {
                    montoTotal += r.getPrecio();
                    break;
                }
            }
        }

        // Cerramos y pasamos al controlador de Cobro Facial (Acceso)
        vista.dispose();
        new AccesoComedorController(usuarioLogueado, platosSeleccionados, montoTotal);
    }
}