package sgcu.vistas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class GestionEstudiantilView extends JFrame {

    private JTable tablaBeneficios;
    private DefaultTableModel modeloTabla;
    private JComboBox<String> comboCedula, comboTipo;
    private JTextField txtCedula, txtDescuento;
    private JButton btnGuardar, btnEditar, btnEliminar, btnDashboard;

    
    private JLabel lblPreviewPrecio;

    public GestionEstudiantilView() {
        setTitle("GESTIÓN ESTUDIANTIL - SGCU");
        setSize(1100, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(41, 41, 41));
        header.setPreferredSize(new Dimension(0, 80));
        header.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel title = new JLabel("BENEFICIOS ESTUDIANTILES");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));

        btnDashboard = crearBoton("Volver al Panel", new Color(0, 75, 237), Color.WHITE);
        btnDashboard.setPreferredSize(new Dimension(140, 35));

        JPanel panelNav = new JPanel(new GridBagLayout());
        panelNav.setOpaque(false);
        panelNav.add(btnDashboard);

        header.add(title, BorderLayout.WEST);
        header.add(panelNav, BorderLayout.EAST);

        
        JPanel mainContent = new JPanel(new GridBagLayout());
        mainContent.setBackground(new Color(230, 230, 230));
        mainContent.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();

        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            new EmptyBorder(15, 15, 15, 15)
        ));

        GridBagConstraints gf = new GridBagConstraints();
        gf.fill = GridBagConstraints.HORIZONTAL;
        gf.insets = new Insets(8, 5, 8, 5);

        
        JLabel lblForm = new JLabel("Asignar Beneficio");
        lblForm.setFont(new Font("Segoe UI", Font.BOLD, 18));
        gf.gridx = 0; gf.gridy = 0; gf.gridwidth = 2;
        formPanel.add(lblForm, gf);

        
        gf.gridwidth = 1; gf.gridy = 1; gf.gridx = 0;
        formPanel.add(crearEtiqueta("C.I. Estudiante:"), gf);

        JPanel panelCedula = new JPanel(new BorderLayout(5, 0));
        panelCedula.setOpaque(false);
        comboCedula = new JComboBox<>(new String[]{"V-", "E-"});
        txtCedula = new JTextField(12);
        txtCedula.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar()) || txtCedula.getText().length() >= 9)
                    e.consume();
            }
        });
        panelCedula.add(comboCedula, BorderLayout.WEST);
        panelCedula.add(txtCedula, BorderLayout.CENTER);
        gf.gridx = 1; formPanel.add(panelCedula, gf);

        
        gf.gridy = 2; gf.gridx = 0;
        formPanel.add(crearEtiqueta("Tipo de Beneficio:"), gf);
        comboTipo = new JComboBox<>(new String[]{"Exonerado", "Becario"});
        gf.gridx = 1; formPanel.add(comboTipo, gf);

        
        gf.gridy = 3; gf.gridx = 0;
        formPanel.add(crearEtiqueta("% Descuento:"), gf);

        txtDescuento = new JTextField();
        txtDescuento.setEnabled(false);
        
        txtDescuento.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                String actual = txtDescuento.getText();
                if (!Character.isDigit(c) && c != '.') { e.consume(); return; }
                if (c == '.' && actual.contains(".")) e.consume(); 
            }
        });
        gf.gridx = 1; formPanel.add(txtDescuento, gf);

        
        JLabel lblNota = new JLabel("<html><i>Becario: entre 1 % y 99 %<br>(no puede igualar al Exonerado)</i></html>");
        lblNota.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblNota.setForeground(new Color(120, 120, 120));
        gf.gridy = 4; gf.gridx = 0; gf.gridwidth = 2;
        formPanel.add(lblNota, gf);

        
        lblPreviewPrecio = new JLabel(" ");
        lblPreviewPrecio.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPreviewPrecio.setForeground(new Color(0, 100, 180));
        lblPreviewPrecio.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 220, 255)),
            new EmptyBorder(6, 8, 6, 8)
        ));
        lblPreviewPrecio.setOpaque(true);
        lblPreviewPrecio.setBackground(new Color(235, 245, 255));
        lblPreviewPrecio.setVisible(false);
        gf.gridy = 5; gf.gridwidth = 2;
        formPanel.add(lblPreviewPrecio, gf);

        
        btnGuardar  = crearBoton("GUARDAR BENEFICIO",   new Color(40, 167, 69),  Color.WHITE);
        btnEditar   = crearBoton("EDITAR BENEFICIO",    new Color(255, 193, 7),  Color.WHITE);
        btnEliminar = crearBoton("ELIMINAR SELECCIÓN",  new Color(220, 53, 69),  Color.WHITE);

        gf.gridy = 6; gf.gridwidth = 2; formPanel.add(btnGuardar,  gf);
        gf.gridy = 7;                   formPanel.add(btnEditar,   gf);
        gf.gridy = 8;                   formPanel.add(btnEliminar, gf);

        
        comboTipo.addActionListener(e -> {
            boolean esBecario = "Becario".equals(comboTipo.getSelectedItem());
            txtDescuento.setEnabled(esBecario);
            if (!esBecario) txtDescuento.setText("");
        });

        
        String[] columnas = {"Cédula", "Tipo Beneficio", "Descuento"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaBeneficios = new JTable(modeloTabla);
        estilizarTabla(tablaBeneficios);
        JScrollPane scrollTabla = new JScrollPane(tablaBeneficios);
        scrollTabla.getViewport().setBackground(Color.WHITE);

        
        tablaBeneficios.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tablaBeneficios.getSelectedRow();
                if (row < 0) return;

                String cedulaCompleta = modeloTabla.getValueAt(row, 0).toString();
                if (cedulaCompleta.length() > 2) {
                    comboCedula.setSelectedItem(cedulaCompleta.substring(0, 2));
                    txtCedula.setText(cedulaCompleta.substring(2));
                }

                String tipo = modeloTabla.getValueAt(row, 1).toString();
                comboTipo.setSelectedItem(tipo);

                if (tipo.equals("Becario")) {
                    
                    String raw = modeloTabla.getValueAt(row, 2).toString()
                                           .replace("%", "").replace("(Exonerado)", "").trim();
                    txtDescuento.setText(raw);
                    txtDescuento.setEnabled(true);
                } else {
                    txtDescuento.setText("");
                    txtDescuento.setEnabled(false);
                }
            }
        });

        
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0.33; gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 10);
        mainContent.add(formPanel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.67;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainContent.add(scrollTabla, gbc);

        add(header, BorderLayout.NORTH);
        add(mainContent, BorderLayout.CENTER);
    }

    

    public String getCedulaCompleta()  { return comboCedula.getSelectedItem() + txtCedula.getText().trim(); }
    public String getCedulaNumeros()   { return txtCedula.getText().trim(); }
    public String getTipo()            { return comboTipo.getSelectedItem().toString(); }
    public String getDescuento()       { return txtDescuento.getText().trim(); }
    public DefaultTableModel getModeloTabla() { return modeloTabla; }

    public String getCedulaSeleccionada() {
        int row = tablaBeneficios.getSelectedRow();
        return row >= 0 ? modeloTabla.getValueAt(row, 0).toString() : null;
    }

    public void limpiarCampos() {
        txtCedula.setText("");
        txtDescuento.setText("");
        comboTipo.setSelectedIndex(0);
        txtDescuento.setEnabled(false);
        tablaBeneficios.clearSelection();
    }

    

    public void mostrarPreviewPrecio(String texto) {
        lblPreviewPrecio.setText("<html>" + texto.replace("\n", "<br>") + "</html>");
        lblPreviewPrecio.setVisible(true);
    }

    public void ocultarPreviewPrecio() {
        lblPreviewPrecio.setText(" ");
        lblPreviewPrecio.setVisible(false);
    }

    

    public void addGuardarListener(ActionListener l)  { btnGuardar.addActionListener(l); }
    public void addEditarListener(ActionListener l)   { btnEditar.addActionListener(l); }
    public void addEliminarListener(ActionListener l) { btnEliminar.addActionListener(l); }
    public void addDashboardListener(ActionListener l){ btnDashboard.addActionListener(l); }

    
    public void addDescuentoChangeListener(DocumentListener l) {
        txtDescuento.getDocument().addDocumentListener(l);
    }

    
    public void addTipoChangeListener(ActionListener l) {
        comboTipo.addActionListener(l);
    }

    

    public void mostrarMensaje(String msg) { JOptionPane.showMessageDialog(this, msg, "Éxito", JOptionPane.INFORMATION_MESSAGE); }
    public void mostrarError(String msg)   { JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE); }

   

    private JLabel crearEtiqueta(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return lbl;
    }

    private void estilizarTabla(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(41, 41, 41));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setReorderingAllowed(false);
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++)
            table.getColumnModel().getColumn(i).setCellRenderer(center);
    }

    private JButton crearBoton(String texto, Color bg, Color fg) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? bg.darker() : bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}