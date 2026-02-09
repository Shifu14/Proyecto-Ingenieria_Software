package comedor;

public class PanelModel {
    private String tituloApp;
    private String subtitulo;

    public PanelModel() {
        this.tituloApp = "SISTEMA DE GESTIÓN DE COMEDOR";
        this.subtitulo = "Seleccione su tipo de perfil para ingresar";
    }

    public String getTituloApp() { return tituloApp; }
    public String getSubtitulo() { return subtitulo; }
}