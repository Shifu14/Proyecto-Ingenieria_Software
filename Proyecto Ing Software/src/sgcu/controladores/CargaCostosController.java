package sgcu.controladores;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import sgcu.modelos.CargaCostosModel;
import sgcu.vistas.CargaCostosView;

public class CargaCostosController {
    
    private CargaCostosView vista;
    private List<CargaCostosModel> listaCostos;
    private String usuarioLogueado; 

    public CargaCostosController(String nombreUsuario) {
        this.usuarioLogueado = nombreUsuario;
        this.vista = new CargaCostosView();
        
        // Cargamos los datos y los ordenamos inmediatamente
        this.listaCostos = CargaCostosModel.cargarTodos();
        ordenarListaPorFecha(this.listaCostos);
        
        vista.mostrarDatosTabla(listaCostos);

        //Listeners
        vista.addAgregarListener(e -> procesarGuardar());
        vista.addEliminarListener(e -> procesarEliminar());
        vista.addMenuAdminListener(e -> irAMenuAdmin());
        vista.addDashboardListener(e -> irAlDashboard());
        
        // Listener de búsqueda
        vista.addFiltrarListener(e -> filtrarPorPeriodo());

        vista.setVisible(true);
    }

    private void procesarGuardar() {
        try {
            if(vista.getPeriodo().isEmpty()) throw new Exception("Periodo vacío");

            String periodo = vista.getPeriodo().trim();
            double cf = Double.parseDouble(vista.getCostosFijos());
            double cv = Double.parseDouble(vista.getCostosVariables());
            int nb = Integer.parseInt(vista.getNumeroBandejas());
            double merma = Double.parseDouble(vista.getMerma());

            CargaCostosModel nuevo = new CargaCostosModel(periodo, cf, cv, nb, merma);
            
            // Agregamos, ordenamos y guardamos
            listaCostos.add(nuevo);
            ordenarListaPorFecha(listaCostos);
            CargaCostosModel.guardarTodos(listaCostos);
            
            vista.mostrarDatosTabla(listaCostos);
            vista.limpiarCampos();
            vista.mostrarMensaje("Costo calculado, ordenado y guardado.");
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
                // No es necesario ordenar al eliminar, pero sí guardamos los cambios
                CargaCostosModel.guardarTodos(listaCostos);
                vista.mostrarDatosTabla(listaCostos);
                vista.limpiarCampos();
            }
        } else {
            vista.mostrarError("Seleccione una fila.");
        }
    }

    private void filtrarPorPeriodo() {
        String busqueda = vista.getTextoBusqueda().toLowerCase().trim();

        if (busqueda.isEmpty()) {
            vista.mostrarDatosTabla(listaCostos);
            return;
        }

        List<CargaCostosModel> listaFiltrada = new ArrayList<>();
        
        for (CargaCostosModel c : listaCostos) {
            if (c.getPeriodo().toLowerCase().contains(busqueda)) {
                listaFiltrada.add(c);
            }
        }

        if (listaFiltrada.isEmpty()) {
            vista.mostrarError("Error: No existe un registro con el periodo ingresado.");
            vista.mostrarDatosTabla(listaCostos); 
        } else {
            vista.mostrarDatosTabla(listaFiltrada); 
        }
    }

    // --- MÉTODOS DE ORDENAMIENTO CRONOLÓGICO ---

    private void ordenarListaPorFecha(List<CargaCostosModel> lista) {
        // Ordenamos la lista usando un calculador de valor numérico
        lista.sort(Comparator.comparingInt(c -> obtenerValorNumericoFecha(c.getPeriodo())));
    }

    private int obtenerValorNumericoFecha(String periodo) {
        // Separa "Enero 2025" en ["Enero", "2025"]
        String[] partes = periodo.trim().split(" ");
        
        // Si no tiene el formato correcto de 2 palabras, lo manda al final de la lista
        if (partes.length < 2) return 999999; 
        
        String mesString = partes[0].toLowerCase();
        int anio = 0;
        
        try {
            // Intenta leer el año (la segunda palabra)
            anio = Integer.parseInt(partes[1]);
        } catch (NumberFormatException e) {
            return 999999;
        }
        
        int mesNum = 0;
        switch (mesString) {
            case "enero": mesNum = 1; break;
            case "febrero": mesNum = 2; break;
            case "marzo": mesNum = 3; break;
            case "abril": mesNum = 4; break;
            case "mayo": mesNum = 5; break;
            case "junio": mesNum = 6; break;
            case "julio": mesNum = 7; break;
            case "agosto": mesNum = 8; break;
            case "septiembre": mesNum = 9; break;
            case "octubre": mesNum = 10; break;
            case "noviembre": mesNum = 11; break;
            case "diciembre": mesNum = 12; break;
            default: mesNum = 13; // Si escriben mal el mes, va al final del año
        }
        
        // Formula matemática: Año * 100 + Mes (Ej: 2025 * 100 + 1 = 202501)
        return (anio * 100) + mesNum;
    }

    // --- NAVEGACIÓN ---

    private void irAMenuAdmin() {
        new MenuAdminController(this.usuarioLogueado); 
        vista.dispose();
    }

    private void irAlDashboard() {
        new DashboardAdminController(this.usuarioLogueado); 
        vista.dispose();
    }
}