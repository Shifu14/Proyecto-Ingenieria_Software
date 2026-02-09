package comedor;

public class PanelController {

    private PanelView vista;
    private PanelModel modelo;

    public PanelController() {
        this.vista = new PanelView();
        this.modelo = new PanelModel();

        // Configurar textos desde el modelo
        vista.setTextos(modelo.getTituloApp(), modelo.getSubtitulo());

        // Eventos
        vista.addConsumidorListener(e -> irALoginConsumidor());
        vista.addAdminListener(e -> irALoginAdmin());

        vista.setVisible(true);
    }

    private void irALoginConsumidor() {
        vista.dispose(); 
        new LoginController(); 
    }

    private void irALoginAdmin() {
        vista.dispose(); 
        new LoginAdminController();
    }
}