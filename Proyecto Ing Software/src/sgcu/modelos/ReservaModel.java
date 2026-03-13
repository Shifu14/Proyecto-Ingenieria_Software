package sgcu.modelos;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ReservaModel {
    private String usuario;
    private String plato;
    private double precio;
    private String fechaHora;

    private static final String RUTA_ARCHIVO = "src/sgcu/data/Reservas.txt";

    public ReservaModel(String usuario, String plato, double precio, String fechaHora) {
        this.usuario = usuario;
        this.plato = plato;
        this.precio = precio;
        this.fechaHora = fechaHora;
    }

    // ─── MÉTODOS DE LECTURA ──────────────────────────────────────────────────

    public static List<ReservaModel> cargarTodos() {
        List<ReservaModel> lista = new ArrayList<>();
        File archivo = new File(RUTA_ARCHIVO);
        if (!archivo.exists()) return lista;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length >= 4) {
                    try {
                        lista.add(new ReservaModel(
                            partes[0].trim(), 
                            partes[1].trim(), 
                            Double.parseDouble(partes[2].trim()), 
                            partes[3].trim()
                        ));
                    } catch (NumberFormatException e) {
                        System.err.println("Error en formato de precio: " + partes[2]);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static List<ReservaModel> cargarPorUsuario(String nombreUsuario) {
        List<ReservaModel> todas = cargarTodos();
        List<ReservaModel> filtradas = new ArrayList<>();
        for (ReservaModel r : todas) {
            if (r.getUsuario().equalsIgnoreCase(nombreUsuario)) {
                filtradas.add(r);
            }
        }
        return filtradas;
    }

    // ─── MÉTODOS DE ESCRITURA Y ACCIÓN ───────────────────────────────────────

    /**
     * ELIMINAR RESERVAS (Método que soluciona el error en AccesoComedorController)
     * @param usuario Nombre del usuario
     * @param platosAEliminar Lista de nombres de platos a borrar
     */
    public static void eliminarReservas(String usuario, List<String> platosAEliminar) {
        List<ReservaModel> todas = cargarTodos();
        File archivo = new File(RUTA_ARCHIVO);

        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
            for (ReservaModel r : todas) {
                // Verificamos si la línea actual coincide con el usuario y alguno de los platos pagados
                boolean esPagada = r.getUsuario().equalsIgnoreCase(usuario) && 
                                  platosAEliminar.contains(r.getPlato());
                
                // Si NO es una de las pagadas, la volvemos a escribir en el archivo
                if (!esPagada) {
                    pw.println(r.getUsuario() + "," + r.getPlato() + "," + 
                               r.getPrecio() + "," + r.getHora());
                }
            }
        } catch (IOException e) {
            System.err.println("Error al actualizar Reservas.txt: " + e.getMessage());
        }
    }

    public boolean guardar() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(RUTA_ARCHIVO, true))) {
            pw.println(usuario + "," + plato + "," + precio + "," + fechaHora);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static ReservaModel crearAhora(String usuario, String plato, double precio) {
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        return new ReservaModel(usuario, plato, precio, fecha);
    }

    public static boolean yaExisteReserva(String usuario, String plato) {
        String hoy = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        List<ReservaModel> actuales = cargarPorUsuario(usuario);
        for (ReservaModel r : actuales) {
            if (r.getPlato().equalsIgnoreCase(plato) && r.getHora().contains(hoy)) {
                return true;
            }
        }
        return false;
    }

    // Getters
    public String getUsuario() { return usuario; }
    public String getPlato() { return plato; }
    public double getPrecio() { return precio; }
    public String getHora() { return fechaHora; }
}