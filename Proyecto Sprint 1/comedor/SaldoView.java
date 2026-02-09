package comedor;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class SaldoView extends JFrame {

    private JLabel lblValor;
    private JTextField txtSuma;
    private JButton btnSumar;
    private JButton btnMenu;
    private JButton btnDashboard; // Nuevo botón

    public SaldoView() {
        setTitle("MONEDERO VIRTUAL DEL COMEDOR");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        try {
            Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("media/icon.png"));
            if (icon != null) setIconImage(icon);
        } catch (Exception e) {}

        // --- ENCABEZADO ---
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(41, 41, 41)); 
        header.setPreferredSize(new Dimension(0, 80));
        header.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel title = new JLabel("BILLETERA VIRTUAL");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));

        // --- PANEL DE BOTONES (Centrado) ---
        JPanel panelNav = new JPanel(new GridBagLayout());
        panelNav.setOpaque(false);
        
        JPanel flowBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        flowBotones.setOpaque(false);

        btnMenu = crearBotonConEstilo("Ver Menú", new Color(60, 60, 60), new Color(80, 80, 80));
        btnMenu.setPreferredSize(new Dimension(120, 35));

        btnDashboard = crearBotonConEstilo("Volver al Panel", new Color(255, 119, 0), new Color(230, 100, 0)); // Naranja UCV
        btnDashboard.setPreferredSize(new Dimension(140, 35));

        flowBotones.add(btnMenu);
        flowBotones.add(btnDashboard);
        
        panelNav.add(flowBotones);
        
        header.add(title, BorderLayout.WEST); 
        header.add(panelNav, BorderLayout.EAST);   

        // --- CONTENIDO ---
        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        lblValor = new JLabel("Cargando...");
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblValor.setForeground(new Color(0, 75, 237)); 
        lblValor.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblInstruccion = new JLabel("Ingrese el monto a recargar:");
        lblInstruccion.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblInstruccion.setHorizontalAlignment(SwingConstants.CENTER);

        txtSuma = new JTextField();
        txtSuma.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtSuma.setHorizontalAlignment(SwingConstants.CENTER);
        txtSuma.setPreferredSize(new Dimension(200, 40));
        
        btnSumar = crearBotonConEstilo("RECARGAR SALDO", new Color(0, 75, 237), new Color(0, 60, 200));
        btnSumar.setPreferredSize(new Dimension(200, 45));

        gbc.gridx = 0; gbc.gridy = 0;
        center.add(lblValor, gbc);
        gbc.gridy = 1; center.add(Box.createVerticalStrut(20), gbc); 
        gbc.gridy = 2; center.add(lblInstruccion, gbc);
        gbc.gridy = 3; center.add(txtSuma, gbc);
        gbc.gridy = 4; gbc.insets = new Insets(20, 10, 10, 10); center.add(btnSumar, gbc);

        // --- FOOTER ---
        JPanel footer = new JPanel();
        footer.setBackground(new Color(41, 41, 41));
        footer.setBorder(new EmptyBorder(5, 0, 5, 0));
        JLabel info = new JLabel("Equipo 9 Ingeniería de Software - Facultad de Ciencias");
        info.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        info.setForeground(new Color(200, 200, 200));
        footer.add(info);

        add(header, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);
    }

    // --- MÉTODOS PÚBLICOS ---

    public void actualizarEtiquetaSaldo(double valor) {
        lblValor.setText("Saldo Actual: " + String.format(java.util.Locale.US, "%.2f", valor) + " Bs.");
    }

    public String getMontoIngresado() { return txtSuma.getText(); }
    public void limpiarCampo() { txtSuma.setText(""); }

    public void addSumarListener(ActionListener listener) { btnSumar.addActionListener(listener); }
    public void addVolverMenuListener(ActionListener listener) { btnMenu.addActionListener(listener); }
    public void addDashboardListener(ActionListener listener) { btnDashboard.addActionListener(listener); } // Nuevo Listener

    public void mostrarMensaje(String mensaje) { JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE); }
    public void mostrarError(String mensaje) { JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE); }

    // --- ESTILO DE BOTONES ---
    private JButton crearBotonConEstilo(String texto, Color colorNormal, Color colorHover) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) g2.setColor(colorHover.darker());
                else if (getModel().isRollover()) g2.setColor(colorHover);
                else g2.setColor(colorNormal);
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
        btn.setOpaque(false);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}