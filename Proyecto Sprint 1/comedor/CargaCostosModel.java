package comedor;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CargaCostosModel {
    
    private String periodo;
    private double costosFijos;
    private double costosVariables;
    private int numeroBandejas;
    private double porcentajeMerma;
    private double ccB;

    private static final String RUTA_ARCHIVO = "costos.txt";

    public CargaCostosModel(String periodo, double costosFijos, double costosVariables, int numeroBandejas, double porcentajeMerma) {
        this.periodo = periodo;
        this.costosFijos = costosFijos;
        this.costosVariables = costosVariables;
        this.numeroBandejas = numeroBandejas;
        this.porcentajeMerma = porcentajeMerma;
        this.ccB = calcularCCB();
    }

    private double calcularCCB() {
        if (numeroBandejas == 0) return 0.0;
        double costoBase = (costosFijos + costosVariables) / numeroBandejas;
        double calculo = costoBase * (1 + (porcentajeMerma / 100));
        return Math.round(calculo * 100.0) / 100.0;
    }

    public static List<CargaCostosModel> cargarTodos() {
        List<CargaCostosModel> lista = new ArrayList<>();
        Path path = Paths.get(RUTA_ARCHIVO);

        if (!Files.exists(path)) {
            try { Files.createFile(path); } catch (IOException e) { e.printStackTrace(); }
            return lista;
        }

        try (BufferedReader br = Files.newBufferedReader(path)) {
            String linea;
            while ((linea = br.readLine()) != null) {
                CargaCostosModel costo = fromCSV(linea);
                if (costo != null) lista.add(costo);
            }
        } catch (IOException e) {
            System.err.println("Error leyendo costos.txt: " + e.getMessage());
        }
        return lista;
    }

    public static void guardarTodos(List<CargaCostosModel> lista) {
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(RUTA_ARCHIVO))) {
            for (CargaCostosModel c : lista) {
                bw.write(c.toCSV());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error guardando costos.txt: " + e.getMessage());
        }
    }

    public String toCSV() {
        return String.format(Locale.US, "%s;%.2f;%.2f;%d;%.2f;%.2f", 
            periodo, costosFijos, costosVariables, numeroBandejas, porcentajeMerma, ccB);
    }

    public static CargaCostosModel fromCSV(String linea) {
        try {
            String[] partes = linea.split(";");
            if (partes.length < 6) return null;
            return new CargaCostosModel(
                partes[0],
                Double.parseDouble(partes[1]),
                Double.parseDouble(partes[2]),
                Integer.parseInt(partes[3]),
                Double.parseDouble(partes[4])
            );
        } catch (Exception e) { return null; }
    }

    public String getPeriodo() { return periodo; }
    public double getCostosFijos() { return costosFijos; }
    public double getCostosVariables() { return costosVariables; }
    public int getNumeroBandejas() { return numeroBandejas; }
    public double getPorcentajeMerma() { return porcentajeMerma; }
    public double getCcB() { return ccB; }
}