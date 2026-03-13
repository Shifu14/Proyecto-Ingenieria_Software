package sgcu.vistas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Locale;

public class DashboardConsumidorView extends JFrame {
    
    private JLabel lblBienvenida, lblSaldoValor; 
    private JButton btnMenu, btnSaldo, btnSaldoPana, btnReservas, btnHistorial, btnLogout;

    public DashboardConsumidorView() {
        setTitle("SGCU - Dashboard Consumidor");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        setLayout(new BorderLayout());

        // ENCABEZADO
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(41, 41, 41)); 
        header.setPreferredSize(new Dimension(0, 80));
        header.setBorder(new EmptyBorder(0, 25, 0, 25));
        
        JLabel titulo = new JLabel("COMEDOR UCV - CONSUMIDOR");
        titulo.setForeground(Color.WHITE); 
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        
        btnLogout = crearBotonEstilizado("Cerrar Sesión", new Color(220, 53, 69));
        btnLogout.setPreferredSize(new Dimension(140, 35));
        
        JPanel panelLogout = new JPanel(new GridBagLayout());
        panelLogout.setOpaque(false);
        panelLogout.add(btnLogout);
        
        header.add(titulo, BorderLayout.WEST); 
        header.add(panelLogout, BorderLayout.EAST);
        
        // BARRA LATERAL
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(51, 51, 51)); 
        sidebar.setPreferredSize(new Dimension(260, 0));
        sidebar.setBorder(new EmptyBorder(30, 15, 30, 15));
        
        btnMenu = crearBotonSidebar("Ver Menú del Día");
        btnSaldo = crearBotonSidebar("Mi Monedero"); 
        btnSaldoPana = crearBotonSidebar("Saldo Pana"); 
        btnReservas = crearBotonSidebar("Mis Reservas");
        btnHistorial = crearBotonSidebar("Historial");

        sidebar.add(btnMenu); sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
        sidebar.add(btnSaldo); sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
        sidebar.add(btnSaldoPana); sidebar.add(Box.createRigidArea(new Dimension(0, 15))); 
        sidebar.add(btnReservas); sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
        sidebar.add(btnHistorial); sidebar.add(Box.createVerticalGlue());

        // CONTENIDO CENTRAL
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBackground(new Color(230, 230, 230));
        panelCentral.setBorder(new EmptyBorder(40, 40, 40, 40));
        
        JPanel contentBox = new JPanel();
        contentBox.setLayout(new BoxLayout(contentBox, BoxLayout.Y_AXIS));
        contentBox.setOpaque(false);
        
        lblBienvenida = new JLabel("¡Bienvenido!");
        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblBienvenida.setAlignmentX(Component.CENTER_ALIGNMENT); 
        
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setOpaque(false); 
        statsPanel.setMaximumSize(new Dimension(2000, 150));
        statsPanel.add(crearTarjeta("Saldo Disponible", "Cargando...", new Color(46, 204, 113), true)); 
        statsPanel.add(crearTarjeta("Menú Hoy", "Pabellón", new Color(52, 152, 219), false));
        statsPanel.add(crearTarjeta("Próx. Reserva", "12:30 PM", new Color(155, 89, 182), false));

        contentBox.add(Box.createVerticalStrut(20));
        contentBox.add(lblBienvenida); 
        contentBox.add(Box.createVerticalStrut(50)); // Espacio directo hacia las tarjetas
        contentBox.add(statsPanel);
        
        panelCentral.add(contentBox, BorderLayout.NORTH);
        add(header, BorderLayout.NORTH); 
        add(sidebar, BorderLayout.WEST); 
        add(panelCentral, BorderLayout.CENTER);
    }

    public void setNombreUsuario(String nombre) { lblBienvenida.setText("¡Hola, " + nombre + "!"); }
    public void actualizarSaldoDisplay(double s) { if (lblSaldoValor != null) lblSaldoValor.setText(String.format(Locale.US, "%.2f Bs", s)); }

    public void addMenuListener(ActionListener l) { btnMenu.addActionListener(l); }
    public void addSaldoListener(ActionListener l) { btnSaldo.addActionListener(l); }
    public void addSaldoPanaListener(ActionListener l) { btnSaldoPana.addActionListener(l); }
    public void addLogoutListener(ActionListener l) { btnLogout.addActionListener(l); }
    public void addReservasListener(ActionListener l) { btnReservas.addActionListener(l); }
    public void addHistorialListener(ActionListener l) { btnHistorial.addActionListener(l); }

    private JPanel crearTarjeta(String t, String v, Color c, boolean esSaldo) {
        JPanel card = new JPanel(new BorderLayout()); card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createMatteBorder(0, 5, 0, 0, c));
        JLabel lblT = new JLabel(t); lblT.setForeground(Color.GRAY);
        JLabel lblV = new JLabel(v); lblV.setFont(new Font("Segoe UI", Font.BOLD, 24));
        if (esSaldo) this.lblSaldoValor = lblV;
        JPanel p = new JPanel(new GridLayout(2, 1)); p.setOpaque(false); p.setBorder(new EmptyBorder(15, 20, 15, 10));
        p.add(lblT); p.add(lblV); card.add(p, BorderLayout.CENTER); return card;
    }

    private JButton crearBotonSidebar(String txt) {
        JButton btn = new JButton(txt);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14)); btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false); btn.setBorderPainted(false);
        btn.setOpaque(true); btn.setBackground(new Color(80, 80, 80)); // Siempre gris (activo)
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(230, 50));
        return btn;
    }

    private JButton crearBotonEstilizado(String txt, Color bg) {
        JButton btn = new JButton(txt) {
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
}