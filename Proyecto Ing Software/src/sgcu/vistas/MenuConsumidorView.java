package sgcu.vistas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionListener;

public class MenuConsumidorView extends JFrame {

    private JTable tabla;
    private JTextField txtBuscar;
    private JButton btnMonedero, btnFiltrar, btnActualizar, btnDashboard;

    public MenuConsumidorView() {
        setTitle("MENÚ DEL DÍA - SGCU");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ENCABEZADO
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(41, 41, 41)); 
        header.setPreferredSize(new Dimension(0, 80));
        header.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel title = new JLabel("MENÚ SEMANAL");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));

        JPanel panelNav = new JPanel(new GridBagLayout());
        panelNav.setOpaque(false);
        JPanel flowBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        flowBotones.setOpaque(false);

        btnActualizar = crearBotonConEstilo("Actualizar", new Color(0, 75, 237), new Color(0, 60, 200));
        btnActualizar.setPreferredSize(new Dimension(120, 35));
        btnMonedero = crearBotonConEstilo("Mi Monedero", new Color(60, 60, 60), new Color(80, 80, 80));
        btnMonedero.setPreferredSize(new Dimension(120, 35));
        btnDashboard = crearBotonConEstilo("Volver al Panel", new Color(255, 119, 0), new Color(230, 100, 0));
        btnDashboard.setPreferredSize(new Dimension(140, 35));
        
        flowBotones.add(btnActualizar); flowBotones.add(btnMonedero); flowBotones.add(btnDashboard);
        panelNav.add(flowBotones);
        header.add(title, BorderLayout.WEST);
        header.add(panelNav, BorderLayout.EAST); 

        // BARRA DE BÚSQUEDA
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolbar.setBackground(new Color(230, 230, 230));
        toolbar.setBorder(new EmptyBorder(10, 20, 10, 20));
        txtBuscar = new JTextField(20);
        btnFiltrar = crearBotonConEstilo("Buscar", new Color(100, 100, 100), new Color(80, 80, 80));
        btnFiltrar.setPreferredSize(new Dimension(100, 30));

        toolbar.add(new JLabel("Buscar plato: "));
        toolbar.add(txtBuscar); toolbar.add(btnFiltrar);

        // TABLA
        tabla = new JTable();
        estilizarTabla(tabla);
        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.getViewport().setBackground(Color.WHITE);

        // FOOTER
        JPanel footer = new JPanel();
        footer.setBackground(new Color(41, 41, 41));
        JLabel info = new JLabel("Equipo 9 Ingeniería de Software - Facultad de Ciencias");
        info.setForeground(new Color(200, 200, 200));
        footer.add(info);

        add(header, BorderLayout.NORTH);
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(toolbar, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);
    }

    public void setTablaModelo(DefaultTableModel m) {
        tabla.setModel(m);
        estilizarTabla(tabla);
    }

    public String getTextoBusqueda() { return txtBuscar.getText(); }
    public void addActualizarListener(ActionListener l) { btnActualizar.addActionListener(l); }
    public void addFiltrarListener(ActionListener l) { btnFiltrar.addActionListener(l); }
    public void addDashboardListener(ActionListener l) { btnDashboard.addActionListener(l); }
    public void addBilleteraListener(ActionListener l) { btnMonedero.addActionListener(l); }

    private void estilizarTabla(JTable t) {
        t.setFont(new Font("Segoe UI", Font.PLAIN, 16)); t.setRowHeight(40);
        JTableHeader th = t.getTableHeader();
        th.setFont(new Font("Segoe UI", Font.BOLD, 16));
        th.setReorderingAllowed(false); 
    }

    private JButton crearBotonConEstilo(String t, Color c1, Color c2) {
        JButton b = new JButton(t) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? c2 : c1);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose(); super.paintComponent(g);
            }
        };
        b.setFont(new Font("Segoe UI", Font.BOLD, 14)); b.setForeground(Color.WHITE);
        b.setBorderPainted(false); b.setContentAreaFilled(false); b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }
}