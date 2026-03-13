package sgcu.modelos;

public class DashboardConsumidorModel {
    private String nombreUsuario;

    public DashboardConsumidorModel(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }
}