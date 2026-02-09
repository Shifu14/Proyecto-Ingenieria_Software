package comedor;
import javax.swing.*;
import javax.swing.SwingUtilities;
import java.awt.*;
import java.awt.event.ActionListener;

public class PanelView extends JFrame {

    private JButton btnConsumidor;
    private JButton btnAdmin;
    private JLabel lblTitulo;
    private JLabel lblSubtitulo;

    public PanelView() {
        setTitle("SGCU - BIENVENIDO");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("media/icon.png"));
            if (icon != null) setIconImage(icon);
        } catch (Exception e) {}

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(41, 41, 41)); 

        JPanel cardPanel = new JPanel(new GridBagLayout());
        cardPanel.setBackground(new Color(230, 230, 230));
        cardPanel.setPreferredSize(new Dimension(600, 400));
        cardPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        lblTitulo = new JLabel("SISTEMA DE GESTIÓN DE COMEDOR");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(new Color(41, 41, 41));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

        lblSubtitulo = new JLabel("Seleccione su tipo de perfil para ingresar");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblSubtitulo.setForeground(Color.GRAY);
        lblSubtitulo.setHorizontalAlignment(SwingConstants.CENTER);

        // Botones con color forzado
        btnConsumidor = crearBotonGrande("👤  PERFIL CONSUMIDOR", "Estudiantes, Profesores y Personal", new Color(0, 75, 237));
        btnAdmin = crearBotonGrande("🛠️  PERFIL ADMINISTRATIVO", "Gestión de Menú y Costos", new Color(180, 0, 0));

        gbc.gridy = 0; 
        cardPanel.add(lblTitulo, gbc);
        
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 10, 30, 10);
        cardPanel.add(lblSubtitulo, gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(10, 40, 10, 40);
        cardPanel.add(btnConsumidor, gbc);

        gbc.gridy = 3;
        cardPanel.add(btnAdmin, gbc);
        
        JLabel lblFooter = new JLabel("SGCU v1.0 - Facultad de Ciencias");
        lblFooter.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblFooter.setForeground(Color.GRAY);
        lblFooter.setHorizontalAlignment(SwingConstants.CENTER);
        
        gbc.gridy = 4;
        gbc.insets = new Insets(40, 10, 10, 10);
        cardPanel.add(lblFooter, gbc);

        mainPanel.add(cardPanel);
        add(mainPanel);
    }

    // --- CORRECCIÓN VISUAL: Pintado manual del botón ---
    private JButton crearBotonGrande(String titulo, String desc, Color colorBase) {
        JButton btn = new JButton("<html><center><span style='font-size:14px; font-weight:bold;'>" + titulo + "</span><br><span style='font-size:10px; font-weight:normal;'>" + desc + "</span></center></html>") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Color dinámico (Hover/Press)
                if (getModel().isPressed()) {
                    g2.setColor(colorBase.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(colorBase.brighter());
                } else {
                    g2.setColor(colorBase);
                }
                
                // Dibujar fondo sólido con bordes redondeados
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                
                super.paintComponent(g); // Pinta el texto encima
            }
        };
        
        btn.setPreferredSize(new Dimension(0, 70));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false); // Importante para que funcione el paintComponent
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        return btn;
    }

    public void addConsumidorListener(ActionListener l) { btnConsumidor.addActionListener(l); }
    public void addAdminListener(ActionListener l) { btnAdmin.addActionListener(l); }
    public void setTextos(String titulo, String sub) { lblTitulo.setText(titulo); lblSubtitulo.setText(sub); }

    // --- MAIN ---
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PanelController());
    }
}
