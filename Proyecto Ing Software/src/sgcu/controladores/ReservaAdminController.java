package sgcu.controladores;

import sgcu.modelos.ReservaModel;
import sgcu.vistas.ReservaAdminView;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.stream.Collectors;

public class ReservaAdminController {
    private ReservaAdminView vista;
    private List<ReservaModel> todas;

    public ReservaAdminController(String admin) {
        this.vista = new ReservaAdminView();
        
        cargar();

        // Listeners sincronizados con ReservaAdminView
        vista.addRefrescarListener(e -> cargar());
        vista.addBuscarListener(e -> filtrar());
        vista.addDashboardListener(e -> {
            new DashboardAdminController(admin);
            vista.dispose();
        });

        vista.setVisible(true);
    }

    private void cargar() {
        todas = ReservaModel.cargarTodos();
        mostrar(todas);
    }

    private void filtrar() {
        String b = vista.getTextoBusqueda().toLowerCase().trim();
        List<ReservaModel> f = todas.stream()
            .filter(r -> r.getUsuario().toLowerCase().contains(b))
            .collect(Collectors.toList());
        mostrar(f);
    }

    private void mostrar(List<ReservaModel> lista) {
        DefaultTableModel mod = new DefaultTableModel(new String[]{"Usuario","Plato","Precio","Fecha"}, 0);
        double total = 0;
        for (ReservaModel r : lista) {
            mod.addRow(new Object[]{r.getUsuario(), r.getPlato(), r.getPrecio(), r.getHora()});
            total += r.getPrecio();
        }
        vista.setTablaModelo(mod);
        vista.actualizarTotal(lista.size(), total);
    }
}