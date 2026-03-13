package sgcu.vistas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Locale;

public class ReservaAdminView extends JFrame {

    private JTable tabla;
    private JTextField txtBuscar;
    private JButton btnBuscar, btnRefrescar, btnDashboard;
    private JLabel lblTotalCant, lblTotalMonto;

    public ReservaAdminView() {
        setTitle("ADMINISTRACIÓN DE RESERVAS - SGCU");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // HEADER
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(41, 41, 41));
        header.setPreferredSize(new Dimension(0, 70));
        header.setBorder(new EmptyBorder(0, 20, 0, 20));
        
        JLabel title = new JLabel("PANEL DE RESERVAS GLOBALES");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        
        btnDashboard = new JButton("Volver al Dashboard");
        header.add(title, BorderLayout.WEST);
        header.add(btnDashboard, BorderLayout.EAST);

        // TOOLBAR (BUSCADOR)
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        txtBuscar = new JTextField(20);
        btnBuscar = new JButton("Buscar Usuario");
        btnRefrescar = new JButton("Refrescar");
        toolbar.add(new JLabel("Usuario:"));
        toolbar.add(txtBuscar);
        toolbar.add(btnBuscar);
        toolbar.add(btnRefrescar);

        // TABLA
        tabla = new JTable();
        JScrollPane scroll = new JScrollPane(tabla);

        // FOOTER (TOTALES)
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 10));
        lblTotalCant = new JLabel("Reservas: 0");
        lblTotalMonto = new JLabel("Total: 0.00 Bs");
        lblTotalMonto.setFont(new Font("Segoe UI", Font.BOLD, 14));
        footer.add(lblTotalCant);
        footer.add(lblTotalMonto);

        add(header, BorderLayout.NORTH);
        add(toolbar, BorderLayout.CENTER); // Contiene el buscador y la tabla
        
        JPanel pnlCentro = new JPanel(new BorderLayout());
        pnlCentro.add(toolbar, BorderLayout.NORTH);
        pnlCentro.add(scroll, BorderLayout.CENTER);
        add(pnlCentro, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);
    }

    // MÉTODOS CLAVE PARA EL CONTROLADOR
    public void setTablaModelo(DefaultTableModel modelo) { 
        tabla.setModel(modelo); 
    }

    public String getTextoBusqueda() { 
        return txtBuscar.getText(); 
    }

    public void actualizarTotal(int cantidad, double monto) {
        lblTotalCant.setText("Reservas Totales: " + cantidad);
        lblTotalMonto.setText("Monto Total CCB: " + String.format(Locale.US, "%.2f", monto) + " Bs");
    }

    // LISTENERS (Nombres que el controlador necesita)
    public void addBuscarListener(ActionListener l) { btnBuscar.addActionListener(l); }
    public void addRefrescarListener(ActionListener l) { btnRefrescar.addActionListener(l); }
    public void addDashboardListener(ActionListener l) { btnDashboard.addActionListener(l); }
}