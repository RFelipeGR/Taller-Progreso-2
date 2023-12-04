import java.util.ArrayList;

public class Parqueadero {
    public static final int TAMANO = 40;
    public static final int NO_HAY_PUESTO = -1;
    public static final int PARQUEADERO_CERRADO = -2;
    public static final int CARRO_NO_EXISTE = -3;
    public static final int CARRO_YA_EXISTE = -4;
    public static final int HORA_INICIAL = 6;
    public static final int HORA_CIERRE = 20;
    public static final int TARIFA_INICIAL = 1200;

    private Puesto puestos[];
    private int tarifa;
    private int caja;
    private int horaActual;
    private boolean abierto;

    public Parqueadero() {
        horaActual = HORA_INICIAL;
        abierto = true;
        tarifa = TARIFA_INICIAL;
        caja = 0;

        puestos = new Puesto[TAMANO];
        for (int i = 0; i < TAMANO; i++)
            puestos[i] = new Puesto(i);
    }

    public String darPlacaCarro(int pPosicion) {
        String respuesta = "";
        if (estaOcupado(pPosicion)) {
            respuesta = "Placa: " + puestos[pPosicion].darCarro().darPlaca();
        } else {
            respuesta = "No hay un carro en esta posición";
        }

        return respuesta;
    }

    public int entrarCarro(String pPlaca) {
        int resultado = 0;
        if (!abierto) {
            resultado = PARQUEADERO_CERRADO;
        } else {
            int numPuestoCarro = buscarPuestoCarro(pPlaca.toUpperCase());
            if (numPuestoCarro != CARRO_NO_EXISTE) {
                resultado = CARRO_YA_EXISTE;
            }

            resultado = buscarPuestoLibre();
            if (resultado != NO_HAY_PUESTO) {
                Carro carroEntrando = new Carro(pPlaca, horaActual);
                puestos[resultado].parquearCarro(carroEntrando);
            }
        }

        return resultado;
    }

    public int sacarCarro(String pPlaca) {
        int resultado = 0;
        if (!abierto) {
            resultado = PARQUEADERO_CERRADO;
        } else {
            int numPuesto = buscarPuestoCarro(pPlaca.toUpperCase());
            if (numPuesto == CARRO_NO_EXISTE) {
                resultado = CARRO_NO_EXISTE;
            } else {
                Carro carro = puestos[numPuesto].darCarro();
                int nHoras = carro.darTiempoEnParqueadero(horaActual);
                int porPagar = nHoras * tarifa;
                caja = caja + porPagar;
                puestos[numPuesto].sacarCarro();
                resultado = porPagar;
            }
        }

        return resultado;
    }

    public int darMontoCaja() {
        return caja;
    }

    public int calcularPuestosLibres() {
        int puestosLibres = 0;
        for (Puesto puesto : puestos) {
            if (!puesto.estaOcupado()) {
                puestosLibres = puestosLibres + 1;
            }
        }
        return puestosLibres;
    }

    public void cambiarTarifa(int pTarifa) {
        tarifa = pTarifa;
    }

    public int buscarPuestoLibre() {
        int puesto = NO_HAY_PUESTO;
        for (int i = 0; i < TAMANO && puesto == NO_HAY_PUESTO; i++) {
            if (!puestos[i].estaOcupado()) {
                puesto = i;
            }
        }
        return puesto;
    }

    public int buscarPuestoCarro(String pPlaca) {
        int puesto = CARRO_NO_EXISTE;
        for (int i = 0; i < TAMANO && puesto == CARRO_NO_EXISTE; i++) {
            if (puestos[i].tieneCarroConPlaca(pPlaca)) {
                puesto = i;
            }
        }
        return puesto;
    }

    public void avanzarHora() {
        if (horaActual <= HORA_CIERRE) {
            horaActual = (horaActual + 1);
        }
        if (horaActual == HORA_CIERRE) {
            abierto = false;
        }
    }

    public int darHoraActual() {
        return horaActual;
    }

    public boolean estaAbierto() {
        return abierto;
    }

    public int darTarifa() {
        return tarifa;
    }

    public boolean estaOcupado(int pPuesto) {
        return puestos[pPuesto].estaOcupado();
    }

    // Nuevos Métodos
    public int contarCarrosQueComienzanConPlacaPB() {
        int cantidadCarrosPlacaPB = 0;
        for (Puesto puesto : puestos) {
            if (puesto.estaOcupado() && puesto.darCarro().darPlaca().toUpperCase().startsWith("PB")) {
                cantidadCarrosPlacaPB++;
            }
        }
        return cantidadCarrosPlacaPB;
    }

    public boolean hayCarroCon24Horas() {
        for (Puesto puesto : puestos) {
            if (puesto.estaOcupado()) {
                Carro carro = puesto.darCarro();
                int tiempoEnParqueadero = carro.darTiempoEnParqueadero(horaActual);
                if (tiempoEnParqueadero >= 24) {
                    return true;
                }
            }
        }
        return false;
    }

    public int desocuparParqueadero() {
        int cantidadCarrosSacados = 0;
        for (Puesto puesto : puestos) {
            if (puesto.estaOcupado()) {
                cantidadCarrosSacados++;
                puesto.sacarCarro();
            }
        }
        return cantidadCarrosSacados;
    }

    // Métodos sin cambios
    public String metodo1() {
        int cantidadCarrosPlacaPB = contarCarrosQueComienzanConPlacaPB();
        boolean hayCarroCon24Horas = hayCarroCon24Horas();

        return "Cantidad de carros con placa PB: " + cantidadCarrosPlacaPB + " – Hay carro parqueado por 24 o más horas: " +
                (hayCarroCon24Horas ? "Sí." : "No.");
    }


    public String metodo2() {
        int cantidadCarrosSacados = desocuparParqueadero();
        return "Cantidad de carros sacados: " + cantidadCarrosSacados + ".";
    }

}
