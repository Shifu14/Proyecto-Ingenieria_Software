package sgcu.controladores;

import java.time.LocalTime;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import sgcu.modelos.MenuAdminModel;
import sgcu.vistas.MenuConsumidorView;

public class MenuConsumidorController {
    private MenuConsumidorView vista;
    private String usuarioLogueado; 
    private boolean accesoConcedido; // Variable de estado

    // Recibe el estado de acceso
    public MenuConsumidorController(String nombreUsuario, boolean accesoConcedido) {
        this.usuarioLogueado = nombreUsuario; 
        this.accesoConcedido = accesoConcedido;
        this.vista = new MenuConsumidorView();

        actualizarMenuSegunHora();

        this.vista.addActualizarListener(e -> actualizarMenuSegunHora());
        this.vista.addFiltrarListener(e -> filtrarPorNombre());
        
        this.vista.addBilleteraListener(e -> {
            vista.dispose(); 
            // Pasa el estado al SaldoController
            new SaldoController(this.usuarioLogueado, this.accesoConcedido); 
        });
        
        this.vista.addDashboardListener(e -> {
            vista.dispose(); 
            // Devuelve el estado al Dashboard
            new DashboardConsumidorController(this.usuarioLogueado, this.accesoConcedido); 
        });

        this.vista.setVisible(true);
    }

    private void actualizarMenuSegunHora() {
        LocalTime ahora = LocalTime.now();
        int tiempoActual = ahora.getHour() * 60 + ahora.getMinute();

        String filtroServicio = "";
        if (tiempoActual >= 360 && tiempoActual <= 630) {
            filtroServicio = "Desayuno";
        } else if (tiempoActual >= 690 && tiempoActual <= 990) {
            filtroServicio = "Almuerzo";
        }

        List<MenuAdminModel> listaCompleta = MenuAdminModel.cargarTodos();
        String[] columnas = {"Plato", "Descripción", "Precio (Bs)", "Servicio", "Día"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        for (MenuAdminModel m : listaCompleta) {
            if (m.getHora().equalsIgnoreCase(filtroServicio)) {
                modeloTabla.addRow(new Object[]{
                    m.getNombre(), m.getDescripcion(), m.getPrecio(), m.getHora(), m.getDia()
                });
            }
        }
        vista.setTablaModelo(modeloTabla);
    }

    private void filtrarPorNombre() {
        String busqueda = vista.getTextoBusqueda().toLowerCase().trim();
        if (busqueda.isEmpty()) {
            actualizarMenuSegunHora();
            return;
        }

        List<MenuAdminModel> lista = MenuAdminModel.cargarTodos();
        String[] columnas = {"Plato", "Descripción", "Precio (Bs)", "Servicio", "Día"};
        DefaultTableModel mod = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        for (MenuAdminModel m : lista) {
            if (m.getNombre().toLowerCase().contains(busqueda)) {
                mod.addRow(new Object[]{ 
                    m.getNombre(), m.getDescripcion(), m.getPrecio(), m.getHora(), m.getDia() 
                });
            }
        }

        if (mod.getRowCount() == 0) {
            JOptionPane.showMessageDialog(vista, "No existe un plato con el nombre ingresado", "Búsqueda sin resultados", JOptionPane.ERROR_MESSAGE);
            actualizarMenuSegunHora();
        } else {
            vista.setTablaModelo(mod);
        }
    }
}