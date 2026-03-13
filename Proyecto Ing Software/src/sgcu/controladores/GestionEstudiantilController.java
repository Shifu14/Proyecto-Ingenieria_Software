package sgcu.controladores;

import sgcu.modelos.CargaCostosModel;
import sgcu.modelos.GestionEstudiantilModel;
import sgcu.vistas.GestionEstudiantilView;
import java.util.List;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class GestionEstudiantilController {

    private GestionEstudiantilView vista;
    private List<GestionEstudiantilModel> listaBeneficios;
    private String adminLogueado;

    public GestionEstudiantilController(String adminLogueado) {
        this.adminLogueado  = adminLogueado;
        this.vista          = new GestionEstudiantilView();
        this.listaBeneficios = GestionEstudiantilModel.cargarTodos();

        actualizarTabla();

        vista.addGuardarListener(e -> procesarGuardar());
        vista.addEditarListener(e -> procesarEditar());
        vista.addEliminarListener(e -> procesarEliminar());
        vista.addDashboardListener(e -> {
            vista.dispose();
            new DashboardAdminController(this.adminLogueado);
        });

       
        vista.addDescuentoChangeListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e)  { actualizarPreviewPrecio(); }
            @Override public void removeUpdate(DocumentEvent e)  { actualizarPreviewPrecio(); }
            @Override public void changedUpdate(DocumentEvent e) { actualizarPreviewPrecio(); }
        });
        
        vista.addTipoChangeListener(e -> actualizarPreviewPrecio());

        vista.setVisible(true);
    }

   


    private void procesarGuardar() {
        if (vista.getCedulaNumeros().isEmpty()) {
            vista.mostrarError("Debe ingresar la cédula del estudiante.");
            return;
        }

        String cedula = vista.getCedulaCompleta();

        boolean existe = listaBeneficios.stream().anyMatch(b -> b.getCedula().equals(cedula));
        if (existe) {
            vista.mostrarError("Este estudiante ya tiene un beneficio asignado.\n" +
                               "Si desea modificarlo, use el botón 'EDITAR BENEFICIO'.");
            return;
        }

        procesarAccion(cedula, true);
    }

   


    private void procesarEditar() {
        if (vista.getCedulaNumeros().isEmpty()) {
            vista.mostrarError("Seleccione un registro en la tabla o ingrese la cédula para editar.");
            return;
        }

        String cedula = vista.getCedulaCompleta();

        boolean existe = listaBeneficios.stream().anyMatch(b -> b.getCedula().equals(cedula));
        if (!existe) {
            vista.mostrarError("No se encontró ningún beneficio previo para esta cédula.\n" +
                               "Use 'GUARDAR BENEFICIO' para crear uno nuevo.");
            return;
        }

        procesarAccion(cedula, false);
    }

    

    private void procesarAccion(String cedula, boolean esNuevo) {
        String tipo = vista.getTipo();

       
        if (!GestionEstudiantilModel.verificarSiEsEstudianteValido(cedula)) {
            vista.mostrarError("La cédula ingresada no existe o no pertenece\n" +
                               "a un 'Estudiante' registrado en el sistema.");
            return;
        }

        double descuento = 0.0;

        if (tipo.equals("Exonerado")) {
            
            descuento = 100.0;

        } else if (tipo.equals("Becario")) {
           
            String descTexto = vista.getDescuento().trim();
            if (descTexto.isEmpty()) {
                vista.mostrarError("Debe ingresar el porcentaje de descuento para el Becario.");
                return;
            }

            try {
                descuento = Double.parseDouble(descTexto);
            } catch (NumberFormatException ex) {
                vista.mostrarError("Ingrese un porcentaje numérico válido (Ej: 30).");
                return;
            }

            


            if (!GestionEstudiantilModel.esDescuentoBecarioValido(descuento)) {
                vista.mostrarError(
                    "El porcentaje del Becario debe estar entre 1 % y " +
                    (int) GestionEstudiantilModel.MAX_DESCUENTO_BECARIO + " %.\n" +
                    "• Mínimo 1 %: el Becario siempre recibe algún descuento.\n" +
                    "• Máximo " + (int) GestionEstudiantilModel.MAX_DESCUENTO_BECARIO +
                    " %: el Becario nunca puede igualarse al Exonerado (100 %)."
                );
                return;
            }
        }

        
        listaBeneficios.removeIf(b -> b.getCedula().equals(cedula));
        listaBeneficios.add(new GestionEstudiantilModel(cedula, tipo, descuento));
        GestionEstudiantilModel.guardarTodos(listaBeneficios);

        actualizarTabla();
        vista.limpiarCampos();
        vista.ocultarPreviewPrecio();
        vista.mostrarMensaje(esNuevo ? "Beneficio asignado exitosamente." : "Beneficio actualizado con éxito.");
    }

    


    private void procesarEliminar() {
        String cedulaSeleccionada = vista.getCedulaSeleccionada();
        if (cedulaSeleccionada != null) {
            int confirm = javax.swing.JOptionPane.showConfirmDialog(
                vista,
                "¿Eliminar el beneficio de la cédula " + cedulaSeleccionada + "?",
                "Confirmar eliminación",
                javax.swing.JOptionPane.YES_NO_OPTION
            );
            if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                listaBeneficios.removeIf(b -> b.getCedula().equals(cedulaSeleccionada));
                GestionEstudiantilModel.guardarTodos(listaBeneficios);
                actualizarTabla();
                vista.limpiarCampos();
                vista.ocultarPreviewPrecio();
            }
        } else {
            vista.mostrarError("Seleccione un registro de la tabla para eliminar.");
        }
    }

    

   

    private void actualizarPreviewPrecio() {
        String tipo = vista.getTipo();

        
        List<CargaCostosModel> costos = CargaCostosModel.cargarTodos();
        if (costos.isEmpty()) {
            vista.ocultarPreviewPrecio();
            return;
        }
        double ccb = costos.get(costos.size() - 1).getCcB();

        if (tipo.equals("Exonerado")) {
            vista.mostrarPreviewPrecio(
                "Exonerado: paga 0.00 Bs (100 % de descuento sobre " +
                String.format("%.2f", ccb) + " Bs)"
            );
            return;
        }

        
        String descTexto = vista.getDescuento().trim();
        if (descTexto.isEmpty()) {
            vista.ocultarPreviewPrecio();
            return;
        }
        try {
            double descuento = Double.parseDouble(descTexto);
            if (GestionEstudiantilModel.esDescuentoBecarioValido(descuento)) {
                GestionEstudiantilModel temp = new GestionEstudiantilModel("", "Becario", descuento);
                double precioFinal = temp.calcularPrecioFinal(ccb);
                vista.mostrarPreviewPrecio(
                    String.format("Becario: paga %.2f Bs (%.0f %% de descuento sobre %.2f Bs)",
                        precioFinal, descuento, ccb)
                );
            } else {
                vista.ocultarPreviewPrecio();
            }
        } catch (NumberFormatException ex) {
            vista.ocultarPreviewPrecio();
        }
    }

    
    

    private void actualizarTabla() {
        vista.getModeloTabla().setRowCount(0);
        for (GestionEstudiantilModel b : listaBeneficios) {
            String descuentoMostrado = b.getTipo().equals("Exonerado")
                ? "100 % (Exonerado)"
                : String.format("%.0f %%", b.getDescuento());

            vista.getModeloTabla().addRow(new Object[]{
                b.getCedula(),
                b.getTipo(),
                descuentoMostrado
            });
        }
    }
}