package sgcu.modelos;

public class DashboardAdminModel {
    private String nombreUsuario;

    public DashboardAdminModel(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }
}