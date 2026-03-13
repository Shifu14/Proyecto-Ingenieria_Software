package sgcu.modelos;

import java.io.*;

public class SaldoPanaModel {

    private static final String RUTA_CONSUMIDORES = "src/sgcu/data/Consumidores.txt";

    // Busca en el archivo Consumidores.txt y retorna el NOMBRE DEL USUARIO si la cédula existe y es de un Estudiante.
    public String obtenerUsuarioPorCedulaEstudiante(String cedulaBuscada) {
        File archivo = new File(RUTA_CONSUMIDORES);
        if (!archivo.exists()) return null;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                
                // Estructura: Nombre(0), Correo(1), Clave(2), Tipo(3), Saldo(4), Foto(5), Cedula(6)
                if (partes.length >= 7) {
                    String nombreUsuario = partes[0];
                    String tipoUsuario = partes[3];
                    String cedulaUsuario = partes[6];

                    if (cedulaUsuario.equalsIgnoreCase(cedulaBuscada) && tipoUsuario.equalsIgnoreCase("Estudiante")) {
                        return nombreUsuario;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}