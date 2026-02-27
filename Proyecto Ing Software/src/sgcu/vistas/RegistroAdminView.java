package sgcu.vistas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class RegistroAdminView extends JFrame {
    private JTextField nombre, correo;
    private JPasswordField contraseña, confirmarContraseña;
    private JCheckBox chkVer;
    private JButton btnEntrada, btnVolver;

    public RegistroAdminView() {
        setTitle("REGISTRO DE ADMINISTRADORES - SGCU");
        setSize(800, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(41, 41, 41));

        JPanel bloque = new JPanel(new GridBagLayout());
        bloque.setBackground(new Color(230, 230, 230));
        bloque.setPreferredSize(new Dimension(500, 500));
        bloque.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("NUEVO ADMINISTRADOR");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        bloque.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1; gbc.gridx = 0;
        bloque.add(new JLabel("Nombre de usuario:"), gbc);
        nombre = new JTextField(20);
        gbc.gridx = 1; bloque.add(nombre, gbc);

        gbc.gridy = 2; gbc.gridx = 0;
        bloque.add(new JLabel("Correo UCV:"), gbc);
        correo = new JTextField(20);
        gbc.gridx = 1; bloque.add(correo, gbc);

        gbc.gridy = 3; gbc.gridx = 0;
        bloque.add(new JLabel("Contraseña:"), gbc);
        contraseña = new JPasswordField(20);
        gbc.gridx = 1; bloque.add(contraseña, gbc);

        gbc.gridy = 4; gbc.gridx = 0;
        bloque.add(new JLabel("Confirmar contraseña:"), gbc);
        confirmarContraseña = new JPasswordField(20);
        gbc.gridx = 1; bloque.add(confirmarContraseña, gbc);

        chkVer = new JCheckBox("Ver contraseñas");
        chkVer.setBackground(new Color(230, 230, 230));
        gbc.gridy = 5; gbc.gridx = 1;
        bloque.add(chkVer, gbc);

        btnEntrada = new JButton("REGISTRAR");
        btnEntrada.setBackground(new Color(0, 123, 255));
        btnEntrada.setForeground(Color.WHITE);
        btnEntrada.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEntrada.setPreferredSize(new Dimension(200, 45));
        gbc.gridy = 6; gbc.gridx = 0; gbc.gridwidth = 2;
        bloque.add(btnEntrada, gbc);

        btnVolver = new JButton("CANCELAR");
        btnVolver.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridy = 7;
        bloque.add(btnVolver, gbc);

        panel.add(bloque);
        add(panel);

        chkVer.addActionListener(e -> {
            char echo = chkVer.isSelected() ? (char) 0 : '•';
            contraseña.setEchoChar(echo);
            confirmarContraseña.setEchoChar(echo);
        });
    }

    public String getNombre() { return nombre.getText(); }
    public String getCorreo() { return correo.getText(); }
    public String getContraseña() { return new String(contraseña.getPassword()); }
    public String getConfirmarContraseña() { return new String(confirmarContraseña.getPassword()); }
    public void addRegistroListener(ActionListener l) { btnEntrada.addActionListener(l); }
    public void addVolverListener(ActionListener l) { btnVolver.addActionListener(l); }
    public void mostrarError(String m) { JOptionPane.showMessageDialog(this, m, "Error", 0); }
    public void mostrarMensaje(String m) { JOptionPane.showMessageDialog(this, m, "Éxito", 1); }
}