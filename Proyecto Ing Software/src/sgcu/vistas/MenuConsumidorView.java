package sgcu.vistas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionListener;

public class MenuConsumidorView extends JFrame {

    private JTable tabla;
    private JTextField txtBuscar;
    private JButton btnMonedero, btnFiltrar, btnActualizar, btnDashboard, btnReservar;
    private JLabel lblInfoCarrito; 
    private Runnable onCarritoChanged; 

    public MenuConsumidorView() {
        setTitle("MENÚ DEL DÍA - SGCU");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ── ENCABEZADO ───────────────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(41, 41, 41));
        header.setPreferredSize(new Dimension(0, 80));
        header.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel title = new JLabel("MENÚ SEMANAL");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));

        JPanel panelNav = new JPanel(new GridBagLayout());
        panelNav.setOpaque(false);
        JPanel flowBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        flowBotones.setOpaque(false);

        btnDashboard = crearBoton("Volver al Panel", new Color(108, 117, 125), new Color(90, 100, 110));
        btnMonedero = crearBoton("Mi Billetera", new Color(40, 167, 69), new Color(33, 136, 56));
        
        flowBotones.add(btnMonedero);
        flowBotones.add(btnDashboard);
        panelNav.add(flowBotones);

        header.add(title, BorderLayout.WEST);
        header.add(panelNav, BorderLayout.EAST);

        // ── BARRA DE BÚSQUEDA ────────────────────────────────────────────────
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        toolbar.setBackground(Color.WHITE);
        toolbar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)));

        txtBuscar = new JTextField(25);
        txtBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnFiltrar = crearBoton("Buscar Plato", new Color(0, 123, 255), new Color(0, 105, 217));
        btnActualizar = crearBoton("Ver Todo", new Color(23, 162, 184), new Color(19, 132, 150));

        toolbar.add(new JLabel("Buscar:"));
        toolbar.add(txtBuscar);
        toolbar.add(btnFiltrar);
        toolbar.add(btnActualizar);

        // ── TABLA DE MENÚ ────────────────────────────────────────────────────
        tabla = new JTable();
        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        scrollPane.getViewport().setBackground(new Color(245, 245, 245));

        // ── SUR: PANEL DE CARRITO Y RESERVA ──────────────────────────────────
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBorder(new EmptyBorder(15, 20, 20, 20));
        
        lblInfoCarrito = new JLabel("Platos seleccionados: 0");
        lblInfoCarrito.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblInfoCarrito.setForeground(new Color(41, 41, 41));
        
        btnReservar = crearBoton("RESERVAR SELECCIÓN", new Color(0, 75, 237), new Color(0, 50, 200));
        btnReservar.setPreferredSize(new Dimension(240, 45));

        footer.add(lblInfoCarrito, BorderLayout.WEST);
        footer.add(btnReservar, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(toolbar, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
        
        add(footer, BorderLayout.SOUTH);
    }

    // --- MÉTODOS DE COMUNICACIÓN CON EL CONTROLADOR ---

    public void setTablaModelo(DefaultTableModel modelo) {
        tabla.setModel(modelo);
        estilizarTabla(tabla);
        
        // Configurar la columna 5 ("Acción") como botón interactivo
        if (tabla.getColumnCount() > 5) {
            tabla.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
            tabla.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox()));
        }
    }

    public DefaultTableModel getModeloTabla() {
        return (DefaultTableModel) tabla.getModel();
    }

    public void actualizarTextos(int enCarrito) {
        lblInfoCarrito.setText("Platos seleccionados: " + enCarrito);
    }

    public String getValorColumna(int fila, int col) {
        return tabla.getValueAt(fila, col).toString();
    }

    public String getTextoBusqueda() { return txtBuscar.getText(); }

    public void setOnCarritoChanged(Runnable r) { this.onCarritoChanged = r; }

    public void addFiltrarListener(ActionListener l)    { btnFiltrar.addActionListener(l); }
    public void addActualizarListener(ActionListener l) { btnActualizar.addActionListener(l); }
    public void addReservarListener(ActionListener l)   { btnReservar.addActionListener(l); }
    public void addDashboardListener(ActionListener l)  { btnDashboard.addActionListener(l); }
    public void addBilleteraListener(ActionListener l)  { btnMonedero.addActionListener(l); }

    // --- ESTILIZACIÓN ---

    private void estilizarTabla(JTable t) {
        t.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        t.setRowHeight(45);
        t.setBackground(Color.WHITE);
        t.setShowVerticalLines(false);
        t.setGridColor(new Color(230, 230, 230));
        
        JTableHeader th = t.getTableHeader();
        th.setFont(new Font("Segoe UI", Font.BOLD, 15));
        th.setBackground(new Color(41, 41, 41));
        th.setForeground(Color.WHITE);
        th.setReorderingAllowed(false);

        if (t.getColumnCount() > 5) {
            t.getColumnModel().getColumn(5).setPreferredWidth(160);
        }

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < t.getColumnCount(); i++) {
            if (i != 5) t.getColumnModel().getColumn(i).setCellRenderer(center);
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

    // ─── CLASES INTERNAS PARA EL BOTÓN DE LA TABLA ───────────────────────

    private class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setFocusPainted(false);
            setFont(new Font("Segoe UI", Font.BOLD, 12));
        }
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            boolean enCarrito = (value != null && (Boolean) value);
            if (enCarrito) {
                setText("Eliminar Reserva");
                setBackground(new Color(220, 53, 69)); 
                setForeground(Color.WHITE);
            } else {
                setText("Agregar Reserva");
                setBackground(new Color(0, 123, 255)); 
                setForeground(Color.WHITE);
            }
            return this;
        }
    }

    private class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private boolean enCarrito;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.setFocusPainted(false);
            button.setFont(new Font("Segoe UI", Font.BOLD, 12));
            button.addActionListener(e -> fireEditingStopped());
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            enCarrito = (value != null && (Boolean) value);
            if (enCarrito) {
                button.setText("Eliminar Reserva");
                button.setBackground(new Color(220, 53, 69));
                button.setForeground(Color.WHITE);
            } else {
                button.setText("Agregar Reserva");
                button.setBackground(new Color(0, 123, 255));
                button.setForeground(Color.WHITE);
            }
            return button;
        }

        public Object getCellEditorValue() {
            enCarrito = !enCarrito; 
            SwingUtilities.invokeLater(() -> {
                if (onCarritoChanged != null) onCarritoChanged.run(); 
            });
            return enCarrito;
        }
    }
}