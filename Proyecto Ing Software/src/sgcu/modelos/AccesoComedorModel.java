package sgcu.modelos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.util.Arrays;

public class AccesoComedorModel {
    private static final String RUTA_ARCHIVO = "src/sgcu/data/Consumidores.txt";

    // 1. Obtiene la ruta de la foto guardada en el txt
    public String obtenerRutaFotoGuardada(String nombreUsuario) {
        try (BufferedReader br = new BufferedReader(new FileReader(RUTA_ARCHIVO))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length >= 6 && partes[0].equalsIgnoreCase(nombreUsuario)) {
                    return partes[5]; // La ruta está en la columna 6
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    // 2. Compara los archivos convirtiéndolos a bytes
    public boolean validarIdentidad(File fotoSubida, String rutaGuardada) {
        if (rutaGuardada == null || rutaGuardada.equals("Sin foto")) return false;
        
        File fotoEnBD = new File(rutaGuardada);
        if (!fotoEnBD.exists() || !fotoSubida.exists()) return false;

        try {
            byte[] bytesSubida = Files.readAllBytes(fotoSubida.toPath());
            byte[] bytesBD = Files.readAllBytes(fotoEnBD.toPath());
            
            return Arrays.equals(bytesSubida, bytesBD); // Si son el mismo archivo, coinciden
        } catch (Exception e) {
            return false;
        }
    }
}