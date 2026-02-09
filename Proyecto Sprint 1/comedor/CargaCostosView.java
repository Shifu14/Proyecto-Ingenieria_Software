package comedor;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Locale;

public class CargaCostosView extends JFrame {
    
    private JTable tablaCostos;
    private DefaultTableModel modeloTabla;
    
    private JTextField txtPeriodo, txtCostosFijos, txtCostosVariables, txtNumeroBandejas, txtPorcentajeMerma;
    private JButton btnAgregar, btnEliminar, btnLimpiar;
    
    // Botones Navegación
    private JButton btnMenuAdmin; 
    private JButton btnDashboard; // Nuevo

    public CargaCostosView() {
        setTitle("CÁLCULO DE COSTOS (CCB)");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        try {
            Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("media/icon.png"));
            if(icon != null) setIconImage(icon);
        } catch (Exception e) {}

        // --- ENCABEZADO ---
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(41, 41, 41)); 
        header.setPreferredSize(new Dimension(0, 80));
        header.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel title = new JLabel("GESTIÓN DE COSTOS");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        // --- PANEL DE NAVEGACIÓN CENTRADO ---
        JPanel panelNav = new JPanel(new GridBagLayout());
        panelNav.setOpaque(false);
        
        JPanel flowBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        flowBotones.setOpaque(false);

        btnMenuAdmin = crearBotonEstilizado("Gestión Menú", new Color(60, 60, 60));
        btnMenuAdmin.setPreferredSize(new Dimension(140, 35));

        btnDashboard = crearBotonEstilizado("Volver al Panel", new Color(0, 75, 237));
        btnDashboard.setPreferredSize(new Dimension(140, 35));

        flowBotones.add(btnMenuAdmin);
        flowBotones.add(btnDashboard);
        
        panelNav.add(flowBotones);

        header.add(title, BorderLayout.WEST);
        header.add(panelNav, BorderLayout.EAST);

        // --- CONTENIDO ---
        JPanel mainContent = new JPanel(new GridBagLayout());
        mainContent.setBackground(new Color(230, 230, 230));
        mainContent.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();

        // --- FORMULARIO ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                new EmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel lblForm = new JLabel("Nuevo Registro");
        lblForm.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblForm.setForeground(new Color(41, 41, 41));
        
        GridBagConstraints gbcTitle = new GridBagConstraints();
        gbcTitle.gridx=0; gbcTitle.gridy=0; gbcTitle.gridwidth=2; gbcTitle.insets=new Insets(0,0,15,0);
        formPanel.add(lblForm, gbcTitle);

        txtPeriodo = new JTextField();
        txtCostosFijos = new JTextField();
        txtCostosVariables = new JTextField();
        txtNumeroBandejas = new JTextField();
        txtPorcentajeMerma = new JTextField();

        agregarCampo(formPanel, "Periodo (Ej: Enero):", txtPeriodo, 1);
        agregarCampo(formPanel, "Costos Fijos ($):", txtCostosFijos, 2);
        agregarCampo(formPanel, "Costos Variables ($):", txtCostosVariables, 3);
        agregarCampo(formPanel, "Nro. Bandejas:", txtNumeroBandejas, 4);
        agregarCampo(formPanel, "% Merma:", txtPorcentajeMerma, 5);

        JPanel btnPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        btnAgregar = crearBotonEstilizado("CALCULAR Y GUARDAR", new Color(0, 75, 237));
        btnEliminar = crearBotonEstilizado("ELIMINAR SELECCIÓN", new Color(220, 53, 69));
        btnLimpiar = crearBotonEstilizado("LIMPIAR CAMPOS", new Color(108, 117, 125));

        btnPanel.add(btnAgregar);
        btnPanel.add(btnEliminar);
        btnPanel.add(btnLimpiar);

        GridBagConstraints gbcBtn = new GridBagConstraints();
        gbcBtn.gridx=0; gbcBtn.gridy=6; gbcBtn.gridwidth=2; gbcBtn.fill=GridBagConstraints.HORIZONTAL;
        formPanel.add(btnPanel, gbcBtn);

        // --- TABLA ---
        String[] columnas = {"Periodo", "C. Fijos", "C. Variables", "Bandejas", "% Merma", "CCB ($)"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        tablaCostos = new JTable(modeloTabla);
        estilizarTabla(tablaCostos);
        JScrollPane scrollTabla = new JScrollPane(tablaCostos);
        scrollTabla.getViewport().setBackground(Color.WHITE);
        scrollTabla.setBorder(BorderFactory.createEmptyBorder());

        // --- LAYOUT ---
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0.3; 
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 10);
        mainContent.add(formPanel, gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        gbc.weightx = 0.7; 
        gbc.insets = new Insets(0, 0, 0, 0);
        mainContent.add(scrollTabla, gbc);

        // --- LISTENERS LOCALES ---
        btnLimpiar.addActionListener(e -> limpiarCampos());
        tablaCostos.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tablaCostos.getSelectedRow();
                if (row != -1) {
                    txtPeriodo.setText(modeloTabla.getValueAt(row, 0).toString());
                    btnAgregar.setText("REGISTRO SELECCIONADO");
                    btnAgregar.setEnabled(false);
                }
            }
        });

        limpiarCampos();
        add(header, BorderLayout.NORTH);
        add(mainContent, BorderLayout.CENTER);
    }

    // --- MÉTODOS PÚBLICOS ---

    public void mostrarDatosTabla(List<CargaCostosModel> lista) {
        modeloTabla.setRowCount(0);
        for (CargaCostosModel c : lista) {
            modeloTabla.addRow(new Object[]{
                c.getPeriodo(),
                String.format(Locale.US, "%.2f", c.getCostosFijos()),
                String.format(Locale.US, "%.2f", c.getCostosVariables()),
                c.getNumeroBandejas(),
                c.getPorcentajeMerma() + "%",
                String.format(Locale.US, "%.2f", c.getCcB())
            });
        }
    }

    public String getPeriodo() { return txtPeriodo.getText(); }
    public String getCostosFijos() { return txtCostosFijos.getText(); }
    public String getCostosVariables() { return txtCostosVariables.getText(); }
    public String getNumeroBandejas() { return txtNumeroBandejas.getText(); }
    public String getMerma() { return txtPorcentajeMerma.getText(); }
    public String getPeriodoSeleccionado() {
        int row = tablaCostos.getSelectedRow();
        return (row >= 0) ? modeloTabla.getValueAt(row, 0).toString() : null;
    }

    public void addAgregarListener(ActionListener l) { btnAgregar.addActionListener(l); }
    public void addEliminarListener(ActionListener l) { btnEliminar.addActionListener(l); }
    public void addMenuAdminListener(ActionListener l) { btnMenuAdmin.addActionListener(l); }
    public void addDashboardListener(ActionListener l) { btnDashboard.addActionListener(l); } // Nuevo listener

    public void limpiarCampos() {
        txtPeriodo.setText("");
        txtCostosFijos.setText("");
        txtCostosVariables.setText("");
        txtNumeroBandejas.setText("");
        txtPorcentajeMerma.setText("");
        tablaCostos.clearSelection();
        btnAgregar.setText("CALCULAR Y GUARDAR");
        btnAgregar.setEnabled(true);
    }
    public void mostrarMensaje(String msg) { JOptionPane.showMessageDialog(this, msg); }
    public void mostrarError(String msg) { JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE); }

    private void agregarCampo(JPanel panel, String label, JTextField field, int y) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        GridBagConstraints gbcL = new GridBagConstraints();
        gbcL.gridx=0; gbcL.gridy=y; gbcL.anchor=GridBagConstraints.WEST; gbcL.insets=new Insets(5,0,5,5);
        panel.add(lbl, gbcL);
        GridBagConstraints gbcF = new GridBagConstraints();
        gbcF.gridx=1; gbcF.gridy=y; gbcF.fill=GridBagConstraints.HORIZONTAL; gbcF.weightx=1.0;
        panel.add(field, gbcF);
    }
    
    private void estilizarTabla(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(35);
        table.setSelectionBackground(new Color(0, 75, 237));
        table.setSelectionForeground(Color.WHITE);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(230, 230, 230));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(41, 41, 41));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setReorderingAllowed(false);
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for(int i=0; i<table.getColumnCount(); i++) table.getColumnModel().getColumn(i).setCellRenderer(center);
    }
    
    // Botón con bordes redondeados
    private JButton crearBotonEstilizado(String texto, Color bg) {
        JButton btn = new JButton(texto) {
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
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CargaCostosController());
    }
}