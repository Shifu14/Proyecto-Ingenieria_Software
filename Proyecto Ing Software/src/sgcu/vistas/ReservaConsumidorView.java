package sgcu.vistas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionListener;

public class ReservaConsumidorView extends JFrame {

    private JTable tablaReservas;
    private JTextField txtBuscar;
    private JButton btnDashboard, btnPagar, btnFiltrar, btnVerTodo;
    private JLabel lblTotalReservas, lblSeleccionados;
    private Runnable onSeleccionChanged;

    public ReservaConsumidorView() {
        setTitle("SGCU - GESTIÓN DE MIS RESERVAS");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ── ENCABEZADO ───────────────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(41, 41, 41));
        header.setPreferredSize(new Dimension(0, 80));
        header.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel title = new JLabel("MIS RESERVAS ACTIVAS");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));

        // Botón con el mismo estilo y tamaño que el del Menú
        btnDashboard = crearBoton("Volver al Panel", new Color(108, 117, 125), new Color(90, 100, 110));
        btnDashboard.setPreferredSize(new Dimension(150, 35));
        
        JPanel panelNav = new JPanel(new GridBagLayout());
        panelNav.setOpaque(false);
        panelNav.add(btnDashboard);

        header.add(title, BorderLayout.WEST);
        header.add(panelNav, BorderLayout.EAST);

        // ── BARRA DE BÚSQUEDA ────────────────────────────────────────────────
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        toolbar.setBackground(Color.WHITE);
        toolbar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)));
        
        txtBuscar = new JTextField(25);
        txtBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        btnFiltrar = crearBoton("Buscar Plato", new Color(0, 123, 255), new Color(0, 105, 217));
        btnVerTodo = crearBoton("Ver Todo", new Color(23, 162, 184), new Color(19, 132, 150));

        toolbar.add(new JLabel("Filtrar:"));
        toolbar.add(txtBuscar);
        toolbar.add(btnFiltrar);
        toolbar.add(btnVerTodo);

        // ── CUERPO (Tabla) ───────────────────────────────────────────────────
        tablaReservas = new JTable();
        JScrollPane scroll = new JScrollPane(tablaReservas);
        scroll.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        scroll.getViewport().setBackground(new Color(245, 245, 245));

        // ── PIE DE PÁGINA ────────────────────────────────────────────────────
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBorder(new EmptyBorder(15, 20, 20, 20));
        footer.setBackground(new Color(245, 245, 245));

        JPanel panelInfo = new JPanel(new GridLayout(2, 1, 0, 5));
        panelInfo.setOpaque(false);
        
        lblTotalReservas = new JLabel("Total de reservas en sistema: 0");
        lblTotalReservas.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        lblSeleccionados = new JLabel("Platos seleccionados para pago: 0");
        lblSeleccionados.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblSeleccionados.setForeground(new Color(41, 41, 41));
        
        panelInfo.add(lblTotalReservas);
        panelInfo.add(lblSeleccionados);

        btnPagar = crearBoton("PROCEDER AL PAGO SEGURO", new Color(40, 167, 69), new Color(33, 136, 56));
        btnPagar.setPreferredSize(new Dimension(280, 45));

        footer.add(panelInfo, BorderLayout.WEST);
        footer.add(btnPagar, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);
        JPanel center = new JPanel(new BorderLayout());
        center.add(toolbar, BorderLayout.NORTH);
        center.add(scroll, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);
    }

    public void setTablaModelo(DefaultTableModel modelo) {
        tablaReservas.setModel(modelo);
        estilizarTabla();
        
        // Aplicamos el Renderer y Editor de botones a la columna "Seleccionar" (índice 3)
        if (tablaReservas.getColumnCount() > 3) {
            tablaReservas.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());
            tablaReservas.getColumnModel().getColumn(3).setCellEditor(new ButtonEditor(new JCheckBox()));
        }
    }

    private void estilizarTabla() {
        tablaReservas.setRowHeight(45);
        tablaReservas.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        tablaReservas.setShowVerticalLines(false);
        tablaReservas.setGridColor(new Color(230, 230, 230));

        JTableHeader header = tablaReservas.getTableHeader();
        header.setReorderingAllowed(false); // BLOQUEO DE MOVIMIENTO DE COLUMNAS
        header.setBackground(new Color(41, 41, 41));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 15));

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        // Centrar columnas de datos, excepto la de botones
        for (int i = 0; i < 3; i++) {
            tablaReservas.getColumnModel().getColumn(i).setCellRenderer(center);
        }
        
        if (tablaReservas.getColumnCount() > 3) {
            tablaReservas.getColumnModel().getColumn(3).setPreferredWidth(180);
        }
    }

    private JButton crearBoton(String texto, Color c1, Color c2) {
        JButton b = new JButton(texto) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? c2 : c1);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    // --- MÉTODOS PARA EL CONTROLADOR ---
    public DefaultTableModel getModeloTabla() { return (DefaultTableModel) tablaReservas.getModel(); }
    public String getTextoBusqueda() { return txtBuscar.getText().trim(); }
    public void setOnSeleccionChanged(Runnable r) { this.onSeleccionChanged = r; }
    public void actualizarConteo(int total, int seleccionados) {
        lblTotalReservas.setText("Total de reservas en sistema: " + total);
        lblSeleccionados.setText("Platos seleccionados para pago: " + seleccionados);
        btnPagar.setEnabled(seleccionados > 0);
    }
    
    public void addFiltrarListener(ActionListener l) { btnFiltrar.addActionListener(l); }
    public void addVerTodoListener(ActionListener l) { btnVerTodo.addActionListener(l); }
    public void addPagarListener(ActionListener l) { btnPagar.addActionListener(l); }
    public void addDashboardListener(ActionListener l) { btnDashboard.addActionListener(l); }

    // ─── CLASES INTERNAS PARA EL BOTÓN DE PAGO ───────────────────────────

    private class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setFocusPainted(false);
            setFont(new Font("Segoe UI", Font.BOLD, 12));
        }
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            boolean enPago = (value != null && (Boolean) value);
            if (enPago) {
                setText("Quitar del Pago");
                setBackground(new Color(220, 53, 69)); // Rojo
                setForeground(Color.WHITE);
            } else {
                setText("Seleccionar para Pago");
                setBackground(new Color(0, 123, 255)); // Azul
                setForeground(Color.WHITE);
            }
            return this;
        }
    }

    private class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private boolean enPago;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.setFocusPainted(false);
            button.setFont(new Font("Segoe UI", Font.BOLD, 12));
            button.addActionListener(e -> fireEditingStopped());
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            enPago = (value != null && (Boolean) value);
            if (enPago) {
                button.setText("Quitar del Pago");
                button.setBackground(new Color(220, 53, 69));
                button.setForeground(Color.WHITE);
            } else {
                button.setText("Seleccionar para Pago");
                button.setBackground(new Color(0, 123, 255));
                button.setForeground(Color.WHITE);
            }
            return button;
        }

        public Object getCellEditorValue() {
            enPago = !enPago; 
            SwingUtilities.invokeLater(() -> {
                if (onSeleccionChanged != null) onSeleccionChanged.run(); 
            });
            return enPago;
        }
    }
}