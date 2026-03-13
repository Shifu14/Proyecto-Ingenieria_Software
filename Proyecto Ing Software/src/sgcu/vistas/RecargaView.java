package sgcu.vistas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class RecargaView extends JFrame {
    private JComboBox<String> cbBancos, cbPrefijo, cbTipoCI;
    private JTextField txtTelefono, txtCedula, txtMonto;
    private JButton btnGenerar, btnVerificar, btnVolver;
    
    // Campos para la verificación
    private JTextField txtRefGenerada, txtRefIngresada;

    public RecargaView() {
        setTitle("SGCU - PROCESADOR DE PAGO MÓVIL");
        // 1. Reducimos el tamaño de la ventana
        setSize(420, 550); 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        // 2. Reducimos los márgenes verticales (Insets) para juntar los elementos
        gbc.insets = new Insets(5, 15, 5, 15); 

        // --- SECCIÓN 1: FORMULARIO DE EMISIÓN ---
        String[] bancos = {
            "0102 - BANCO DE VENEZUELA", "0105 - MERCANTIL", "0108 - PROVINCIAL", 
            "0134 - BANESCO", "0191 - BNC", "0172 - BANCAMIGA", "0114 - EXTERIOR",
            "0163 - DEL TESORO", "0175 - BICENTENARIO", "0128 - CARONÍ", "0151 - BFC",
            "0174 - BANPLUS", "0138 - PLAZA", "0166 - AGRÍCOLA", "0169 - MI BANCO"
        };
        cbBancos = new JComboBox<>(bancos);
        
        cbPrefijo = new JComboBox<>(new String[]{"0412", "0414", "0424", "0416", "0426"});
        txtTelefono = new JTextField(); 
        restringirNumeros(txtTelefono, 7);
        
        cbTipoCI = new JComboBox<>(new String[]{"V", "E", "J", "P"});
        txtCedula = new JTextField(); 
        restringirNumeros(txtCedula, 9);
        
        txtMonto = new JTextField();

        btnGenerar = new JButton("GENERAR PAGO MÓVIL");
        btnGenerar.setBackground(new Color(0, 123, 255));
        btnGenerar.setForeground(Color.WHITE);
        btnGenerar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnGenerar.setPreferredSize(new Dimension(0, 35));

        // Construcción de la cuadrícula
        gbc.gridx = 0; gbc.gridy = 0; centerPanel.add(new JLabel("Banco Destino:"), gbc);
        gbc.gridx = 1; centerPanel.add(cbBancos, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; centerPanel.add(new JLabel("Número Telefónico:"), gbc);
        JPanel pTel = new JPanel(new BorderLayout(5, 0)); pTel.setOpaque(false);
        pTel.add(cbPrefijo, BorderLayout.WEST); pTel.add(txtTelefono, BorderLayout.CENTER);
        gbc.gridx = 1; centerPanel.add(pTel, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; centerPanel.add(new JLabel("Cédula / RIF:"), gbc);
        JPanel pCI = new JPanel(new BorderLayout(5, 0)); pCI.setOpaque(false);
        pCI.add(cbTipoCI, BorderLayout.WEST); pCI.add(txtCedula, BorderLayout.CENTER);
        gbc.gridx = 1; centerPanel.add(pCI, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; centerPanel.add(new JLabel("Monto a Recargar (Bs):"), gbc);
        gbc.gridx = 1; centerPanel.add(txtMonto, gbc);
        
        gbc.gridx = 1; gbc.gridy = 4; 
        gbc.insets = new Insets(15, 15, 5, 15); // Un poco de espacio antes del botón
        centerPanel.add(btnGenerar, gbc);

        // --- SECCIÓN 2: VERIFICACIÓN BANCARIA ---
        // Juntamos más los elementos de este panel reduciendo el espacio del GridLayout
        JPanel pnlVerif = new JPanel(new GridLayout(4, 1, 2, 4)); 
        pnlVerif.setBackground(new Color(245, 245, 245));
        pnlVerif.setBorder(BorderFactory.createTitledBorder(null, "VERIFICACIÓN BANCARIA", TitledBorder.CENTER, TitledBorder.TOP));
        
        txtRefGenerada = new JTextField("Esperando emisión...");
        txtRefGenerada.setEditable(false);
        txtRefGenerada.setHorizontalAlignment(SwingConstants.CENTER);
        txtRefGenerada.setFont(new Font("Segoe UI", Font.BOLD, 14));
        txtRefGenerada.setBackground(new Color(230, 240, 255));

        txtRefIngresada = new JTextField();
        txtRefIngresada.setHorizontalAlignment(SwingConstants.CENTER);
        txtRefIngresada.setToolTipText("Pegue aquí su referencia para validar");

        btnVerificar = new JButton("VERIFICAR RECARGA");
        btnVerificar.setBackground(new Color(40, 167, 69));
        btnVerificar.setForeground(Color.WHITE);
        btnVerificar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnVerificar.setEnabled(false);

        pnlVerif.add(new JLabel("Referencia Generada (Cópiela):", SwingConstants.CENTER));
        pnlVerif.add(txtRefGenerada);
        pnlVerif.add(txtRefIngresada);
        pnlVerif.add(btnVerificar);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; 
        gbc.insets = new Insets(20, 15, 10, 15); 
        centerPanel.add(pnlVerif, gbc);

        // Relleno inferior invisible para empujar todo hacia arriba
        gbc.gridy = 6; gbc.weighty = 1.0;
        centerPanel.add(Box.createGlue(), gbc);

        // --- FOOTER ---
        JPanel footer = new JPanel();
        footer.setBackground(Color.WHITE);
        footer.setBorder(new EmptyBorder(5, 0, 10, 0));
        btnVolver = new JButton("VOLVER AL MONEDERO");
        btnVolver.setFocusPainted(false);
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        footer.add(btnVolver);

        add(centerPanel, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);
    }

    private void restringirNumeros(JTextField f, int lim) {
        f.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar()) || f.getText().length() >= lim) e.consume();
            }
        });
    }

    // Getters y Setters
    public String getMonto() { return txtMonto.getText().trim(); }
    public String getTelefono() { return txtTelefono.getText().trim(); }
    public String getCedula() { return txtCedula.getText().trim(); }
    public String getRefIngresada() { return txtRefIngresada.getText().trim(); }
    
    public void setRefGenerada(String ref) { 
        txtRefGenerada.setText(ref); 
        btnVerificar.setEnabled(true); 
    }

    public void addGenerarListener(ActionListener l) { btnGenerar.addActionListener(l); }
    public void addVerificarListener(ActionListener l) { btnVerificar.addActionListener(l); }
    public void addVolverListener(ActionListener l) { btnVolver.addActionListener(l); }
}