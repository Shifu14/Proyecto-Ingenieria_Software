package sgcu.modelos;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class RegistroModel {
    private String nombre;
    private String correo;
    private String password;
    private String tipo;
    private String rutaFotoGuardada; // Nueva variable

    private static final String RUTA_ARCHIVO = "src/sgcu/data/Consumidores.txt";
    private static final String CARPETA_FOTOS = "src/sgcu/data/fotos/"; // Carpeta donde irán las imágenes

    // El constructor ahora pide el archivo de la foto
    public RegistroModel(String nombre, String correo, String password, File archivoFoto) {
        this.nombre = nombre;
        this.correo = correo;
        this.password = password;
        this.tipo = "Consumidor"; 
        this.rutaFotoGuardada = guardarImagenEnProyecto(archivoFoto, nombre);
    }

    // Método para copiar la foto del usuario a la carpeta del proyecto
    private String guardarImagenEnProyecto(File fotoOriginal, String nombreUsuario) {
        if (fotoOriginal == null) return "Sin foto";

        try {
            // Creamos la carpeta "fotos" si no existe
            Path directorio = Paths.get(CARPETA_FOTOS);
            if (!Files.exists(directorio)) {
                Files.createDirectories(directorio);
            }

            // Obtenemos la extensión de la foto original (ej. ".png")
            String nombreArchivo = fotoOriginal.getName();
            String extension = nombreArchivo.substring(nombreArchivo.lastIndexOf("."));

            // Renombramos la foto usando el nombre del usuario para evitar duplicados
            // Ej: "src/sgcu/data/fotos/JuanPerez.png"
            Path rutaDestino = Paths.get(CARPETA_FOTOS + nombreUsuario.replaceAll("\\s+", "_") + extension);

            // Copiamos la imagen
            Files.copy(fotoOriginal.toPath(), rutaDestino, StandardCopyOption.REPLACE_EXISTING);

            return rutaDestino.toString(); // Devolvemos la ruta donde quedó guardada
        } catch (IOException e) {
            e.printStackTrace();
            return "Error al guardar foto";
        }
    }

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

    public static boolean existeUsuario(String nombreBusqueda) {
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
        // Agregamos la ruta de la foto al final del registro CSV
        return nombre + "," + correo + "," + password + "," + tipo + ",0.00," + rutaFotoGuardada;
    }

    // Getters 
    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
    public String getPassword() { return password; }
    public String getTipo() { return tipo; }
    public String getRutaFotoGuardada() { return rutaFotoGuardada; }
}