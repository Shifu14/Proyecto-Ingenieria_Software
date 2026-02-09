package comedor;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class MenuAdminModel {
    // Atributos
    private int id;
    private String nombre;
    private String descripcion;
    private double precio;
    private String dia;
    private String hora;

    // Ruta del archivo (Directo en la carpeta del proyecto para evitar problemas de ruta)
    private static final String RUTA_ARCHIVO = "menu.txt";

    public MenuAdminModel(int id, String nombre, String descripcion, double precio, String dia, String hora) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.dia = dia;
        this.hora = hora;
    }

    // --- PERSISTENCIA: LEER ---
    // Este método es clave: Lee el archivo y devuelve la lista de platos existentes
    public static List<MenuAdminModel> cargarTodos() {
        List<MenuAdminModel> lista = new ArrayList<>();
        Path path = Paths.get(RUTA_ARCHIVO);

        // Si el archivo no existe, lo creamos vacío y retornamos lista vacía
        if (!Files.exists(path)) {
            try { Files.createFile(path); } catch (IOException e) { e.printStackTrace(); }
            return lista;
        }

        // Si existe, leemos línea por línea
        try (BufferedReader br = Files.newBufferedReader(path)) {
            String linea;
            while ((linea = br.readLine()) != null) {
                MenuAdminModel plato = fromCSV(linea);
                if (plato != null) lista.add(plato);
            }
        } catch (IOException e) {
            System.err.println("Error leyendo menu.txt: " + e.getMessage());
        }
        return lista;
    }

    // --- PERSISTENCIA: GUARDAR ---
    // Sobrescribe el archivo con la lista completa actualizada
    public static void guardarTodos(List<MenuAdminModel> lista) {
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(RUTA_ARCHIVO))) {
            for (MenuAdminModel plato : lista) {
                bw.write(plato.toCSV());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error guardando menu.txt: " + e.getMessage());
        }
    }

    // --- UTILIDADES CSV ---
    public String toCSV() {
        return id + ";" + nombre + ";" + descripcion + ";" + precio + ";" + dia + ";" + hora;
    }

    public static MenuAdminModel fromCSV(String linea) {
        try {
            String[] partes = linea.split(";");
            if (partes.length < 6) return null;
            return new MenuAdminModel(
                Integer.parseInt(partes[0]),
                partes[1],
                partes[2],
                Double.parseDouble(partes[3]),
                partes[4],
                partes[5]
            );
        } catch (Exception e) { return null; }
    }

    // --- GETTERS Y SETTERS ---
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    public String getDia() { return dia; }
    public void setDia(String dia) { this.dia = dia; }
    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }
}