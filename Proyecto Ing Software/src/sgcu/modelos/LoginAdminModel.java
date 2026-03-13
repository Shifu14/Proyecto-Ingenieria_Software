package sgcu.modelos;

import java.io.*;
import java.util.Scanner;

public class LoginAdminModel {
    private static final String NOMBRE_ARCHIVO = "src/sgcu/data/Admins.txt";

    public Usuario autenticarAdmin(String correoIngresado, String passwordIngresada) {
        File archivo = new File(NOMBRE_ARCHIVO);
        if (!archivo.exists()) return null;

        try (Scanner scanner = new Scanner(archivo)) {
            while (scanner.hasNextLine()) {
                String linea = scanner.nextLine();
                String[] partes = linea.split(","); 
                
                // CORRECCIÓN: Cambiamos de '== 4' a '>= 4' para que acepte la nueva estructura con cédula
                if (partes.length >= 4) {
                    String correoTxt = partes[1];
                    String passTxt = partes[2];
                    String tipoTxt = partes[3];
                    
                    if (correoTxt.equalsIgnoreCase(correoIngresado) && passTxt.equals(passwordIngresada)) {
                        if (tipoTxt.equalsIgnoreCase("Administrador")) {
                            
                            // Si tiene la cédula registrada (índice 4), la incluimos en la sesión
                            if (partes.length >= 5) {
                                return new Usuario(partes[0], partes[1], partes[2], partes[3], partes[4]);
                            }
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) { e.printStackTrace(); }
        
        return null;
    }
}