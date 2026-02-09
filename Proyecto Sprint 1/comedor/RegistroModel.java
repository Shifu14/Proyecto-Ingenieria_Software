package comedor;
import java.io.*;

public class RegistroModel {
    private String nombre;
    private String correo;
    private String password;
    private String tipo;

    private static final String RUTA_ARCHIVO = "usuarios.txt";

    public RegistroModel(String nombre, String correo, String password) {
        this.nombre = nombre;
        this.correo = correo;
        this.password = password;
        this.tipo = "Consumidor"; 
    }

    // --- MÉTODOS DE PERSISTENCIA ---

    public boolean guardarUsuario() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_ARCHIVO, true))) {
            bw.write(this.toCSV());
            bw.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para verificar si el NOMBRE DE USUARIO ya existe
    public static boolean existeUsuario(String nombreBusqueda) {
        File archivo = new File(RUTA_ARCHIVO);
        if (!archivo.exists()) return false;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                // partes[0] es el Nombre
                if (partes.length > 0 && partes[0].equalsIgnoreCase(nombreBusqueda)) {
                    return true; 
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
        return false;
    }

    // [NUEVO] Método para verificar si el CORREO ya existe
    public static boolean existeCorreo(String correoBusqueda) {
        File archivo = new File(RUTA_ARCHIVO);
        if (!archivo.exists()) return false;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                // partes[1] es el Correo (según nuestro formato CSV)
                if (partes.length > 1 && partes[1].equalsIgnoreCase(correoBusqueda)) {
                    return true; 
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
        return false;
    }

    public String toCSV() {
        return nombre + "," + correo + "," + password + "," + tipo;
    }

    // Getters
    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
    public String getPassword() { return password; }
    public String getTipo() { return tipo; }
}