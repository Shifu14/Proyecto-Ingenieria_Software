package sgcu.controladores;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import sgcu.modelos.MenuAdminModel;
import sgcu.modelos.ReservaModel;
import sgcu.vistas.MenuConsumidorView;

public class MenuConsumidorController {
    private MenuConsumidorView vista;
    private String usuarioLogueado;
    
    // PERSISTENCIA: Este conjunto recuerda los nombres de los platos seleccionados
    // aunque el usuario filtre la tabla o cambie de vista.
    private Set<String> carritoGlobal = new HashSet<>();

    public MenuConsumidorController(String nombreUsuario) {
        this.usuarioLogueado = nombreUsuario;
        this.vista = new MenuConsumidorView();

        // 1. Carga inicial según la hora (Desayuno/Almuerzo)
        actualizarMenuSegunHora();

        // 2. CONFIGURACIÓN DE EVENTOS
        
        // Se ejecuta cada vez que el usuario marca/desmarca un botón en la tabla
        this.vista.setOnCarritoChanged(() -> {
            sincronizarCarritoConVista();
            vista.actualizarTextos(carritoGlobal.size());
        });

        this.vista.addActualizarListener(e -> actualizarMenuSegunHora());
        this.vista.addFiltrarListener(e -> filtrarPorNombre());
        this.vista.addReservarListener(e -> procesarReserva());

        this.vista.addBilleteraListener(e -> {
            vista.dispose();
            new SaldoController(this.usuarioLogueado);
        });

        this.vista.addDashboardListener(e -> {
            vista.dispose();
            new DashboardConsumidorController(this.usuarioLogueado);
        });

        this.vista.setVisible(true);
    }

    /**
     * Revisa el estado actual de la tabla y actualiza el carrito global (Set).
     * Esto permite que el sistema "recuerde" la selección.
     */
    private void sincronizarCarritoConVista() {
        DefaultTableModel modelo = vista.getModeloTabla();
        for (int i = 0; i < modelo.getRowCount(); i++) {
            String nombrePlato = vista.getValorColumna(i, 0);
            boolean seleccionadoEnTabla = (boolean) modelo.getValueAt(i, 5);
            
            if (seleccionadoEnTabla) {
                carritoGlobal.add(nombrePlato);
            } else {
                carritoGlobal.remove(nombrePlato);
            }
        }
    }

    private void actualizarMenuSegunHora() {
        String filtroServicio = obtenerServicioActual();
        List<MenuAdminModel> listaCompleta = MenuAdminModel.cargarTodos();
        
        DefaultTableModel modelo = crearModeloBase();

        for (MenuAdminModel m : listaCompleta) {
            if (m.getHora().equalsIgnoreCase(filtroServicio)) {
                // PERSISTENCIA: Verificamos si este plato ya estaba seleccionado antes
                boolean yaSeleccionado = carritoGlobal.contains(m.getNombre());
                
                modelo.addRow(new Object[]{
                    m.getNombre(), m.getDescripcion(), m.getPrecio(), 
                    m.getHora(), m.getDia(), yaSeleccionado
                });
            }
        }
        vista.setTablaModelo(modelo);
        vista.actualizarTextos(carritoGlobal.size());
    }

    private void filtrarPorNombre() {
        String busqueda = vista.getTextoBusqueda().toLowerCase().trim();
        if (busqueda.isEmpty()) {
            actualizarMenuSegunHora();
            return;
        }

        String servicioActual = obtenerServicioActual();
        List<MenuAdminModel> lista = MenuAdminModel.cargarTodos();
        DefaultTableModel mod = crearModeloBase();

        // Filtrado usando Streams (compatible con Java 8+ usando Collectors)
        List<MenuAdminModel> filtrados = lista.stream()
            .filter(m -> m.getNombre().toLowerCase().contains(busqueda))
            .filter(m -> m.getHora().equalsIgnoreCase(servicioActual))
            .collect(Collectors.toList());

        if (filtrados.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "No se encontraron platos con ese nombre para el horario actual.");
            actualizarMenuSegunHora();
        } else {
            for (MenuAdminModel m : filtrados) {
                boolean yaSeleccionado = carritoGlobal.contains(m.getNombre());
                mod.addRow(new Object[]{
                    m.getNombre(), m.getDescripcion(), m.getPrecio(), 
                    m.getHora(), m.getDia(), yaSeleccionado 
                });
            }
            vista.setTablaModelo(mod);
        }
    }

    private void procesarReserva() {
        if (carritoGlobal.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Seleccione al menos un plato para reservar.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(vista, 
            "¿Confirmar la reserva de " + carritoGlobal.size() + " platos?", 
            "Confirmar Reserva", JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        int exitosas = 0;
        int duplicadas = 0;

        // Iteramos sobre el carrito persistente
        for (String nombrePlato : carritoGlobal) {
            if (ReservaModel.yaExisteReserva(usuarioLogueado, nombrePlato)) {
                duplicadas++;
                continue;
            }
            
            // Buscamos el precio del plato para registrar la reserva
            double precio = buscarPrecioPlato(nombrePlato);
            
            // Usamos el método crearAhora que añadimos a ReservaModel
            ReservaModel nueva = ReservaModel.crearAhora(usuarioLogueado, nombrePlato, precio);
            if (nueva.guardar()) exitosas++;
        }

        String mensaje = "Proceso finalizado.\nExitosas: " + exitosas;
        if (duplicadas > 0) mensaje += "\nOmitidas (Ya reservadas hoy): " + duplicadas;
        
        JOptionPane.showMessageDialog(vista, mensaje);
        
        // Limpiamos el carrito tras la reserva exitosa y refrescamos
        carritoGlobal.clear();
        actualizarMenuSegunHora();
    }

    private double buscarPrecioPlato(String nombre) {
        return MenuAdminModel.cargarTodos().stream()
                .filter(m -> m.getNombre().equalsIgnoreCase(nombre))
                .mapToDouble(MenuAdminModel::getPrecio)
                .findFirst().orElse(0.0);
    }

    private String obtenerServicioActual() {
        int hora = LocalTime.now().getHour();
        if (hora >= 6 && hora < 11) return "Desayuno";
        if (hora >= 11 && hora < 16) return "Almuerzo";
        return "Cerrado";
    }

    private DefaultTableModel crearModeloBase() {
        String[] columnas = {"Plato", "Descripción", "Precio (Bs)", "Servicio", "Día", "Acción"};
        return new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return column == 5; }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 5 ? Boolean.class : String.class;
            }
        };
    }
}