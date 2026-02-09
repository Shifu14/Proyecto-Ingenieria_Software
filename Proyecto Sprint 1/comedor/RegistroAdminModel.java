package comedor;
import java.io.*;

public class RegistroAdminModel {
    private String nombre;
    private String correo;
    private String password;
    private String tipo;

    // Archivo exclusivo para admins
    private static final String RUTA_ARCHIVO = "Admin.txt";

    public RegistroAdminModel(String nombre, String correo, String password) {
        this.nombre = nombre;
        this.correo = correo;
        this.password = password;
        this.tipo = "Administrador"; // Tipo forzado
    }

    public boolean guardarAdmin() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_ARCHIVO, true))) {
            bw.write(this.toCSV());
            bw.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean existeAdmin(String nombreBusqueda) {
        File archivo = new File(RUTA_ARCHIVO);
        if (!archivo.exists()) return false;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length > 0 && partes[0].equalsIgnoreCase(nombreBusqueda)) {
                    return true; 
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
        return false;
    }

    public static boolean existeCorreo(String correoBusqueda) {
        File archivo = new File(RUTA_ARCHIVO);
        if (!archivo.exists()) return false;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
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
}