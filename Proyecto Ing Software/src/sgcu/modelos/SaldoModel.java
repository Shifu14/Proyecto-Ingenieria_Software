package sgcu.modelos;

import java.io.*;
import java.util.*;

public class SaldoModel {
    private final String RUTA_ARCHIVO = "src/sgcu/data/Consumidores.txt";
    
    // Simulación de bóveda bancaria: guarda las referencias generadas por RecargaController
    private static Map<String, Double> referenciasPendientes = new HashMap<>();

    public SaldoModel() {} 
    public SaldoModel(String nombreUsuario) {} // Mantenido para evitar errores en otros archivos

    public double obtenerSaldo(String nombreUsuario) {
        File archivo = new File(RUTA_ARCHIVO);
        if (!archivo.exists()) return 0.0;
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length >= 5 && partes[0].equalsIgnoreCase(nombreUsuario)) {
                    return Double.parseDouble(partes[4]);
                }
            }
        } catch (Exception e) { return 0.0; }
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
                String[] p = linea.split(",");
                if (p.length >= 5 && p[0].equalsIgnoreCase(nombreUsuario)) {
                    double ns = Double.parseDouble(p[4]) + montoAdicional;
                    linea = p[0] + "," + p[1] + "," + p[2] + "," + p[3] + "," + String.format(Locale.US, "%.2f", ns);
                    if (p.length > 5) for (int i = 5; i < p.length; i++) linea += "," + p[i];
                    actualizado = true;
                }
                lineas.add(linea);
            }
        } catch (Exception e) { return false; }

        if (actualizado) {
            try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
                for (String l : lineas) pw.println(l);
                return true;
            } catch (Exception e) { return false; }
        }
        return false;
    }

    // --- MÉTODOS DE SIMULACIÓN BANCARIA ---
    public static void registrarReferencia(String ref, double monto) {
        referenciasPendientes.put(ref, monto);
    }

    public boolean verificarYAplicarRecarga(String usuario, String ref) {
        if (referenciasPendientes.containsKey(ref)) {
            double monto = referenciasPendientes.get(ref);
            if (actualizarSaldo(usuario, monto)) {
                referenciasPendientes.remove(ref); // Se consume la referencia
                return true;
            }
        }
        return false;
    }
}