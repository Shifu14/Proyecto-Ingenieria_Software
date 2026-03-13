package sgcu.modelos;

import java.util.Random;

public class RecargaModel {
    
    /**
     * Valida que los datos ingresados cumplan con el formato de un Pago Móvil real.
     * @param telefono El número de teléfono (debe tener exactamente 7 dígitos)
     * @param cedula La cédula de identidad (entre 6 y 9 dígitos)
     * @param montoTxt El monto en formato texto (debe ser un número válido y mayor a 0)
     * @return true si todos los campos son válidos, false si alguno falla.
     */
    public boolean validarCampos(String telefono, String cedula, String montoTxt) {
        try {
            double monto = Double.parseDouble(montoTxt);
            
            boolean telefonoValido = telefono.length() == 7;
            boolean cedulaValida = cedula.length() >= 6 && cedula.length() <= 9;
            boolean montoValido = monto > 0;

            return telefonoValido && cedulaValida && montoValido;

        } catch (NumberFormatException e) {
            // Si el monto no es un número (por ejemplo, si escriben letras), falla la validación
            return false; 
        }
    }

    /**
     * Simula la generación de un código de referencia bancaria único.
     * @return Un String con formato "PM-XXXXXXXX"
     */
    public String generarReferenciaUnica() {
        Random random = new Random();
        // Genera un número aleatorio de 8 dígitos (entre 10000000 y 99999999)
        int numeroReferencia = 10000000 + random.nextInt(90000000);
        return "PM-" + numeroReferencia;
    }
}