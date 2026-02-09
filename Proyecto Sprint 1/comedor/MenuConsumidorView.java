package comedor;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionListener;

public class MenuConsumidorView extends JFrame {

    private JTable tabla;
    private JTextField txtBuscar;
    private JButton btnMonedero;
    private JButton btnFiltrar;
    private JButton btnActualizar; 
    private JButton btnDashboard; // Nuevo botón

    public MenuConsumidorView() {
        setTitle("MENÚ DEL DÍA");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        try {
            Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("media/icon.png"));
            if(icon != null) setIconImage(icon);
        } catch (Exception e) {}

        // --- ENCABEZADO ---
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(41, 41, 41)); 
        header.setPreferredSize(new Dimension(0, 80)); // Altura ajustada a 80px (estándar Dashboard)
        header.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel title = new JLabel("MENÚ SEMANAL");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));

        // --- PANEL DE BOTONES (Centrado Verticalmente) ---
        JPanel panelNav = new JPanel(new GridBagLayout());
        panelNav.setOpaque(false);
        
        // FlowLayout interno para pegar los botones
        JPanel flowBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        flowBotones.setOpaque(false);

        btnActualizar = crearBotonConEstilo("Actualizar", new Color(0, 75, 237), new Color(0, 60, 200));
        btnActualizar.setPreferredSize(new Dimension(120, 35));

        btnMonedero = crearBotonConEstilo("Billetera", new Color(60, 60, 60), new Color(80, 80, 80));
        btnMonedero.setPreferredSize(new Dimension(120, 35));

        btnDashboard = crearBotonConEstilo("Volver al Panel", new Color(255, 119, 0), new Color(230, 100, 0)); // Naranja UCV
        btnDashboard.setPreferredSize(new Dimension(140, 35));
        
        flowBotones.add(btnActualizar);
        flowBotones.add(btnMonedero);
        flowBotones.add(btnDashboard); // Añadido
        
        panelNav.add(flowBotones);
        
        header.add(title, BorderLayout.WEST);
        header.add(panelNav, BorderLayout.EAST);   

        // --- BARRA DE BUSQUEDA ---
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolbar.setBackground(new Color(230, 230, 230));
        toolbar.setBorder(new EmptyBorder(10, 20, 10, 20));
        
        JLabel lblBuscar = new JLabel("Buscar plato: ");
        lblBuscar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        txtBuscar = new JTextField(20);
        txtBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        btnFiltrar = crearBotonConEstilo("Buscar", new Color(100, 100, 100), new Color(80, 80, 80));
        btnFiltrar.setPreferredSize(new Dimension(100, 30)); // Ajuste de tamaño

        toolbar.add(lblBuscar);
        toolbar.add(txtBuscar);
        toolbar.add(btnFiltrar);

        // --- TABLA ---
        tabla = new JTable();
        estilizarTabla(tabla);

        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        // --- FOOTER ---
        JPanel footer = new JPanel();
        footer.setBackground(new Color(41, 41, 41));
        footer.setBorder(new EmptyBorder(5, 0, 5, 0));
        JLabel info = new JLabel("Equipo 9 Ingeniería de Software - Facultad de Ciencias");
        info.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        info.setForeground(new Color(200, 200, 200));
        footer.add(info);

        // --- LAYOUT ---
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(toolbar, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);
    }

    // --- MÉTODOS PÚBLICOS ---

    public void setTablaModelo(DefaultTableModel modelo) {
        tabla.setModel(modelo);
        estilizarTabla(tabla);
    }

    public String getTextoBusqueda() { return txtBuscar.getText(); }

    public void addMonederoListener(ActionListener listener) { btnMonedero.addActionListener(listener); }
    public void addFiltrarListener(ActionListener listener) { btnFiltrar.addActionListener(listener); }
    public void addActualizarListener(ActionListener listener) { btnActualizar.addActionListener(listener); }
    public void addDashboardListener(ActionListener listener) { btnDashboard.addActionListener(listener); } // Nuevo Listener

    private void estilizarTabla(JTable t) {
        t.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        t.setRowHeight(40);
        t.setSelectionBackground(new Color(0, 75, 237));
        t.setSelectionForeground(Color.WHITE);
        t.setShowVerticalLines(false);
        t.setGridColor(new Color(230, 230, 230));
        
        JTableHeader th = t.getTableHeader();
        th.setFont(new Font("Segoe UI", Font.BOLD, 16));
        th.setBackground(new Color(245, 245, 245));
        th.setForeground(Color.BLACK);
        
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        if(t.getColumnCount() > 0) {
            for (int i = 0; i < t.getColumnCount(); i++) t.getColumnModel().getColumn(i).setCellRenderer(center);
        }
    }

    private JButton crearBotonConEstilo(String texto, Color colorNormal, Color colorHover) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) g2.setColor(colorHover.darker());
                else if (getModel().isRollover()) g2.setColor(colorHover);
                else g2.setColor(colorNormal);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10); // Bordes redondeados
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}