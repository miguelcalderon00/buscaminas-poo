package ec.edu.ups.poo.buscaminas.test;

import ec.edu.ups.poo.buscaminas.modelo.Tablero;
import ec.edu.ups.poo.buscaminas.modelo.Casilla;
import ec.edu.ups.poo.buscaminas.excepciones.CasillaYaDescubiertaException;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase Tablero.
 * Asume que Casilla.java y CasillaTest.java están en los paquetes correspondientes.
 */
public class TableroTest {

    // ----------------------------------------------------
    // Pruebas de Inicialización y Configuración
    // ----------------------------------------------------

    /**
     * Prueba que el tablero se inicialice con las dimensiones correctas (10x10).
     */
    @Test
    void testInicializacionDimensiones() {
        Tablero tablero = new Tablero();
        
        // Verifica que la casilla inicial y final existan
        assertNotNull(tablero.getCasilla(0, 0), "La casilla (0, 0) no debería ser nula.");
        assertNotNull(tablero.getCasilla(Tablero.FILAS - 1, Tablero.COLUMNAS - 1), 
                      "La última casilla debería existir.");

        // Verifica coordenadas fuera de límites
        assertNull(tablero.getCasilla(-1, 0), "Coordenadas fuera de límites deben ser nulas.");
        assertNull(tablero.getCasilla(Tablero.FILAS, 0), "Coordenadas fuera de límites deben ser nulas.");
    }

    /**
     * Prueba que se coloquen exactamente 10 minas de forma aleatoria.
     */
    @Test
    void testColocarMinasAleatorias() {
        Tablero tablero = new Tablero();
        int contadorMinas = 0;

        for (int fila = 0; fila < Tablero.FILAS; fila++) {
            for (int col = 0; col < Tablero.COLUMNAS; col++) {
                if (tablero.getCasilla(fila, col).tieneMina()) {
                    contadorMinas++;
                }
            }
        }
        
        // Asegura que se colocaron exactamente NUM_MINAS (10)
        assertEquals(Tablero.NUM_MINAS, contadorMinas, "El tablero debe tener exactamente 10 minas.");
    }

    // ----------------------------------------------------
    // Pruebas de Lógica de Juego (Descubrir y Marcar)
    // ----------------------------------------------------

    /**
     * Prueba que al intentar descubrir una casilla ya descubierta se lance la excepción.
     */
    @Test
    void testDescubrirCasillaYaDescubiertaLanzaExcepcion() {
        Tablero tablero = new Tablero();
        int fila = 0, col = 0; 
        
        // 1. Descubrir la casilla por primera vez
        try {
             tablero.descubrirCasilla(fila, col);
        } catch (CasillaYaDescubiertaException e) {
             fail("No debería lanzar excepción la primera vez.");
        }
        
        // 2. Intentar descubrirla de nuevo, esperando la excepción
        assertThrows(CasillaYaDescubiertaException.class, () -> {
            tablero.descubrirCasilla(fila, col);
        }, "Intentar descubrir una casilla descubierta debe lanzar CasillaYaDescubiertaException.");
    }

    /**
     * Prueba la funcionalidad de marcado y desmarcado de una casilla.
     */
    @Test
    void testMarcarYDesmarcarCasilla() {
        Tablero tablero = new Tablero();
        int fila = 1, col = 1;
        Casilla casilla = tablero.getCasilla(fila, col);

        // 1. Marcar
        tablero.marcarCasilla(fila, col);
        assertTrue(casilla.estaMarcada(), "La casilla debe estar marcada.");

        // 2. Desmarcar
        tablero.marcarCasilla(fila, col); // Toggle (cambia el estado)
        assertFalse(casilla.estaMarcada(), "La casilla debe estar desmarcada.");
        
        // 3. Prueba: No se debe marcar una casilla descubierta
        try {
            tablero.descubrirCasilla(fila, col);
        } catch (CasillaYaDescubiertaException e) {
            fail("No debería fallar al descubrir.");
        }
        
        tablero.marcarCasilla(fila, col);
        assertFalse(casilla.estaMarcada(), "Una casilla descubierta no debe poder marcarse.");
    }
}
    
    // ----------------------------------------------------
    // Pruebas de Condición de Victoria
    // ----------------