package comedor;
import java.io.*;
import java.util.Scanner;

public class LoginModel {
    
    private static final String NOMBRE_ARCHIVO = "usuarios.txt";

    // Método para validar credenciales
    // Retorna el objeto Usuario si es correcto, o null si falla
    public Usuario autenticarUsuario(String usuarioIngresado, String passwordIngresada) {
        File archivo = new File(NOMBRE_ARCHIVO);
        if (!archivo.exists()) return null;

        try (Scanner scanner = new Scanner(archivo)) {
            while (scanner.hasNextLine()) {
                String linea = scanner.nextLine();
                String[] partes = linea.split(","); 
                
                // Formato CSV: Nombre,Correo,Pass,Tipo
                if (partes.length == 4) {
                    String nombreTxt = partes[0];
                    String passTxt = partes[2];
                    
                    // Verificamos credenciales
                    if (nombreTxt.equals(usuarioIngresado) && passTxt.equals(passwordIngresada)) {
                        // Retornamos el objeto completo para saber su ROL (Estudiante/Admin)
                        return new Usuario(partes[0], partes[1], partes[2], partes[3]);
                    }
                }
            }
        } catch (FileNotFoundException e) { e.printStackTrace(); }
        
        return null; // No encontrado o contraseña incorrecta
    }
}