package sgcu.modelos;

import java.io.*;
import java.util.Scanner;

public class LoginModel {
    private static final String NOMBRE_ARCHIVO = "src/sgcu/data/Consumidores.txt";

    public Usuario autenticarUsuario(String correoIngresado, String passwordIngresada) {
        File archivo = new File(NOMBRE_ARCHIVO);
        if (!archivo.exists()) return null;

        try (Scanner scanner = new Scanner(archivo)) {
            while (scanner.hasNextLine()) {
                String linea = scanner.nextLine();
                String[] partes = linea.split(","); 
                
                if (partes.length >= 4) {
                    String correoTxt = partes[1];
                    String passTxt = partes[2];
                    
                    if (correoTxt.equalsIgnoreCase(correoIngresado) && passTxt.equals(passwordIngresada)) {
                        
                        // La estructura actual del TXT es: Nombre, Correo, Clave, Tipo, Saldo, Foto, Cedula
                        // Por lo tanto, la cédula se encuentra en la posición 6.
                        if (partes.length >= 7) {
                            return new Usuario(partes[0], partes[1], partes[2], partes[3], partes[6]);
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) { 
            e.printStackTrace(); 
        }
        
        return null; 
    }
}