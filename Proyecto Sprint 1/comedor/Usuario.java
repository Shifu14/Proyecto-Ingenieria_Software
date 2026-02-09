package comedor;

public class Usuario {
    private String nombre;
    private String correo;
    private String password;
    private String tipo;

    public Usuario(String nombre, String correo, String password, String tipo) {
        this.nombre = nombre;
        this.correo = correo;
        this.password = password;
        this.tipo = tipo;
    }

    // Getters necesarios para que el LoginController y Dashboard sepan quién entró
    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
    public String getPassword() { return password; }
    public String getTipo() { return tipo; }

    // Método útil para guardar en formato texto (CSV)
    // Ejemplo: "Juan,juan@email.com,1234,Consumidor"
    public String toCSV() {
        return nombre + "," + correo + "," + password + "," + tipo;
    }
}