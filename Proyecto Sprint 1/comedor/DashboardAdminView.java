package comedor;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class DashboardAdminView extends JFrame {
    
    private JLabel lblBienvenida;
    private JButton btnMenu;
    private JButton btnCostos;
    private JButton btnLogout;

    public DashboardAdminView() {
        setTitle("SGCU - Panel de Administración");
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
        header.setBackground(new Color(41, 41, 41)); // Gris oscuro
        header.setPreferredSize(new Dimension(0, 80));
        header.setBorder(new EmptyBorder(0, 25, 0, 25));
        
        JLabel titulo = new JLabel("PANEL DE CONTROL");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        // --- BOTÓN CERRAR SESIÓN (Centrado) ---
        btnLogout = crearBotonEstilizado("Cerrar Sesión", new Color(220, 53, 69)); // Rojo
        btnLogout.setPreferredSize(new Dimension(140, 35));
        
        JPanel panelLogout = new JPanel(new GridBagLayout()); // GridBag centra verticalmente
        panelLogout.setOpaque(false);
        panelLogout.add(btnLogout);
        
        header.add(titulo, BorderLayout.WEST);
        header.add(panelLogout, BorderLayout.EAST);
        
        // --- 2. BARRA LATERAL (Sidebar) ---
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(51, 51, 51)); 
        sidebar.setPreferredSize(new Dimension(260, 0));
        sidebar.setBorder(new EmptyBorder(30, 15, 30, 15));

        JLabel lblMenu = new JLabel("NAVEGACIÓN");
        lblMenu.setForeground(Color.LIGHT_GRAY);
        lblMenu.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Botones del Sidebar
        btnMenu = crearBotonSidebar("Gestión de Menú");
        btnCostos = crearBotonSidebar("Cálculo de CCB");

        sidebar.add(lblMenu);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20))); 
        sidebar.add(btnMenu);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15))); 
        sidebar.add(btnCostos);
        sidebar.add(Box.createVerticalGlue()); 

        // --- 3. CONTENIDO CENTRAL ---
        JPanel panelContenido = new JPanel(new GridBagLayout());
        panelContenido.setBackground(new Color(230, 230, 230)); 
        
        lblBienvenida = new JLabel("Bienvenido al Sistema");
        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblBienvenida.setForeground(new Color(60, 60, 60));
        
        JLabel lblSubtitulo = new JLabel("Seleccione una opción del menú lateral para comenzar.");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblSubtitulo.setForeground(new Color(100, 100, 100));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 20, 0);
        panelContenido.add(lblBienvenida, gbc);
        
        gbc.gridy = 1;
        panelContenido.add(lblSubtitulo, gbc);
        
        // Armado final
        add(header, BorderLayout.NORTH);
        add(sidebar, BorderLayout.WEST);
        add(panelContenido, BorderLayout.CENTER);
    }

    // --- MÉTODOS PÚBLICOS ---
    public void setNombreUsuario(String nombre) {
        lblBienvenida.setText("Hola, " + nombre);
    }

    public void addMenuListener(ActionListener l) { btnMenu.addActionListener(l); }
    public void addCostosListener(ActionListener l) { btnCostos.addActionListener(l); }
    public void addLogoutListener(ActionListener l) { btnLogout.addActionListener(l); }

    // --- ESTILOS VISUALES ---

    private JButton crearBotonEstilizado(String texto, Color bg) {
        JButton btn = new JButton(texto) {
            @Override
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
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton crearBotonSidebar(String texto) {
        JButton btn = new JButton(texto) {
            @Override
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
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(230, 50)); 
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        return btn;
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> {
            // Iniciamos a través del CONTROLADOR para que la lógica funcione
            new DashboardAdminController("Administrador");
        });
    }
}