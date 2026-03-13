package sgcu.modelos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.util.Arrays;

public class AccesoComedorModel {
    private static final String RUTA_CONSUMIDORES = "src/sgcu/data/Consumidores.txt";
    private static final String RUTA_BENEFICIOS = "src/sgcu/data/Beneficios.txt";

    public String obtenerRutaFotoGuardada(String nombreUsuario) {
        try (BufferedReader br = new BufferedReader(new FileReader(RUTA_CONSUMIDORES))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length >= 7 && partes[0].equalsIgnoreCase(nombreUsuario)) {
                    return partes[5]; 
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public boolean validarIdentidad(File fotoSubida, String rutaGuardada) {
        if (rutaGuardada == null || rutaGuardada.equals("Sin foto")) return false;
        File fotoEnBD = new File(rutaGuardada);
        if (!fotoEnBD.exists() || !fotoSubida.exists()) return false;
        try {
            byte[] bytesSubida = Files.readAllBytes(fotoSubida.toPath());
            byte[] bytesBD = Files.readAllBytes(fotoEnBD.toPath());
            return Arrays.equals(bytesSubida, bytesBD);
        } catch (Exception e) { return false; }
    }

    // Obtiene el Tipo de Usuario (Profesor, Empleado, Estudiante) y su Cédula
    public String[] obtenerDatosUsuario(String nombreUsuario) {
        try (BufferedReader br = new BufferedReader(new FileReader(RUTA_CONSUMIDORES))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length >= 7 && partes[0].equalsIgnoreCase(nombreUsuario)) {
                    return new String[]{partes[3], partes[6]}; 
                }
            }
        } catch (Exception e) {}
        return new String[]{"Desconocido", "N/A"};
    }

    // Busca si la cédula está en Beneficios.txt (Para los Exonerados/Becarios)
    public String[] obtenerBeneficio(String cedula) {
        File archivo = new File(RUTA_BENEFICIOS);
        if (!archivo.exists()) return null;
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length >= 3 && partes[0].equalsIgnoreCase(cedula)) {
                    return new String[]{partes[1], partes[2]}; 
                }
            }
        } catch (Exception e) {}
        return null; 
    }
}