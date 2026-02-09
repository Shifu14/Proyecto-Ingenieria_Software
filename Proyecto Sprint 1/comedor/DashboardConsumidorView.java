package comedor;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Locale;

public class DashboardConsumidorView extends JFrame {
    
    private JLabel lblBienvenida;
    // Referencia global para poder actualizar el texto después
    private JLabel lblSaldoValor; 
    
    private JButton btnMenu, btnSaldo, btnReservas, btnHistorial, btnLogout;

    public DashboardConsumidorView() {
        setTitle("SGCU - Dashboard Consumidor");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        try {
            Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("media/icon.png"));
            if(icon != null) setIconImage(icon);
        } catch (Exception e) {}
        
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        setLayout(new BorderLayout());

        // --- 1. ENCABEZADO ---
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
        
        // --- 2. BARRA LATERAL ---
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(51, 51, 51)); 
        sidebar.setPreferredSize(new Dimension(260, 0));
        sidebar.setBorder(new EmptyBorder(30, 15, 30, 15));

        JLabel lblNav = new JLabel("NAVEGACIÓN");
        lblNav.setForeground(Color.LIGHT_GRAY);
        lblNav.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblNav.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        btnMenu = crearBotonSidebar("Ver Menú del Día");
        btnSaldo = crearBotonSidebar("Mi Monedero");
        btnReservas = crearBotonSidebar("Mis Reservas");
        btnHistorial = crearBotonSidebar("Historial");

        sidebar.add(lblNav);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));
        sidebar.add(btnMenu);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
        sidebar.add(btnSaldo);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
        sidebar.add(btnReservas);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
        sidebar.add(btnHistorial);
        sidebar.add(Box.createVerticalGlue());

        // --- 3. CONTENIDO CENTRAL ---
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBackground(new Color(230, 230, 230));
        panelCentral.setBorder(new EmptyBorder(40, 40, 40, 40));
        
        JPanel contentBox = new JPanel();
        contentBox.setLayout(new BoxLayout(contentBox, BoxLayout.Y_AXIS));
        contentBox.setOpaque(false);
        
        lblBienvenida = new JLabel("¡Bienvenido!");
        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblBienvenida.setForeground(new Color(60, 60, 60));
        lblBienvenida.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblSub = new JLabel("Resumen de tu cuenta hoy:");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblSub.setForeground(new Color(100, 100, 100));
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Tarjetas de Resumen
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setOpaque(false);
        statsPanel.setMaximumSize(new Dimension(2000, 150));
        
        // Creamos las tarjetas. Nota: El valor inicial de saldo se sobrescribirá por el controlador
        statsPanel.add(crearTarjeta("Saldo Disponible", "Cargando...", new Color(46, 204, 113), true)); 
        statsPanel.add(crearTarjeta("Menú Hoy", "Pabellón", new Color(52, 152, 219), false));
        statsPanel.add(crearTarjeta("Próx. Reserva", "12:30 PM", new Color(155, 89, 182), false));

        contentBox.add(lblBienvenida);
        contentBox.add(lblSub);
        contentBox.add(Box.createVerticalStrut(30));
        contentBox.add(statsPanel);
        contentBox.add(Box.createVerticalGlue());

        panelCentral.add(contentBox, BorderLayout.NORTH);

        add(header, BorderLayout.NORTH);
        add(sidebar, BorderLayout.WEST);
        add(panelCentral, BorderLayout.CENTER);
    }

    // --- MÉTODOS PÚBLICOS ---
    public void setNombreUsuario(String nombre) {
        lblBienvenida.setText("¡Hola, " + nombre + "!");
    }

    // Nuevo método para actualizar el saldo desde el controlador
    public void actualizarSaldoDisplay(double saldo) {
        if (lblSaldoValor != null) {
            lblSaldoValor.setText(String.format(Locale.US, "%.2f Bs", saldo));
        }
    }

    public void addMenuListener(ActionListener l) { btnMenu.addActionListener(l); }
    public void addSaldoListener(ActionListener l) { btnSaldo.addActionListener(l); }
    public void addLogoutListener(ActionListener l) { btnLogout.addActionListener(l); }

    // --- COMPONENTES VISUALES ---

    // Modificado para detectar si es la tarjeta de saldo
    private JPanel crearTarjeta(String titulo, String valor, Color colorBorde, boolean esTarjetaSaldo) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createMatteBorder(0, 5, 0, 0, colorBorde));
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTitulo.setForeground(Color.GRAY);
        
        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblValor.setForeground(new Color(60, 60, 60));
        
        // Si es la tarjeta de saldo, guardamos la referencia en la variable global
        if (esTarjetaSaldo) {
            this.lblSaldoValor = lblValor;
        }
        
        JPanel p = new JPanel(new GridLayout(2, 1));
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(15, 20, 15, 10));
        p.add(lblTitulo);
        p.add(lblValor);
        
        card.add(p, BorderLayout.CENTER);
        return card;
    }

    private JButton crearBotonSidebar(String texto) {
        JButton btn = new JButton(texto) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) g2.setColor(new Color(0, 50, 180));
                else if (getModel().isRollover()) g2.setColor(new Color(0, 75, 237));
                else g2.setColor(new Color(80, 80, 80));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(230, 50));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        return btn;
    }

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
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new DashboardConsumidorController("Estudiante"));
    }
}