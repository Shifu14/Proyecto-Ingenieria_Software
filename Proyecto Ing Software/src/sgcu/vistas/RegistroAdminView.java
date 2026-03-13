package sgcu.vistas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class RegistroAdminView extends JFrame {
    private JTextField nombre, correo;
    private JComboBox<String> comboCedula;
    private JTextField txtCedula;
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
        bloque.setPreferredSize(new Dimension(500, 540)); // Ampliado para la cédula
        bloque.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("NUEVO ADMINISTRADOR");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        bloque.add(title, gbc);

        gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.WEST;

        // Nombre
        gbc.gridy = 1; gbc.gridx = 0;
        bloque.add(new JLabel("Nombre de usuario:"), gbc);
        nombre = new JTextField(20);
        gbc.gridx = 1; bloque.add(nombre, gbc);

        // Cédula (NUEVO)
        gbc.gridy = 2; gbc.gridx = 0;
        bloque.add(new JLabel("Cédula de Identidad:"), gbc);
        
        JPanel panelCedula = new JPanel(new BorderLayout(5, 0));
        panelCedula.setOpaque(false);
        comboCedula = new JComboBox<>(new String[]{"V-", "E-"});
        txtCedula = new JTextField();
        
        txtCedula.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) || txtCedula.getText().length() >= 9) {
                    e.consume(); 
                }
            }
        });

        panelCedula.add(comboCedula, BorderLayout.WEST);
        panelCedula.add(txtCedula, BorderLayout.CENTER);
        gbc.gridx = 1; bloque.add(panelCedula, gbc);

        // Correo
        gbc.gridy = 3; gbc.gridx = 0;
        bloque.add(new JLabel("Correo UCV:"), gbc);
        correo = new JTextField(20);
        gbc.gridx = 1; bloque.add(correo, gbc);

        // Contraseña
        gbc.gridy = 4; gbc.gridx = 0;
        bloque.add(new JLabel("Contraseña:"), gbc);
        contraseña = new JPasswordField(20);
        gbc.gridx = 1; bloque.add(contraseña, gbc);

        // Confirmar
        gbc.gridy = 5; gbc.gridx = 0;
        bloque.add(new JLabel("Confirmar Clave:"), gbc);
        confirmarContraseña = new JPasswordField(20);
        gbc.gridx = 1; bloque.add(confirmarContraseña, gbc);

        chkVer = new JCheckBox("Ver contraseña");
        chkVer.setBackground(new Color(230, 230, 230));
        gbc.gridy = 6; gbc.gridx = 1;
        bloque.add(chkVer, gbc);

        btnEntrada = new JButton("REGISTRAR");
        btnEntrada.setBackground(new Color(0, 80, 180));
        btnEntrada.setForeground(Color.WHITE);
        btnEntrada.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEntrada.setPreferredSize(new Dimension(200, 45));
        gbc.gridy = 7; gbc.gridx = 0; gbc.gridwidth = 2;
        bloque.add(btnEntrada, gbc);

        btnVolver = new JButton("CANCELAR");
        btnVolver.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridy = 8;
        bloque.add(btnVolver, gbc);

        panel.add(bloque); add(panel);

        chkVer.addActionListener(e -> {
            char echo = chkVer.isSelected() ? (char) 0 : '•';
            contraseña.setEchoChar(echo);
            confirmarContraseña.setEchoChar(echo);
        });
    }

    public String getNombre() { return nombre.getText(); }
    public String getTipoCedula() { return comboCedula.getSelectedItem().toString(); }
    public String getNumeroCedula() { return txtCedula.getText(); }
    public String getCorreo() { return correo.getText(); }
    public String getContraseña() { return new String(contraseña.getPassword()); }
    public String getConfirmarContraseña() { return new String(confirmarContraseña.getPassword()); }
    
    public void addRegistroListener(ActionListener l) { btnEntrada.addActionListener(l); }
    public void addVolverListener(ActionListener l) { btnVolver.addActionListener(l); }
    public void mostrarMensaje(String msg) { JOptionPane.showMessageDialog(this, msg); }
    public void mostrarError(String msg) { JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE); }
}