import javax.swing.SwingUtilities;
import comedor.PanelController; // <--- Importamos la clase desde el paquete 'src'

public class Main {
    public static void main(String[] args) {
        // Ejecutamos la aplicación en el hilo de despacho de eventos de Swing
        SwingUtilities.invokeLater(() -> {
            // Iniciamos el Controlador del Panel Principal
            // Este es el punto de entrada que abre la ventana de selección de perfil
            new PanelController(); 
        });
    }
}