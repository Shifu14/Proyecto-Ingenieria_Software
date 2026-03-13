package sgcu.vistas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class SaldoPanaView extends JFrame {
    
    private JComboBox<String> comboCedula;
    private JTextField txtCedula;
    private JTextField txtMonto;
    private JButton btnTransferir, btnVolver;

    public SaldoPanaView() {
        setTitle("SALDO PANA - SGCU");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ENCABEZADO
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(41, 41, 41)); 
        header.setPreferredSize(new Dimension(0, 80));
        header.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel title = new JLabel("APOYO SALDO PANA");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        
        btnVolver = crearBotonEstilizado("Volver", new Color(108, 117, 125));
        btnVolver.setPreferredSize(new Dimension(100, 35));
        JPanel panelVolver = new JPanel(new GridBagLayout());
        panelVolver.setOpaque(false);
        panelVolver.add(btnVolver);

        header.add(title, BorderLayout.WEST);
        header.add(panelVolver, BorderLayout.EAST);

        // CONTENIDO CENTRAL
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setBackground(new Color(230, 230, 230));
        
        JPanel bloque = new JPanel(new GridBagLayout());
        bloque.setBackground(Color.WHITE);
        bloque.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)), new EmptyBorder(20, 20, 20, 20)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblInfo = new JLabel("<html><center>Transfiere saldo a un compañero<br>ingresando su Cédula de Identidad.</center></html>");
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblInfo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        bloque.add(lblInfo, gbc);

        // CÉDULA DEL PANA
        gbc.gridwidth = 1; gbc.gridy = 1; gbc.gridx = 0;
        bloque.add(new JLabel("C.I. del Compañero:"), gbc);
        
        JPanel panelCedula = new JPanel(new BorderLayout(5, 0));
        panelCedula.setOpaque(false);
        comboCedula = new JComboBox<>(new String[]{"V-", "E-"});
        txtCedula = new JTextField(10);
        txtCedula.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar()) || txtCedula.getText().length() >= 9) e.consume();
            }
        });
        panelCedula.add(comboCedula, BorderLayout.WEST);
        panelCedula.add(txtCedula, BorderLayout.CENTER);
        gbc.gridx = 1; bloque.add(panelCedula, gbc);

        // MONTO
        gbc.gridy = 2; gbc.gridx = 0;
        bloque.add(new JLabel("Monto a enviar (Bs):"), gbc);
        txtMonto = new JTextField(10);
        txtMonto.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != '.') e.consume();
            }
        });
        gbc.gridx = 1; bloque.add(txtMonto, gbc);

        // BOTÓN TRANSFERIR
        btnTransferir = crearBotonEstilizado("TRANSFERIR SALDO", new Color(40, 167, 69));
        btnTransferir.setPreferredSize(new Dimension(200, 45));
        gbc.gridy = 3; gbc.gridx = 0; gbc.gridwidth = 2;
        bloque.add(btnTransferir, gbc);

        panelCentral.add(bloque);
        
        add(header, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
    }

    public String getCedulaCompleta() { return comboCedula.getSelectedItem().toString() + txtCedula.getText().trim(); }
    public String getCedulaNumeros() { return txtCedula.getText().trim(); }
    public String getMonto() { return txtMonto.getText().trim(); }

    public void limpiarCampos() { txtCedula.setText(""); txtMonto.setText(""); }

    public void addTransferirListener(ActionListener l) { btnTransferir.addActionListener(l); }
    public void addVolverListener(ActionListener l) { btnVolver.addActionListener(l); }

    public void mostrarMensaje(String msg) { JOptionPane.showMessageDialog(this, msg, "Éxito", JOptionPane.INFORMATION_MESSAGE); }
    public void mostrarError(String msg) { JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE); }

    private JButton crearBotonEstilizado(String texto, Color bg) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) g2.setColor(bg.darker());
                else g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}