package sgcu.modelos;

public class Usuario {
    private String nombre;
    private String correo;
    private String password;
    private String tipo;
    private String cedula;

    // Nuevo constructor que incluye la cédula
    public Usuario(String nombre, String correo, String password, String tipo, String cedula) {
        this.nombre = nombre;
        this.correo = correo;
        this.password = password;
        this.tipo = tipo;
        this.cedula = cedula;
    }

    // Getters necesarios para que el LoginController y Dashboard sepan quién entró    
    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
    public String getPassword() { return password; }
    public String getTipo() { return tipo; }
    public String getCedula() { return cedula; }

    public String toCSV() {
        return nombre + "," + correo + "," + password + "," + tipo + "," + cedula;
    }
}