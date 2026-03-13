package sgcu.modelos;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GestionEstudiantilModel {

    private String cedula;
    private String tipo;
    private double descuento; 

    private static final String RUTA_BENEFICIOS   = "src/sgcu/data/Beneficios.txt";
    private static final String RUTA_CONSUMIDORES = "src/sgcu/data/Consumidores.txt";

    
    public static final double MAX_DESCUENTO_BECARIO = 99.0;

    public GestionEstudiantilModel(String cedula, String tipo, double descuento) {
        this.cedula    = cedula;
        this.tipo      = tipo;
        this.descuento = descuento;
    }

    

    
    public double calcularPrecioFinal(double ccb) {
        if (tipo.equalsIgnoreCase("Exonerado")) return 0.0;
        return ccb * (1.0 - descuento / 100.0);
    }

    
    public static boolean esDescuentoBecarioValido(double porcentaje) {
        return porcentaje > 0 && porcentaje < MAX_DESCUENTO_BECARIO;
    }

    

    public static List<GestionEstudiantilModel> cargarTodos() {
        List<GestionEstudiantilModel> lista = new ArrayList<>();
        File archivo = new File(RUTA_BENEFICIOS);
        if (!archivo.exists()) return lista;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;
                String[] partes = linea.split(",");
                if (partes.length == 3) {
                    lista.add(new GestionEstudiantilModel(
                        partes[0].trim(),
                        partes[1].trim(),
                        Double.parseDouble(partes[2].trim())
                    ));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return lista;
    }

    public static void guardarTodos(List<GestionEstudiantilModel> lista) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_BENEFICIOS))) {
            for (GestionEstudiantilModel b : lista) {
                
                bw.write(b.cedula + "," + b.tipo + "," + String.format(java.util.Locale.US, "%.2f", b.descuento));
                bw.newLine();
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    
    public static boolean verificarSiEsEstudianteValido(String cedulaBuscada) {
        File archivo = new File(RUTA_CONSUMIDORES);
        if (!archivo.exists()) return false;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length >= 7) {
                    String tipoUsuario  = partes[3].trim();
                    String cedulaUsuario = partes[6].trim();
                    if (cedulaUsuario.equalsIgnoreCase(cedulaBuscada)) {
                        return tipoUsuario.equalsIgnoreCase("Estudiante");
                    }
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    

    public String getCedula()    { return cedula; }
    public String getTipo()      { return tipo; }
    public double getDescuento() { return descuento; }
}