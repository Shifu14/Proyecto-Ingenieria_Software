package sgcu.vistas;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

public class RegistroView extends JFrame {

    private JTextField nombre, correo;
    private JComboBox<String> comboTipo, comboCedula; 
    private JTextField txtCedula;
    private JPasswordField contraseña, confirmarContraseña;
    private JCheckBox chkVer;
    private JButton btnSubirFoto, btnEntrada, btnVolver;
    private JLabel lblNombreFoto;
    private File fotoSeleccionada; 
    
    public static final String PLACEHOLDER_TIPO = "--Escoger un tipo de cuenta--";
    private static final Color GRIS_CLARO_COLOR = new Color(170, 170, 170);

    public RegistroView() {
        setTitle("REGISTRO DE USUARIO - SGCU");
        setSize(800, 800); 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(41, 41, 41));

        JPanel bloque = new JPanel(new GridBagLayout());
        bloque.setBackground(new Color(230, 230, 230));
        bloque.setPreferredSize(new Dimension(500, 660)); // Ampliado para la cédula
        bloque.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("CREAR CUENTA");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        bloque.add(title, gbc);

        gbc.gridwidth = 1; 
        gbc.anchor = GridBagConstraints.WEST;

        // 1. Nombre
        gbc.gridy = 1; gbc.gridx = 0;
        bloque.add(crearEtiqueta("Nombre de Usuario:"), gbc);
        nombre = crearCampoTexto();
        gbc.gridx = 1; bloque.add(nombre, gbc);

        // 2. Cédula (NUEVO)
        gbc.gridy = 2; gbc.gridx = 0;
        bloque.add(crearEtiqueta("Cédula de Identidad:"), gbc);
        
        JPanel panelCedula = new JPanel(new BorderLayout(5, 0));
        panelCedula.setOpaque(false);
        comboCedula = new JComboBox<>(new String[]{"V-", "E-"});
        txtCedula = new JTextField();
        
