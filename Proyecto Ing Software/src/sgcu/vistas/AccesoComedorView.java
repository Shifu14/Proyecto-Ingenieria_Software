package sgcu.vistas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;

public class AccesoComedorView extends JFrame {
    private JButton btnSubirFoto, btnVerificar, btnCancelar;
    private JLabel lblNombreFoto, lblInstruccion;
    private File fotoSeleccionada;

    public AccesoComedorView() {
        setTitle("VERIFICACIÓN FACIAL Y COBRO - SGCU");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel header = new JPanel();
        header.setBackground(new Color(41, 41, 41));
        header.setPreferredSize(new Dimension(0, 60));
        JLabel title = new JLabel("CONTROL DE ACCESO");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        header.add(title);

        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0; gbc.fill = GridBagConstraints.HORIZONTAL;

        lblInstruccion = new JLabel("<html><center>Por favor, suba su fotografía para el<br>reconocimiento facial.<br><b>Costo del servicio: 500.00 Bs</b></center></html>");
        lblInstruccion.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblInstruccion.setHorizontalAlignment(SwingConstants.CENTER);
        
        btnSubirFoto = crearBoton("Examinar Imagen", new Color(108, 117, 125));
        lblNombreFoto = new JLabel("Ningún archivo seleccionado");
        lblNombreFoto.setHorizontalAlignment(SwingConstants.CENTER);
        lblNombreFoto.setForeground(Color.GRAY);

        gbc.gridy = 0; center.add(lblInstruccion, gbc);
        gbc.gridy = 1; center.add(btnSubirFoto, gbc);
        gbc.gridy = 2; center.add(lblNombreFoto, gbc);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        footer.setBackground(Color.WHITE);
        footer.setBorder(new EmptyBorder(10, 0, 20, 0));
        
        btnVerificar = crearBoton("VERIFICAR Y PAGAR", new Color(40, 167, 69));
        btnCancelar = crearBoton("CANCELAR", new Color(220, 53, 69));
        
        footer.add(btnVerificar);
        footer.add(btnCancelar);

        add(header, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);

        btnSubirFoto.addActionListener(e -> abrirSelector());
    }

    private void abrirSelector() {
        JFileChooser explorador = new JFileChooser();
        explorador.setFileFilter(new FileNameExtensionFilter("Imágenes (JPG, PNG)", "jpg", "jpeg", "png"));
        explorador.setAcceptAllFileFilterUsed(false);
        if (explorador.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            fotoSeleccionada = explorador.getSelectedFile();
            lblNombreFoto.setText(fotoSeleccionada.getName());
            lblNombreFoto.setForeground(new Color(0, 150, 0));
        }
    }

    private JButton crearBoton(String txt, Color c) {
        JButton b = new JButton(txt);
        b.setBackground(c); b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setFocusPainted(false); b.setBorderPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(180, 40));
        return b;
    }

    public File getFotoSeleccionada() { return fotoSeleccionada; }
    public void addVerificarListener(ActionListener l) { btnVerificar.addActionListener(l); }
    public void addCancelarListener(ActionListener l) { btnCancelar.addActionListener(l); }
    public void mostrarMensaje(String m, boolean esError) {
        JOptionPane.showMessageDialog(this, m, esError ? "Error" : "Éxito", esError ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
    }
}