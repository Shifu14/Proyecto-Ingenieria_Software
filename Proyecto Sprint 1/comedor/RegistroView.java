package comedor;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class RegistroView extends JFrame {

    // Componentes
    private JTextField correo;
    private JTextField nombre;
    private JPasswordField contraseña;
    private JButton btnEntrada;
    private JButton btnVolver;

    public RegistroView() {
        setTitle("REGISTRO EN EL SISTEMA");
        setSize(800, 700); 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("media/icon.png"));
            if (icon != null) setIconImage(icon);
        } catch (Exception e) {}

        // Fondo oscuro general
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(41, 41, 41));

        // Bloque central claro
        JPanel bloque = new JPanel(new GridBagLayout());
        bloque.setBackground(new Color(230, 230, 230));
        bloque.setPreferredSize(new Dimension(500, 420)); // Aumentamos altura
        bloque.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- TÍTULO ---
        JLabel title = new JLabel("REGISTRO");
        title.setForeground(Color.BLACK);
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        // --- CAMPOS ---
        JLabel camcorreo = crearEtiqueta("Correo Electrónico");
        correo = crearCampoTexto();

        JLabel camnombre = crearEtiqueta("Nombre de Usuario");
        nombre = crearCampoTexto();

        JLabel camcontraseña = crearEtiqueta("Contraseña");
        contraseña = new JPasswordField(20);
        contraseña.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contraseña.setPreferredSize(new Dimension(contraseña.getPreferredSize().width, 30));

        // --- BOTONES ---
        btnEntrada = new JButton("REGISTRARSE");
        estilizarBoton(btnEntrada, new Color(0, 75, 237)); // Azul

        btnVolver = new JButton("Volver al Inicio de Sesión");
        estilizarBoton(btnVolver, new Color(100, 100, 100)); // Gris

        // --- ARMADO DEL LAYOUT ---
        
        // Título
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 10, 20, 10);
        bloque.add(title, gbc);

        // Correo
        gbc.gridwidth = 1; gbc.gridy = 1; gbc.gridx = 0; gbc.weightx = 0.0;
        gbc.insets = new Insets(10, 30, 10, 10);
        bloque.add(camcorreo, gbc);

        gbc.gridx = 1; gbc.weightx = 1.0;
        gbc.insets = new Insets(10, 0, 10, 30);
        bloque.add(correo, gbc);

        // Nombre
        gbc.gridy = 2; gbc.gridx = 0; gbc.weightx = 0.0;
        gbc.insets = new Insets(10, 30, 10, 10);
        bloque.add(camnombre, gbc);

        gbc.gridx = 1; gbc.weightx = 1.0;
        gbc.insets = new Insets(10, 0, 10, 30);
        bloque.add(nombre, gbc);

        // Contraseña
        gbc.gridy = 3; gbc.gridx = 0; gbc.weightx = 0.0;
        gbc.insets = new Insets(10, 30, 10, 10);
        bloque.add(camcontraseña, gbc);

        gbc.gridx = 1; gbc.weightx = 1.0;
        gbc.insets = new Insets(10, 0, 10, 30);
        bloque.add(contraseña, gbc);

        // Botón Registrarse
        gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 30, 5, 30);
        bloque.add(btnEntrada, gbc);

        // Botón Volver (Debajo)
        gbc.gridy = 5;
        gbc.insets = new Insets(5, 30, 20, 30);
        bloque.add(btnVolver, gbc);

        panel.add(bloque);
        add(panel);
    }

    // --- MÉTODOS AUXILIARES ---
    private void estilizarBoton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(200, 40));
    }

    private JLabel crearEtiqueta(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lbl.setHorizontalAlignment(SwingConstants.LEFT);
        return lbl;
    }

    private JTextField crearCampoTexto() {
        JTextField txt = new JTextField(20);
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setPreferredSize(new Dimension(txt.getPreferredSize().width, 30));
        return txt;
    }

    // --- GETTERS & LISTENERS ---
    public String getNombre() { return nombre.getText(); }
    public String getCorreo() { return correo.getText(); }
    public String getContraseña() { return new String(contraseña.getPassword()); }

    public void addRegistroListener(ActionListener listener) {
        btnEntrada.addActionListener(listener);
    }
    
    public void addVolverListener(ActionListener listener) { // Listener nuevo
        btnVolver.addActionListener(listener);
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegistroController());
    }

}
