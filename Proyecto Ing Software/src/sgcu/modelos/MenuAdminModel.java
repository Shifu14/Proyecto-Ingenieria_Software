package sgcu.modelos;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MenuAdminModel {
    private int id;
    private String nombre;
    private String descripcion;
    private double precio;
    private String dia;
    private String hora;

    private static final String RUTA_ARCHIVO = "src/sgcu/data/Menu.txt";
    private static final String RUTA_COSTOS  = "src/sgcu/data/Costos.txt";

    public MenuAdminModel() {}

    public MenuAdminModel(int id, String nombre, String descripcion, double precio, String dia, String hora) {
        this.id          = id;
        this.nombre      = nombre;
        this.descripcion = descripcion;
        this.precio      = precio;
        this.dia         = dia;
        this.hora        = hora;
    }

    public static double obtenerCCBActual() {
        Path path = Paths.get(RUTA_COSTOS);
        if (!Files.exists(path)) return 0.0;

        double ultimoCCB = 0.0;
        try (BufferedReader br = Files.newBufferedReader(path)) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;
                String[] partes = linea.split(";");
                if (partes.length >= 6) {
                    try {
                        ultimoCCB = Double.parseDouble(partes[5]);
                    } catch (NumberFormatException ignored) {}
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ultimoCCB;
    }


    public List<MenuAdminModel> obtenerTodos() {
        return cargarTodos();
    }

    public boolean guardarPlato(String id, String nombre, String desc,
                                String precio, String dia, String servicio) {
        try {
            double ccb = obtenerCCBActual();
            if (ccb <= 0.0) return false;

            List<MenuAdminModel> lista = cargarTodos();
            lista.add(new MenuAdminModel(
                Integer.parseInt(id),
                nombre,
                desc,
                ccb, 
                dia,
                servicio
            ));
            guardarTodos(lista);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarPlato(String id, String nombre, String desc,
                                   String precio, String dia, String servicio) {
        List<MenuAdminModel> lista = cargarTodos();
        int idInt = Integer.parseInt(id);
        for (MenuAdminModel p : lista) {
            if (p.getId() == idInt) {
                p.setNombre(nombre);
                p.setDescripcion(desc);
                p.setDia(dia);
                p.setHora(servicio);
                guardarTodos(lista);
                return true;
            }
        }
        return false;
    }

    public boolean eliminarPlato(String id) {
        List<MenuAdminModel> lista = cargarTodos();
        int idInt = Integer.parseInt(id);
        boolean removido = lista.removeIf(p -> p.getId() == idInt);
        if (removido) guardarTodos(lista);
        return removido;
    }

    public static List<MenuAdminModel> cargarTodos() {
        List<MenuAdminModel> lista = new ArrayList<>();
        Path path = Paths.get(RUTA_ARCHIVO);

        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
            } catch (IOException e) { e.printStackTrace(); }
            return lista;
        }

        try (BufferedReader br = Files.newBufferedReader(path)) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                MenuAdminModel plato = fromCSV(linea);
                if (plato != null) lista.add(plato);
            }
        } catch (IOException e) { e.printStackTrace(); }
        return lista;
    }

    public static void guardarTodos(List<MenuAdminModel> lista) {
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(RUTA_ARCHIVO))) {
            for (MenuAdminModel plato : lista) {
                bw.write(plato.toCSV());
                bw.newLine();
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    public String toCSV() {
        return id + ";" + nombre + ";" + descripcion + ";"
             + String.format(Locale.US, "%.2f", precio) + ";" + dia + ";" + hora;
    }

    public static MenuAdminModel fromCSV(String linea) {
        try {
            String[] partes = linea.split(";");
            if (partes.length < 6) return null;
            return new MenuAdminModel(
                Integer.parseInt(partes[0]),
                partes[1], partes[2],
                Double.parseDouble(partes[3]),
                partes[4], partes[5]
            );
        } catch (Exception e) { return null; }
    }

    public int    getId()          { return id; }
    public String getNombre()      { return nombre; }
    public void   setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void   setDescripcion(String d) { this.descripcion = d; }
    public double getPrecio()      { return precio; }
    public void   setPrecio(double precio) { this.precio = precio; }
    public String getDia()         { return dia; }
    public void   setDia(String dia) { this.dia = dia; }
    public String getHora()        { return hora; }
    public void   setHora(String hora) { this.hora = hora; }
}
