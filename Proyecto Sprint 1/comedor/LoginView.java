package comedor;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginView extends JFrame {

    private JTextField nombre;
    private JPasswordField contraseña;
    private JButton btnEntrada;
    private JButton btnRegistro;

    public LoginView() {
        setTitle("INICIO DE SESIÓN - SGCU");
        setSize(800, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("media/icon.png"));
            if (icon != null) setIconImage(icon);
        } catch (Exception e) {}

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(41, 41, 41));

        JPanel bloque = new JPanel(new GridBagLayout());
        bloque.setBackground(new Color(230, 230, 230));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        bloque.setPreferredSize(new Dimension(500, 350));

        JLabel title = new JLabel("BIENVENIDO");
        title.setForeground(new Color(41, 41, 41));
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel camnombre = new JLabel("Usuario");
        camnombre.setFont(new Font("Segoe UI", Font.BOLD, 15));

        nombre = new JTextField(20);
        nombre.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        nombre.setPreferredSize(new Dimension(nombre.getPreferredSize().width, 35));
        
        JLabel camcontraseña = new JLabel("Contraseña");
        camcontraseña.setFont(new Font("Segoe UI", Font.BOLD, 15));

        contraseña = new JPasswordField(20);
        contraseña.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contraseña.setPreferredSize(new Dimension(contraseña.getPreferredSize().width, 35));

        // --- BOTONES CORREGIDOS ---
        btnEntrada = crearBotonEstilizado("INICIAR SESIÓN", new Color(0, 75, 237)); 
        btnRegistro = crearBotonEstilizado("Registrar nueva cuenta", new Color(80, 80, 80));

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        bloque.add(title, gbc);
        bloque.add(Box.createVerticalStrut(20), gbc);

        gbc.gridwidth = 1; gbc.gridy = 1; gbc.gridx = 0; gbc.weightx = 0.0;
        bloque.add(camnombre, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; 
        bloque.add(nombre, gbc);

        gbc.gridy = 2; gbc.gridx = 0; gbc.weightx = 0.0;
        bloque.add(camcontraseña, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        bloque.add(contraseña, gbc);

        gbc.gridy = 3; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 10, 5, 10);
        bloque.add(btnEntrada, gbc);
        
        gbc.gridy = 4;
        gbc.insets = new Insets(10, 10, 10, 10);
        bloque.add(btnRegistro, gbc);

        panel.add(bloque);
        add(panel);
    }

    private JButton crearBotonEstilizado(String texto, Color colorFondo) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) g2.setColor(colorFondo.darker());
                else g2.setColor(colorFondo);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false); // Vital
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(200, 45));
        return btn;
    }

    public String getUsuario() { return nombre.getText(); }
    public String getPassword() { return new String(contraseña.getPassword()); }

    public void setLoginListener(ActionListener listener) { btnEntrada.addActionListener(listener); }
    public void setIrARegistroListener(ActionListener listener) { btnRegistro.addActionListener(listener); }
    public void mostrarMensaje(String msg) { JOptionPane.showMessageDialog(this, msg); }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginController());
    }

}
