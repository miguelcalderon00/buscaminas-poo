package ec.edu.ups.poo.buscaminas.modelo;

import java.io.Serializable;
import java.util.Random;

import ec.edu.ups.poo.buscaminas.excepciones.CasillaYaDescubiertaException;

public class Tablero implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int FILAS = 10;
    public static final int COLUMNAS = 10;
    public static final int NUM_MINAS = 10;

    private Casilla[][] casillas;

    public Tablero() {
        this.casillas = new Casilla[FILAS][COLUMNAS];
        inicializarCasillas();
        colocarMinasAleatorias();
        calcularMinasAlrededor();
    }

    // Inicializa todas las casillas
    private void inicializarCasillas() {
        for (int fila = 0; fila < FILAS; fila++) {
            for (int col = 0; col < COLUMNAS; col++) {
                casillas[fila][col] = new Casilla();
            }
        }
    }

    // Coloca 10 minas aleatoriamente
    private void colocarMinasAleatorias() {
        Random random = new Random();
        int minasColocadas = 0;

        while (minasColocadas < NUM_MINAS) {
            int fila = random.nextInt(FILAS);
            int col = random.nextInt(COLUMNAS);

            if (!casillas[fila][col].tieneMina()) {
                casillas[fila][col].colocarMina();
                minasColocadas++;
            }
        }
    }

    // Calcula los números alrededor para cada casilla
    private void calcularMinasAlrededor() {
        for (int fila = 0; fila < FILAS; fila++) {
            for (int col = 0; col < COLUMNAS; col++) {

                if (!casillas[fila][col].tieneMina()) {
                    int minas = contarMinasVecinas(fila, col);
                    casillas[fila][col].setMinasAlrededor(minas);
                }
            }
        }
    }

    // Cuenta minas alrededor de la casilla
    private int contarMinasVecinas(int fila, int col) {
        int contador = 0;

        for (int i = fila - 1; i <= fila + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {

                if (estaDentroTablero(i, j) && !(i == fila && j == col)) {
                    if (casillas[i][j].tieneMina()) {
                        contador++;
                    }
                }
            }
        }
        return contador;
    }

    // Verifica límites del tablero
    private boolean estaDentroTablero(int fila, int col) {
        return fila >= 0 && fila < FILAS &&
               col >= 0 && col < COLUMNAS;
    }

    // Permite obtener la casilla según fila/columna
    public Casilla getCasilla(int fila, int col) {
        if (!estaDentroTablero(fila, col)) {
            return null;
        }
        return casillas[fila][col];
    }

    // ----------------- LÓGICA DEL JUEGO -----------------

    /**
     * Descubre una casilla.
     * @return true si se pisó una mina, false en caso contrario.
     * @throws CasillaYaDescubiertaException si la casilla ya estaba descubierta.
     */
    public boolean descubrirCasilla(int fila, int col) throws CasillaYaDescubiertaException {
        if (!estaDentroTablero(fila, col)) {
            throw new ArrayIndexOutOfBoundsException("Coordenadas fuera del tablero");
        }

        Casilla casilla = casillas[fila][col];

        if (casilla.estaDescubierta()) {
            throw new CasillaYaDescubiertaException("La casilla ya está descubierta.");
        }

        casilla.descubrir();

        if (casilla.tieneMina()) {
            // Pisó una mina
            return true;
        }

        // Si no hay minas alrededor, descubrimos en "cascada"
        if (casilla.getMinasAlrededor() == 0) {
            descubrirVecinasVacias(fila, col);
        }

        return false;
    }

    // Descubre recursivamente las casillas vacías alrededor
    private void descubrirVecinasVacias(int fila, int col) {
        for (int i = fila - 1; i <= fila + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {

                if (estaDentroTablero(i, j) && !(i == fila && j == col)) {
                    Casilla vecina = casillas[i][j];

                    if (!vecina.estaDescubierta() && !vecina.tieneMina()) {
                        vecina.descubrir();

                        if (vecina.getMinasAlrededor() == 0) {
                            descubrirVecinasVacias(i, j);
                        }
                    }
                }
            }
        }
    }

    // Marca o desmarca una casilla
    public void marcarCasilla(int fila, int col) {
        if (!estaDentroTablero(fila, col)) {
            throw new ArrayIndexOutOfBoundsException("Coordenadas fuera del tablero");
        }

        Casilla casilla = casillas[fila][col];

        // No se marca una casilla ya descubierta
        if (casilla.estaDescubierta()) {
            return;
        }

        if (casilla.estaMarcada()) {
            casilla.desmarcar();
        } else {
            casilla.marcar();
        }
    }

    // Verifica si todas las casillas sin mina están descubiertas
    public boolean todasCasillasSegurasDescubiertas() {
        for (int fila = 0; fila < FILAS; fila++) {
            for (int col = 0; col < COLUMNAS; col++) {
                Casilla c = casillas[fila][col];
                if (!c.tieneMina() && !c.estaDescubierta()) {
                    return false;
                }
            }
        }
        return true;
    }
}
