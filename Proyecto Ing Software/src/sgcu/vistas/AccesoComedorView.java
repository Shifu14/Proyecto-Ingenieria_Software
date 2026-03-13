package sgcu.vistas;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Locale;

public class AccesoComedorView extends JFrame {
    private JButton btnSubirFoto, btnVerificar, btnCancelar;
    private JLabel lblNombreFoto, lblInstruccion, lblInfoUsuario, lblMontoCCB, lblMontoPagar;
    private File fotoSeleccionada;

    public AccesoComedorView() {
        setTitle("VERIFICACIÓN FACIAL Y COBRO - SGCU");
        setSize(550, 520); 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel header = new JPanel();
        header.setBackground(new Color(41, 41, 41));
        header.setPreferredSize(new Dimension(0, 60));
        JLabel title = new JLabel("CONTROL DE ACCESO Y COBRO");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        header.add(title);

        // PANEL CENTRAL 
        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // 1. Info del Usuario y Porcentaje
        gbc.gridx = 0; gbc.gridy = 0;
        lblInfoUsuario = new JLabel("Cargando...", SwingConstants.CENTER);
        lblInfoUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        center.add(lblInfoUsuario, gbc);

        // 2. Valor Real (CCB)
        gbc.gridy = 1;
        lblMontoCCB = new JLabel("Valor Real (CCB) de los platos: 0.00 Bs", SwingConstants.CENTER);
        lblMontoCCB.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblMontoCCB.setForeground(Color.GRAY);
        center.add(lblMontoCCB, gbc);

        // 3. Tarifa Final Dinámica
        gbc.gridy = 2;
        lblMontoPagar = new JLabel("Tarifa a cobrar: 0.00 Bs", SwingConstants.CENTER);
        lblMontoPagar.setFont(new Font("Segoe UI", Font.BOLD, 20));
        center.add(lblMontoPagar, gbc);

        gbc.gridy = 3;
        center.add(new JSeparator(), gbc);

        // 4. Reconocimiento Facial
        gbc.gridy = 4;
        lblInstruccion = new JLabel("Por favor, suba una foto de su rostro para verificar:", SwingConstants.CENTER);
        lblInstruccion.setFont(new Font("Segoe UI", Font.BOLD, 14));
        center.add(lblInstruccion, gbc);

        gbc.gridy = 5;
        btnSubirFoto = crearBoton("Examinar Cámara...", new Color(108, 117, 125));
        center.add(btnSubirFoto, gbc);

        gbc.gridy = 6;
        lblNombreFoto = new JLabel("Ningún archivo seleccionado", SwingConstants.CENTER);
        lblNombreFoto.setForeground(Color.GRAY);
        center.add(lblNombreFoto, gbc);

        // BOTONES INFERIORES
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        footer.setBackground(new Color(240, 240, 240));
        btnVerificar = crearBoton("Verificar y Pagar", new Color(0, 123, 255));
        btnCancelar = crearBoton("Cancelar", new Color(220, 53, 69));
        footer.add(btnVerificar);
        footer.add(btnCancelar);

        add(header, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);

        btnSubirFoto.addActionListener(e -> abrirSelector());
    }

    public void actualizarInfoVista(String tipoUser, String beneficio, int cantPlatos, double ccbTotal, double tarifaFinal) {
        lblInfoUsuario.setText("<html><center>Usuario: <b>" + tipoUser + "</b><br>Estatus de Tarifa: <b>" + beneficio + "</b><br>Platos en carrito: " + cantPlatos + "</center></html>");
        lblMontoCCB.setText("Valor referencial de producción (Suma CCB): " + String.format(Locale.US, "%.2f", ccbTotal) + " Bs");
        
        if (tarifaFinal <= 0) {
            lblMontoPagar.setText("TOTAL A PAGAR: ¡GRATIS! (100% Exonerado)");
            lblMontoPagar.setForeground(new Color(40, 167, 69)); // Verde
        } else {
            lblMontoPagar.setText("TOTAL A PAGAR: " + String.format(Locale.US, "%.2f", tarifaFinal) + " Bs");
            lblMontoPagar.setForeground(new Color(220, 53, 69)); // Rojo
        }
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