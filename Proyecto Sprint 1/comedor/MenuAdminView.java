package comedor;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class MenuAdminView extends JFrame {
    
    private JTable tablaMenu;
    private DefaultTableModel modeloTabla;
    
    private JTextField txtId, txtNombre, txtDescripcion, txtPrecio, txtHora;
    private JComboBox<String> comboDias;
    private JButton btnAgregar, btnEditar, btnEliminar, btnLimpiar;
    
    // Botones de Navegación
    private JButton btnCostos; 
    private JButton btnDashboard; // Nuevo botón

    public MenuAdminView() {
        setTitle("ADMINISTRACIÓN DE MENÚ");
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

        JLabel title = new JLabel("GESTIÓN DE MENÚ");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        // --- PANEL DE BOTONES SUPERIOR (Navegación) ---
        // Usamos GridBagLayout para centrado vertical perfecto (igual que el Dashboard)
        JPanel panelNav = new JPanel(new GridBagLayout());
        panelNav.setOpaque(false);
        
        // Contenedor interno para poner los botones uno al lado del otro
        JPanel flowBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        flowBotones.setOpaque(false);

        btnCostos = crearBotonEstilizado("Ir a Costos", new Color(60, 60, 60));
        btnCostos.setPreferredSize(new Dimension(140, 35)); // Tamaño fijo igual al dashboard

        btnDashboard = crearBotonEstilizado("Volver al Panel", new Color(0, 75, 237)); // Azul
        btnDashboard.setPreferredSize(new Dimension(140, 35));

        flowBotones.add(btnCostos);
        flowBotones.add(btnDashboard);
        
        panelNav.add(flowBotones); // Añadimos el flow al GridBag

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
        
        JLabel lblForm = new JLabel("Datos del Plato");
        lblForm.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblForm.setForeground(new Color(41, 41, 41));
        
        GridBagConstraints gbcTitle = new GridBagConstraints();
        gbcTitle.gridx=0; gbcTitle.gridy=0; gbcTitle.gridwidth=2; gbcTitle.insets=new Insets(0,0,15,0);
        formPanel.add(lblForm, gbcTitle);

        txtId = new JTextField();
        txtNombre = new JTextField();
        txtDescripcion = new JTextField();
        txtPrecio = new JTextField();
        txtHora = new JTextField();

        agregarCampo(formPanel, "ID:", txtId, 1, false);
        agregarCampo(formPanel, "Nombre:", txtNombre, 2, true);
        agregarCampo(formPanel, "Descripción:", txtDescripcion, 3, true);
        agregarCampo(formPanel, "Precio (Bs):", txtPrecio, 4, true);
        
        JLabel lblDia = new JLabel("Día:");
        lblDia.setFont(new Font("Segoe UI", Font.BOLD, 14));
        String[] dias = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};
        comboDias = new JComboBox<>(dias);
        comboDias.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboDias.setBackground(Color.WHITE);
        GridBagConstraints gbcL = new GridBagConstraints();
        gbcL.gridx=0; gbcL.gridy=5; gbcL.anchor=GridBagConstraints.WEST; gbcL.insets=new Insets(5,0,5,5);
        formPanel.add(lblDia, gbcL);
        GridBagConstraints gbcF = new GridBagConstraints();
        gbcF.gridx=1; gbcF.gridy=5; gbcF.fill=GridBagConstraints.HORIZONTAL; gbcF.weightx=1.0;
        formPanel.add(comboDias, gbcF);

        agregarCampo(formPanel, "Hora:", txtHora, 6, true);

        JPanel btnPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(new EmptyBorder(15, 0, 0, 0));

        btnAgregar = crearBotonEstilizado("AGREGAR", new Color(0, 75, 237));
        btnEditar = crearBotonEstilizado("EDITAR", new Color(255, 165, 0));
        btnEliminar = crearBotonEstilizado("ELIMINAR", new Color(220, 53, 69));
        btnLimpiar = crearBotonEstilizado("LIMPIAR", new Color(108, 117, 125));

        btnPanel.add(btnAgregar);
        btnPanel.add(btnEditar);
        btnPanel.add(btnEliminar);
        btnPanel.add(btnLimpiar);

        GridBagConstraints gbcBtn = new GridBagConstraints();
        gbcBtn.gridx=0; gbcBtn.gridy=7; gbcBtn.gridwidth=2; gbcBtn.fill=GridBagConstraints.HORIZONTAL;
        formPanel.add(btnPanel, gbcBtn);

        // --- TABLA ---
        String[] columnas = {"ID", "Nombre", "Descripción", "Precio", "Día", "Hora"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        tablaMenu = new JTable(modeloTabla);
        estilizarTabla(tablaMenu);
        JScrollPane scrollTabla = new JScrollPane(tablaMenu);
        scrollTabla.getViewport().setBackground(Color.WHITE);
        scrollTabla.setBorder(BorderFactory.createEmptyBorder());

        // --- LAYOUT ---
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0.35; 
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 10);
        mainContent.add(formPanel, gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        gbc.weightx = 0.65;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainContent.add(scrollTabla, gbc);

        // --- LISTENERS ---
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        
        tablaMenu.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                cargarDatosDeFila();
            }
        });

        limpiarFormulario(); 

        add(header, BorderLayout.NORTH);
        add(mainContent, BorderLayout.CENTER);
    }

    // --- MÉTODOS PÚBLICOS ---

    public void mostrarDatosTabla(List<MenuAdminModel> lista) {
        modeloTabla.setRowCount(0);
        for(MenuAdminModel p : lista) {
            modeloTabla.addRow(new Object[]{
                p.getId(), p.getNombre(), p.getDescripcion(), 
                p.getPrecio(), p.getDia(), p.getHora()
            });
        }
    }

    public String getId() { return txtId.getText(); }
    public String getNombre() { return txtNombre.getText(); }
    public String getDescripcion() { return txtDescripcion.getText(); }
    public String getPrecio() { return txtPrecio.getText(); }
    public String getHora() { return txtHora.getText(); }
    public String getDia() { return comboDias.getSelectedItem().toString(); }

    public void addAgregarListener(ActionListener l) { btnAgregar.addActionListener(l); }
    public void addEditarListener(ActionListener l) { btnEditar.addActionListener(l); }
    public void addEliminarListener(ActionListener l) { btnEliminar.addActionListener(l); }
    public void addCostosListener(ActionListener l) { btnCostos.addActionListener(l); }
    public void addDashboardListener(ActionListener l) { btnDashboard.addActionListener(l); } // Listener nuevo

    public void limpiarFormulario() {
        txtId.setText("");
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtPrecio.setText("");
        txtHora.setText("");
        comboDias.setSelectedIndex(0);
        tablaMenu.clearSelection();
        btnAgregar.setEnabled(true);
        btnEditar.setEnabled(false);
        btnEliminar.setEnabled(false);
    }

    public void mostrarMensaje(String msg) { JOptionPane.showMessageDialog(this, msg); }
    public void mostrarError(String msg) { JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE); }

    private void cargarDatosDeFila() {
        int row = tablaMenu.getSelectedRow();
        if(row != -1) {
            txtId.setText(modeloTabla.getValueAt(row, 0).toString());
            txtNombre.setText(modeloTabla.getValueAt(row, 1).toString());
            txtDescripcion.setText(modeloTabla.getValueAt(row, 2).toString());
            txtPrecio.setText(modeloTabla.getValueAt(row, 3).toString());
            comboDias.setSelectedItem(modeloTabla.getValueAt(row, 4).toString());
            txtHora.setText(modeloTabla.getValueAt(row, 5).toString());
            btnAgregar.setEnabled(false);
            btnEditar.setEnabled(true);
            btnEliminar.setEnabled(true);
        }
    }

    private void agregarCampo(JPanel panel, String label, JTextField field, int y, boolean editable) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setEditable(editable);
        if(!editable) field.setBackground(new Color(240,240,240));

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
        
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(41, 41, 41));
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for(int i=0; i<table.getColumnCount(); i++) table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
    }
    
    // Botón estilo Dashboard (redondeado)
    private JButton crearBotonEstilizado(String texto, Color bg) {
        JButton btn = new JButton(texto) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) g2.setColor(bg.darker());
                else g2.setColor(bg);
                // Borde redondeado
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
        SwingUtilities.invokeLater(() -> new MenuAdminController());
    }
}