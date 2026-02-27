package sgcu.modelos;

import java.io.*;
import java.util.Scanner;

public class LoginAdminModel {
    private static final String NOMBRE_ARCHIVO = "src/sgcu/data/Admins.txt";

    public Usuario autenticarAdmin(String usuarioIngresado, String correoIngresado, String passwordIngresada) {
        File archivo = new File(NOMBRE_ARCHIVO);
        if (!archivo.exists()) return null;

        try (Scanner scanner = new Scanner(archivo)) {
            while (scanner.hasNextLine()) {
                String linea = scanner.nextLine();
                String[] partes = linea.split(","); 
                
                if (partes.length == 4) {
                    String nombreTxt = partes[0];
                    String correoTxt = partes[1];
                    String passTxt = partes[2];
                    String tipoTxt = partes[3];
                    
                    
                    if (nombreTxt.equals(usuarioIngresado) && 
                        correoTxt.equalsIgnoreCase(correoIngresado) && 
                        passTxt.equals(passwordIngresada)) {
                        
                        if (tipoTxt.equalsIgnoreCase("Administrador")) {
                            return new Usuario(partes[0], partes[1], partes[2], partes[3]);
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) { e.printStackTrace(); }
        
        return null;
    }
}