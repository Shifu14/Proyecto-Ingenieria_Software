package sgcu.vistas;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;

public class RegistroView extends JFrame {

    private JTextField nombre;
    private JTextField correo;
    private JPasswordField contraseña;
    private JPasswordField confirmarContraseña;
    private JCheckBox chkVer;
    private JButton btnSubirFoto;
    private JLabel lblNombreFoto;
    private File fotoSeleccionada; // Variable para guardar el archivo de la foto
    
    private JButton btnEntrada;
    private JButton btnVolver;

    public RegistroView() {
        setTitle("REGISTRO DE USUARIO - SGCU");
        setSize(800, 750); 
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
        bloque.setPreferredSize(new Dimension(500, 580)); 
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

        gbc.gridy = 1; gbc.gridx = 0;
        bloque.add(crearEtiqueta("Nombre de usuario:"), gbc);
        nombre = crearCampoTexto();
        gbc.gridx = 1;
        bloque.add(nombre, gbc);

        gbc.gridy = 2; gbc.gridx = 0;
        bloque.add(crearEtiqueta("Correo electrónico:"), gbc);
        correo = crearCampoTexto();
        gbc.gridx = 1;
        bloque.add(correo, gbc);

        gbc.gridy = 3; gbc.gridx = 0;
        bloque.add(crearEtiqueta("Contraseña:"), gbc);
        contraseña = new JPasswordField(20);
        contraseña.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        bloque.add(contraseña, gbc);

        gbc.gridy = 4; gbc.gridx = 0;
        bloque.add(crearEtiqueta("Confirmar contraseña:"), gbc);
        confirmarContraseña = new JPasswordField(20);
        confirmarContraseña.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        bloque.add(confirmarContraseña, gbc);

        chkVer = new JCheckBox("Ver contraseñas");
        chkVer.setBackground(new Color(230, 230, 230));
        gbc.gridy = 5; gbc.gridx = 1;
        bloque.add(chkVer, gbc);

        // NUEVO: SECCIÓN DE SUBIR FOTO
        btnSubirFoto = crearBotonEstilizado("Subir Foto", new Color(40, 167, 69));
        btnSubirFoto.setPreferredSize(new Dimension(200, 35));
        lblNombreFoto = new JLabel("Ninguna foto seleccionada");
        lblNombreFoto.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        
        gbc.gridy = 6; gbc.gridx = 0;
        bloque.add(btnSubirFoto, gbc);
        gbc.gridx = 1;
        bloque.add(lblNombreFoto, gbc);

        // BOTONES DE ACCIÓN
        btnEntrada = crearBotonEstilizado("REGISTRARSE", new Color(0, 123, 255));
        gbc.gridy = 7; gbc.gridx = 0; gbc.gridwidth = 2;
        bloque.add(btnEntrada, gbc);

        btnVolver = crearBotonEstilizado("VOLVER", new Color(108, 117, 125));
        gbc.gridy = 8;
        bloque.add(btnVolver, gbc);

        panel.add(bloque);
        add(panel);

        // Lógica para ver contraseñas
        chkVer.addActionListener(e -> {
            char echo = chkVer.isSelected() ? (char) 0 : '•';
            contraseña.setEchoChar(echo);
            confirmarContraseña.setEchoChar(echo);
        });

        // Lógica para el botón de subir foto
        btnSubirFoto.addActionListener(e -> abrirSelectorDeImagen());
    }

    private void abrirSelectorDeImagen() {
        JFileChooser explorador = new JFileChooser();
        explorador.setDialogTitle("Seleccione una foto de perfil");
        
        // Filtro para que solo muestre imágenes
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("Imágenes (JPG, JPEG, PNG)", "jpg", "jpeg", "png");
        explorador.setFileFilter(filtro);
        explorador.setAcceptAllFileFilterUsed(false);

        int respuesta = explorador.showOpenDialog(this);
        if (respuesta == JFileChooser.APPROVE_OPTION) {
            fotoSeleccionada = explorador.getSelectedFile();
            lblNombreFoto.setText(fotoSeleccionada.getName());
            lblNombreFoto.setForeground(new Color(0, 150, 0)); // Texto verde si sale bien
        }
    }

    private JLabel crearEtiqueta(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return lbl;
    }

    private JTextField crearCampoTexto() {
        JTextField txt = new JTextField(20);
        txt.setPreferredSize(new Dimension(200, 30));
        return txt;
    }

    private JButton crearBotonEstilizado(String texto, Color bg) {
        JButton btn = new JButton(texto);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setPreferredSize(new Dimension(200, 45));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        return btn;
    }

    public String getNombre() { return nombre.getText(); }
    public String getCorreo() { return correo.getText(); }
    public String getContraseña() { return new String(contraseña.getPassword()); }
    public String getConfirmarContraseña() { return new String(confirmarContraseña.getPassword()); }
    public File getFotoSeleccionada() { return fotoSeleccionada; } // Nuevo getter

    public void addRegistroListener(ActionListener l) { btnEntrada.addActionListener(l); }
    public void addVolverListener(ActionListener l) { btnVolver.addActionListener(l); }
    public void mostrarError(String m) { JOptionPane.showMessageDialog(this, m, "Error", 0); }
    public void mostrarMensaje(String m) { JOptionPane.showMessageDialog(this, m, "Éxito", 1); }
}