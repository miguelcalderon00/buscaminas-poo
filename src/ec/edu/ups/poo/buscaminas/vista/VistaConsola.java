package ec.edu.ups.poo.buscaminas.vista;

import ec.edu.ups.poo.buscaminas.modelo.Casilla;
import ec.edu.ups.poo.buscaminas.modelo.Tablero;

public class VistaConsola {

    public void mostrarTablero(Tablero tablero, boolean revelarTodo) {

        // Encabezado de columnas: 1 2 3 ... 10
        System.out.print("   ");
        for (int col = 1; col <= Tablero.COLUMNAS; col++) {
            System.out.print(col + " ");
        }
        System.out.println();

        // Filas con letras A B C...
        for (int fila = 0; fila < Tablero.FILAS; fila++) {
            char letraFila = (char) ('A' + fila);
            System.out.print(letraFila + "  ");

            for (int col = 0; col < Tablero.COLUMNAS; col++) {
                Casilla c = tablero.getCasilla(fila, col);
                char simbolo = obtenerSimboloCasilla(c, revelarTodo);
                System.out.print(simbolo + " ");
            }
            System.out.println();
        }
    }

    private char obtenerSimboloCasilla(Casilla casilla, boolean revelarTodo) {

        if (revelarTodo) { // se usa cuando el jugador pierde o gana
            if (casilla.tieneMina()) {
                return 'X';
            } else if (casilla.getMinasAlrededor() > 0) {
                return Character.forDigit(casilla.getMinasAlrededor(), 10);
            } else {
                return 'V';
            }
        }

        // Tablero normal
        if (casilla.estaDescubierta()) {
            if (casilla.tieneMina()) return 'X';
            if (casilla.getMinasAlrededor() > 0) 
                return Character.forDigit(casilla.getMinasAlrededor(), 10);
            return 'V';
        }

        if (casilla.estaMarcada()) return 'F'; // bandera

        return '#'; // casilla oculta
    }
}
