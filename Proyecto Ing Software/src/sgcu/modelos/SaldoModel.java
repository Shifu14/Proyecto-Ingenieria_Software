package sgcu.modelos;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SaldoModel {

    private final String RUTA_ARCHIVO = "src/sgcu/data/Consumidores.txt";

    public SaldoModel(String nombreUsuario) {
    }

    public double obtenerSaldo(String nombreUsuario) {
        File archivo = new File(RUTA_ARCHIVO);
        if (!archivo.exists()) return 0.0;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                // El saldo está en la columna 5 (índice 4)
                if (partes.length >= 5 && partes[0].equalsIgnoreCase(nombreUsuario)) {
                    return Double.parseDouble(partes[4]);
                }
            }
        } catch (IOException | NumberFormatException e) {
            return 0.0;
        }
        return 0.0;
    }

    public boolean actualizarSaldo(String nombreUsuario, double montoAdicional) {
        File archivo = new File(RUTA_ARCHIVO);
        if (!archivo.exists()) return false;

        List<String> lineas = new ArrayList<>();
        boolean actualizado = false;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                
                if (partes.length >= 5 && partes[0].equalsIgnoreCase(nombreUsuario)) {
                    double saldoActual = Double.parseDouble(partes[4]);
                    double nuevoSaldo = saldoActual + montoAdicional;
                    
                    String saldoFormateado = String.format(Locale.US, "%.2f", nuevoSaldo);
                    
                    // Armamos la línea base (hasta el saldo)
                    linea = partes[0] + "," + partes[1] + "," + partes[2] + "," + partes[3] + "," + saldoFormateado;
                    
                    // Si el usuario tiene una foto guardada u otros datos, los volvemos a pegar
                    if (partes.length > 5) {
                        for (int i = 5; i < partes.length; i++) {
                            linea += "," + partes[i];
                        }
                    }
                    actualizado = true;
                }
                lineas.add(linea);
            }
        } catch (IOException | NumberFormatException e) {
            return false;
        }

        if (actualizado) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
                for (String l : lineas) {
                    bw.write(l);
                    bw.newLine();
                }
                return true;
            } catch (IOException e) {
                return false;
            }
        }
        return false;
    }
}