        // Evento para bloquear letras y límite de 9 dígitos
        txtCedula.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) || txtCedula.getText().length() >= 9) {
                    e.consume(); // Cancela la pulsación si no es número o se pasa de 9
                }
            }
        });

        panelCedula.add(comboCedula, BorderLayout.WEST);
        panelCedula.add(txtCedula, BorderLayout.CENTER);
        gbc.gridx = 1; bloque.add(panelCedula, gbc);

        // 3. Correo
        gbc.gridy = 3; gbc.gridx = 0;
        bloque.add(crearEtiqueta("Correo Electrónico:"), gbc);
        correo = crearCampoTexto();
        gbc.gridx = 1; bloque.add(correo, gbc);

        // 4. Tipo de Cuenta 
        gbc.gridy = 4; gbc.gridx = 0;
        bloque.add(crearEtiqueta("Tipo de Cuenta:"), gbc);
        comboTipo = new JComboBox<>(new String[]{PLACEHOLDER_TIPO, "Estudiante", "Profesor", "Trabajador"});
        comboTipo.setPreferredSize(new Dimension(200, 30));
        comboTipo.setBackground(Color.WHITE);
        comboTipo.setRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                Font fb = list != null ? list.getFont() : c.getFont();
                if (value != null && value.toString().equals(PLACEHOLDER_TIPO)) {
                    c.setForeground(GRIS_CLARO_COLOR); c.setFont(fb.deriveFont(Font.ITALIC));
                } else {
                    c.setForeground(Color.BLACK); c.setFont(fb.deriveFont(Font.PLAIN));
                }
                c.setBackground(isSelected && index >= 0 ? list.getSelectionBackground() : list.getBackground());
                return c;
            }
        });
        gbc.gridx = 1; bloque.add(comboTipo, gbc);

        // 5. Contraseña
        gbc.gridy = 5; gbc.gridx = 0;
        bloque.add(crearEtiqueta("Contraseña:"), gbc);
        contraseña = new JPasswordField(20);
        contraseña.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1; bloque.add(contraseña, gbc);

        // 6. Confirmar
        gbc.gridy = 6; gbc.gridx = 0;
        bloque.add(crearEtiqueta("Confirmar Contraseña:"), gbc);
        confirmarContraseña = new JPasswordField(20);
        confirmarContraseña.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1; bloque.add(confirmarContraseña, gbc);

        // 7. Ver Contraseña
        chkVer = new JCheckBox("Ver contraseñas");
        chkVer.setBackground(new Color(230, 230, 230));
        gbc.gridy = 7; gbc.gridx = 1; bloque.add(chkVer, gbc);

        // 8. Foto
        btnSubirFoto = crearBotonEstilizado("Subir Foto", new Color(40, 167, 69));
        btnSubirFoto.setPreferredSize(new Dimension(200, 35));
        lblNombreFoto = new JLabel("Ninguna foto seleccionada");
        lblNombreFoto.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        gbc.gridy = 8; gbc.gridx = 0; bloque.add(btnSubirFoto, gbc);
        gbc.gridx = 1; bloque.add(lblNombreFoto, gbc);

        // 9. Botones
        btnEntrada = crearBotonEstilizado("REGISTRARSE", new Color(0, 123, 255));
        gbc.gridy = 9; gbc.gridx = 0; gbc.gridwidth = 2; bloque.add(btnEntrada, gbc);

        btnVolver = crearBotonEstilizado("VOLVER", new Color(108, 117, 125));
        gbc.gridy = 10; bloque.add(btnVolver, gbc);

        panel.add(bloque); add(panel);

        chkVer.addActionListener(e -> {
            char echo = chkVer.isSelected() ? (char) 0 : '•';
            contraseña.setEchoChar(echo);
            confirmarContraseña.setEchoChar(echo);
        });
        btnSubirFoto.addActionListener(e -> abrirSelectorDeImagen());
    }

    private void abrirSelectorDeImagen() {
        JFileChooser explorador = new JFileChooser();
        explorador.setFileFilter(new FileNameExtensionFilter("Imágenes (JPG, PNG)", "jpg", "jpeg", "png"));
        explorador.setAcceptAllFileFilterUsed(false);
        if (explorador.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            fotoSeleccionada = explorador.getSelectedFile();
            lblNombreFoto.setText(fotoSeleccionada.getName());
            lblNombreFoto.setForeground(new Color(0, 150, 0)); 
        }
    }

    private JLabel crearEtiqueta(String texto) {
        JLabel lbl = new JLabel(texto); lbl.setFont(new Font("Segoe UI", Font.BOLD, 14)); return lbl;
    }
    private JTextField crearCampoTexto() {
        JTextField txt = new JTextField(20); txt.setPreferredSize(new Dimension(200, 30)); return txt;
    }
    private JButton crearBotonEstilizado(String texto, Color bg) {
        JButton btn = new JButton(texto); btn.setBackground(bg); btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14)); btn.setPreferredSize(new Dimension(200, 45));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); btn.setFocusPainted(false); btn.setBorderPainted(false);
        return btn;
    }

    public String getNombre() { return nombre.getText(); }
    public String getTipoCedula() { return comboCedula.getSelectedItem().toString(); }
    public String getNumeroCedula() { return txtCedula.getText(); }
    public String getCorreo() { return correo.getText(); }
    public String getTipoCuenta() { return comboTipo.getSelectedItem() != null ? comboTipo.getSelectedItem().toString() : ""; } 
    public String getContraseña() { return new String(contraseña.getPassword()); }
    public String getConfirmarContraseña() { return new String(confirmarContraseña.getPassword()); }
    public File getFotoSeleccionada() { return fotoSeleccionada; } 

    public void addRegistroListener(ActionListener l) { btnEntrada.addActionListener(l); }
    public void addVolverListener(ActionListener l) { btnVolver.addActionListener(l); }
    public void mostrarError(String m) { JOptionPane.showMessageDialog(this, m, "Error", 0); }
    public void mostrarMensaje(String m) { JOptionPane.showMessageDialog(this, m, "Éxito", 1); }
}