package sgcu.vistas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MenuAdminView extends JFrame {

    private JTable tablaMenu;
    private DefaultTableModel modeloTabla;
    private JTextField txtId, txtNombre, txtDescripcion, txtPrecio;
    private JComboBox<String> comboDias, comboServicio;
    private JButton btnAgregar, btnEditar, btnEliminar, btnLimpiar, btnCostos, btnDashboard;

    public MenuAdminView() {
        setTitle("ADMINISTRACIÓN DE MENÚ - SGCU");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("media/icon.png"));
            if (icon != null) setIconImage(icon);
        } catch (Exception e) {}

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(41, 41, 41));
        header.setPreferredSize(new Dimension(0, 80));
        header.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel title = new JLabel("GESTIÓN DEL MENÚ");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));

        JPanel headerBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 20));
        headerBotones.setOpaque(false);
        btnCostos    = crearBotonEstilizado("CÁLCULO DE COSTOS", new Color(0, 123, 255));
        btnDashboard = crearBotonEstilizado("VOLVER AL PANEL",   new Color(108, 117, 125));
        headerBotones.add(btnCostos);
        headerBotones.add(btnDashboard);

        header.add(title, BorderLayout.WEST);
        header.add(headerBotones, BorderLayout.EAST);

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(new Color(240, 240, 240));
        panelFormulario.setPreferredSize(new Dimension(350, 0));
        panelFormulario.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(200, 200, 200)),
            new EmptyBorder(20, 20, 20, 20)
        ));

        txtId = new JTextField();
        txtId.setEditable(false);
        txtId.setBackground(new Color(220, 220, 220));

        txtNombre      = new JTextField();
        txtDescripcion = new JTextField();

        txtPrecio = new JTextField("Sin CCB registrado");
        txtPrecio.setEditable(false);
        txtPrecio.setBackground(new Color(220, 235, 255));
        txtPrecio.setForeground(new Color(0, 60, 160));
        txtPrecio.setFont(new Font("Segoe UI", Font.BOLD, 14));
        txtPrecio.setHorizontalAlignment(JTextField.CENTER);

        String[] dias      = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes"};
        String[] servicios = {"Desayuno", "Almuerzo", "Cena"};
        comboDias     = new JComboBox<>(dias);
        comboServicio = new JComboBox<>(servicios);
        estilizarCombo(comboDias);
        estilizarCombo(comboServicio);

        agregarCampoFormulario(panelFormulario, "ID (Automático):", txtId, 0);
        agregarCampoFormulario(panelFormulario, "Nombre:",          txtNombre, 1);
        agregarCampoFormulario(panelFormulario, "Descripción:",     txtDescripcion, 2);
        agregarCampoFormulario(panelFormulario, "Precio CCB (Bs):", txtPrecio, 3);
        agregarCampoFormulario(panelFormulario, "Día:",             comboDias, 4);
        agregarCampoFormulario(panelFormulario, "Servicio:",        comboServicio, 5);

        JLabel lblNotaPrecio = new JLabel("<html><i>* El precio es fijo sedun el CCB.</i></html>");
        lblNotaPrecio.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblNotaPrecio.setForeground(new Color(120, 120, 120));
        GridBagConstraints gNota = new GridBagConstraints();
        gNota.gridx = 0; gNota.gridy = 6; gNota.gridwidth = 2;
        gNota.anchor = GridBagConstraints.WEST;
        gNota.insets = new Insets(0, 0, 10, 0);
        panelFormulario.add(lblNotaPrecio, gNota);

        JPanel panelAcciones = new JPanel(new GridLayout(2, 2, 10, 10));
        panelAcciones.setOpaque(false);
        panelAcciones.setBorder(new EmptyBorder(10, 0, 0, 0));

        btnAgregar = crearBotonEstilizado("AGREGAR",  new Color(0, 75, 237));
        btnEditar  = crearBotonEstilizado("EDITAR",   new Color(255, 193, 7));
        btnEliminar = crearBotonEstilizado("ELIMINAR", new Color(220, 53, 69));
        btnLimpiar  = crearBotonEstilizado("LIMPIAR",  new Color(108, 117, 125));

        panelAcciones.add(btnAgregar);
        panelAcciones.add(btnEditar);
        panelAcciones.add(btnEliminar);
        panelAcciones.add(btnLimpiar);

        GridBagConstraints gBtn = new GridBagConstraints();
        gBtn.gridx = 0; gBtn.gridy = 7; gBtn.gridwidth = 2;
        gBtn.fill = GridBagConstraints.HORIZONTAL;
        gBtn.insets = new Insets(10, 0, 0, 0);
        panelFormulario.add(panelAcciones, gBtn);

        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBackground(Color.WHITE);
        panelTabla.setBorder(new EmptyBorder(20, 20, 20, 20));

        String[] columnas = {"ID", "Nombre", "Descripción", "Precio (Bs)", "Día", "Servicio"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaMenu = new JTable(modeloTabla);
        estilizarTabla(tablaMenu);

        JScrollPane scrollTabla = new JScrollPane(tablaMenu);
        scrollTabla.getViewport().setBackground(Color.WHITE);
        scrollTabla.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        panelTabla.add(scrollTabla, BorderLayout.CENTER);

        tablaMenu.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                int fila = tablaMenu.getSelectedRow();
                if (fila != -1) {
                    txtId.setText(modeloTabla.getValueAt(fila, 0).toString());
                    txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
                    txtDescripcion.setText(modeloTabla.getValueAt(fila, 2).toString());
                    comboDias.setSelectedItem(modeloTabla.getValueAt(fila, 4).toString());
                    comboServicio.setSelectedItem(modeloTabla.getValueAt(fila, 5).toString());
                }
            }
        });

        setLayout(new BorderLayout());
        add(header, BorderLayout.NORTH);
        add(panelFormulario, BorderLayout.WEST);
        add(panelTabla, BorderLayout.CENTER);
    }

    public void setPrecioReadOnly(String valor) {
        txtPrecio.setText(valor);
    }

    public String getId()          { return txtId.getText(); }
    public String getNombre()      { return txtNombre.getText(); }
    public String getDescripcion() { return txtDescripcion.getText(); }
    public String getPrecio()      { return txtPrecio.getText(); } // solo lectura
    public String getDia()         { return comboDias.getSelectedItem().toString(); }
    public String getTipoServicio(){ return comboServicio.getSelectedItem().toString(); }

    public void limpiarTabla()              { modeloTabla.setRowCount(0); }
    public void agregarFilaTabla(Object[] f){ modeloTabla.addRow(f); }

    public void limpiarFormulario() {
        txtId.setText("");
        txtNombre.setText("");
        txtDescripcion.setText("");
        comboDias.setSelectedIndex(0);
        comboServicio.setSelectedIndex(0);
        tablaMenu.clearSelection();
    }

    public void mostrarMensaje(String msg) { JOptionPane.showMessageDialog(this, msg); }
    public void mostrarError(String msg)   { JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE); }

    public void addAgregarListener(ActionListener l)  { btnAgregar.addActionListener(l); }
    public void addEditarListener(ActionListener l)   { btnEditar.addActionListener(l); }
    public void addEliminarListener(ActionListener l) { btnEliminar.addActionListener(l); }
    public void addLimpiarListener(ActionListener l)  { btnLimpiar.addActionListener(l); }
    public void addDashboardListener(ActionListener l){ btnDashboard.addActionListener(l); }
    public void addCostosListener(ActionListener l)   { btnCostos.addActionListener(l); }

    public void setModoEdicion(boolean activado) {
        if (activado) {
            btnEditar.setText("GUARDAR");
            btnEditar.setBackground(new Color(40, 167, 69));
        } else {
            btnEditar.setText("EDITAR");
            btnEditar.setBackground(new Color(255, 193, 7));
        }
        btnEditar.repaint();
    }

    public boolean isModoEdicion() {
        return btnEditar.getText().equals("GUARDAR");
    }

    private void agregarCampoFormulario(JPanel p, String label, JComponent f, int y) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        f.setPreferredSize(new Dimension(200, 30));
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0; g.gridy = y; g.anchor = GridBagConstraints.WEST; g.insets = new Insets(5, 0, 5, 5);
        p.add(lbl, g);
        g.gridx = 1; g.fill = GridBagConstraints.HORIZONTAL; g.weightx = 1.0;
        p.add(f, g);
    }

    private void estilizarCombo(JComboBox<?> c) {
        c.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        c.setBackground(Color.WHITE);
    }

    private void estilizarTabla(JTable t) {
        t.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        t.setRowHeight(35);
        t.getTableHeader().setBackground(new Color(41, 41, 41));
        t.getTableHeader().setForeground(Color.WHITE);
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        t.getTableHeader().setReorderingAllowed(false);
    }

    private JButton crearBotonEstilizado(String texto, Color bg) {
        JButton b = new JButton(texto) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color colorActual = getBackground();
                g2.setColor(getModel().isPressed() ? colorActual.darker() : colorActual);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }
}
