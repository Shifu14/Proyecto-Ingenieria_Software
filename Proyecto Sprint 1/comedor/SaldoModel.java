package comedor;
import java.io.IOException;
import java.nio.file.*;
import java.util.Locale;

public class SaldoModel {

private final String RUTA_ARCHIVO = "saldo.txt";

    // Obtener el saldo actual del archivo
    public double obtenerSaldo() {
        Path p = Paths.get(RUTA_ARCHIVO);
        if (!Files.exists(p)) {
            try {
                Files.write(p, "0.00".getBytes());
            } catch (IOException e) { return 0.0; }
        }

        try {
            byte[] bytes = Files.readAllBytes(p);
            String contenido = new String(bytes).trim();
            return Double.parseDouble(contenido);
        } catch (IOException | NumberFormatException e) {
            return 0.0;
        }
    }

    // Guardar el nuevo saldo sumado
    public boolean actualizarSaldo(double montoAdicional) {
        double saldoActual = obtenerSaldo();
        double nuevoSaldo = saldoActual + montoAdicional;

        try {
            // Usamos Locale.US para asegurar que se guarde con punto (10.50)
            String saldoFormateado = String.format(Locale.US, "%.2f", nuevoSaldo);
            Files.write(Paths.get(RUTA_ARCHIVO), saldoFormateado.getBytes());
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}