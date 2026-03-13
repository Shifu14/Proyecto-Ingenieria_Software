package sgcu.vistas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginView extends JFrame {
    private JTextField correo;
    private JPasswordField contraseña;
    private JCheckBox chkVer;
    private JButton btnEntrada, btnRegistro;

    public LoginView() {
        setTitle("INICIO DE SESIÓN - SGCU");
        setSize(800, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(41, 41, 41));

        JPanel bloque = new JPanel(new GridBagLayout());
        bloque.setBackground(new Color(230, 230, 230));
        bloque.setPreferredSize(new Dimension(500, 450));
        bloque.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("BIENVENIDO");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        bloque.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        // Fila 1: Correo
        gbc.gridy = 1; gbc.gridx = 0;
        JLabel lblCorreo = new JLabel("Correo Electrónico:");
        lblCorreo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        bloque.add(lblCorreo, gbc);

        correo = new JTextField(20);
        correo.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        bloque.add(correo, gbc);

        // Fila 2: Contraseña
        gbc.gridy = 2; gbc.gridx = 0;
        JLabel lblPass = new JLabel("Contraseña:");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 14));
        bloque.add(lblPass, gbc);

        contraseña = new JPasswordField(20);
        contraseña.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        bloque.add(contraseña, gbc);

        // Fila 3: Ver contraseña
        chkVer = new JCheckBox("Ver contraseña");
        chkVer.setBackground(new Color(230, 230, 230));
        gbc.gridy = 3; gbc.gridx = 1;
        bloque.add(chkVer, gbc);

        // Fila 4: Botón Entrar
        btnEntrada = crearBotonEstilizado("INICIAR SESIÓN", new Color(0, 123, 255));
        gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 2;
        bloque.add(btnEntrada, gbc);

        // Fila 5: Botón Registro
        btnRegistro = crearBotonEstilizado("CREAR CUENTA", new Color(40, 167, 69));
        gbc.gridy = 5;
        bloque.add(btnRegistro, gbc);

        panel.add(bloque);
        add(panel);

        chkVer.addActionListener(e -> contraseña.setEchoChar(chkVer.isSelected() ? (char) 0 : '•'));
    }

    private JButton crearBotonEstilizado(String texto, Color bg) {
        JButton btn = new JButton(texto);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setPreferredSize(new Dimension(200, 45));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFocusPainted(false);
        return btn;
    }

    public String getCorreo() { return correo.getText(); }
    public String getPassword() { return new String(contraseña.getPassword()); }

    public void setLoginListener(ActionListener l) { btnEntrada.addActionListener(l); }
    public void setIrARegistroListener(ActionListener l) { btnRegistro.addActionListener(l); }
    public void mostrarMensaje(String msg) { JOptionPane.showMessageDialog(this, msg); }
}