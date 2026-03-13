package sgcu.controladores;

import sgcu.modelos.SaldoModel;
import sgcu.vistas.SaldoView;

public class SaldoController {
    private SaldoView vistaPrincipal;
    private SaldoModel modelo;
    private String usuarioLogueado;

    public SaldoController(String usuario) {
        this.usuarioLogueado = usuario;
        this.modelo = new SaldoModel();
        this.vistaPrincipal = new SaldoView();

        vistaPrincipal.actualizarEtiquetaSaldo(modelo.obtenerSaldo(usuario));

        // Único botón de acción en el monedero
        vistaPrincipal.addHacerRecargaListener(e -> {
            vistaPrincipal.dispose();
            new RecargaController(usuarioLogueado);
        });

        vistaPrincipal.addVolverMenuListener(e -> { vistaPrincipal.dispose(); new MenuConsumidorController(usuario); });
        vistaPrincipal.addDashboardListener(e -> { vistaPrincipal.dispose(); new DashboardConsumidorController(usuario); });

        vistaPrincipal.setVisible(true);
    }
}