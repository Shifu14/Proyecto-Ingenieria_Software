package sgcu.modelos;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class RegistroModel {
    private String nombre;
    private String cedula;
    private String correo;
    private String password;
    private String tipo;
    private String rutaFotoGuardada; 

    private static final String RUTA_ARCHIVO = "src/sgcu/data/Consumidores.txt";
    private static final String CARPETA_FOTOS = "src/sgcu/data/fotos/"; 

    public RegistroModel(String nombre, String cedula, String correo, String password, String tipo, File archivoFoto) {
        this.nombre = nombre;
        this.cedula = cedula;
        this.correo = correo;
        this.password = password;
        this.tipo = tipo; 
        this.rutaFotoGuardada = guardarImagenEnProyecto(archivoFoto, nombre);
    }

    private String guardarImagenEnProyecto(File fotoOriginal, String nombreUsuario) {
        if (fotoOriginal == null) return "Sin foto";
        try {
            Path directorio = Paths.get(CARPETA_FOTOS);
            if (!Files.exists(directorio)) {
                Files.createDirectories(directorio);
            }
            String nombreArchivo = fotoOriginal.getName();
            String extension = nombreArchivo.substring(nombreArchivo.lastIndexOf("."));
            Path rutaDestino = Paths.get(CARPETA_FOTOS + nombreUsuario.replaceAll("\\s+", "_") + extension);
            Files.copy(fotoOriginal.toPath(), rutaDestino, StandardCopyOption.REPLACE_EXISTING);
            return rutaDestino.toString(); 
        } catch (IOException e) {
            return "Error al guardar foto";
        }
    }

    public boolean guardarUsuario() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_ARCHIVO, true))) {
            bw.write(this.toCSV());
            bw.newLine();
            return true;
        } catch (IOException e) { return false; }
    }

    public static boolean existeUsuario(String nombreBusqueda) {
        File archivo = new File(RUTA_ARCHIVO);
        if (!archivo.exists()) return false;
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length > 0 && partes[0].equalsIgnoreCase(nombreBusqueda)) return true; 
            }
        } catch (IOException e) {}
        return false;
    }

    public static boolean existeCorreo(String correoBusqueda) {
        File archivo = new File(RUTA_ARCHIVO);
        if (!archivo.exists()) return false;
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length > 1 && partes[1].equalsIgnoreCase(correoBusqueda)) return true; 
            }
        } catch (IOException e) {}
        return false;
    }

    public String toCSV() {
        return nombre + "," + correo + "," + password + "," + tipo + ",0.00," + rutaFotoGuardada + "," + cedula;
    }

    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
    public String getPassword() { return password; }
    public String getTipo() { return tipo; }
    public String getCedula() { return cedula; }
}