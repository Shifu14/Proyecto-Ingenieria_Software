package comedor;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class RegistroAdminView extends JFrame {

    private JTextField correo;
    private JTextField nombre;
    private JPasswordField contraseña;
    private JButton btnEntrada;
    private JButton btnVolver;

    public RegistroAdminView() {
        setTitle("REGISTRO DE ADMINISTRADORES");
        setSize(800, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("media/icon.png"));
            if (icon != null) setIconImage(icon);
        } catch (Exception e) {}

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(41, 41, 41));

        JPanel bloque = new JPanel(new GridBagLayout());
        bloque.setBackground(new Color(230, 230, 230));
        bloque.setPreferredSize(new Dimension(500, 420));
        bloque.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- TÍTULO DIFERENCIADO ---
        JLabel title = new JLabel("NUEVO ADMIN");
        title.setForeground(new Color(180, 0, 0)); // Color rojo oscuro para diferenciar
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel camcorreo = crearEtiqueta("Correo Institucional");
        correo = crearCampoTexto();

        JLabel camnombre = crearEtiqueta("Usuario Admin");
        nombre = crearCampoTexto();

        JLabel camcontraseña = crearEtiqueta("Contraseña");
        contraseña = new JPasswordField(20);
        contraseña.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contraseña.setPreferredSize(new Dimension(contraseña.getPreferredSize().width, 30));

        btnEntrada = new JButton("REGISTRAR ADMIN");
        estilizarBoton(btnEntrada, new Color(180, 0, 0)); // Botón rojo

        btnVolver = new JButton("Cancelar");
        estilizarBoton(btnVolver, new Color(100, 100, 100));

        // Layout
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 10, 20, 10);
        bloque.add(title, gbc);

        gbc.gridwidth = 1; gbc.gridy = 1; gbc.gridx = 0; gbc.weightx = 0.0;
        gbc.insets = new Insets(10, 30, 10, 10);
        bloque.add(camcorreo, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        gbc.insets = new Insets(10, 0, 10, 30);
        bloque.add(correo, gbc);

        gbc.gridy = 2; gbc.gridx = 0; gbc.weightx = 0.0;
        gbc.insets = new Insets(10, 30, 10, 10);
        bloque.add(camnombre, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        gbc.insets = new Insets(10, 0, 10, 30);
        bloque.add(nombre, gbc);

        gbc.gridy = 3; gbc.gridx = 0; gbc.weightx = 0.0;
        gbc.insets = new Insets(10, 30, 10, 10);
        bloque.add(camcontraseña, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        gbc.insets = new Insets(10, 0, 10, 30);
        bloque.add(contraseña, gbc);

        gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 30, 5, 30);
        bloque.add(btnEntrada, gbc);

        gbc.gridy = 5;
        gbc.insets = new Insets(5, 30, 20, 30);
        bloque.add(btnVolver, gbc);

        panel.add(bloque);
        add(panel);
    }

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

    public String getNombre() { return nombre.getText(); }
    public String getCorreo() { return correo.getText(); }
    public String getContraseña() { return new String(contraseña.getPassword()); }

    public void addRegistroListener(ActionListener l) { btnEntrada.addActionListener(l); }
    public void addVolverListener(ActionListener l) { btnVolver.addActionListener(l); }

    public void mostrarMensaje(String msg) { JOptionPane.showMessageDialog(this, msg, "Info", JOptionPane.INFORMATION_MESSAGE); }
    public void mostrarError(String msg) { JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE); }
}