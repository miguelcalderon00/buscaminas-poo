package ec.edu.ups.poo.buscaminas.test;

import ec.edu.ups.poo.buscaminas.controlador.Juego;
import ec.edu.ups.poo.buscaminas.modelo.Casilla;
import ec.edu.ups.poo.buscaminas.modelo.Tablero;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración y Unitarias para la clase Juego, 
 * enfocadas en el flujo de la consola (System.in/System.out) y la lógica de estado.
 */
public class JuegoTest {

    // Guardar los streams originales para restaurar después de cada prueba
    private final InputStream originalSystemIn = System.in;
    private final PrintStream originalSystemOut = System.out;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        // 1. Redirigir System.out para capturar la salida de la consola
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void restoreSystem() {
        // 2. Restaurar System.in y System.out después de cada prueba
        System.setIn(originalSystemIn);
        System.setOut(originalSystemOut);
    }

    /**
     * Simula la entrada del usuario en System.in.
     * ⚠️ NOTA: Este método debe llamarse ANTES de instanciar o llamar a 'iniciar()'.
     * @param data La cadena de comandos a simular.
     */
    private void simularInput(String data) {
        System.setIn(new ByteArrayInputStream(data.getBytes()));
    }
    
    // ----------------------------------------------------
    // PRUEBAS DE FLUJO DEL MENÚ (iniciar)
    // ----------------------------------------------------

    @Test
    void testOpcionSalidaS_TerminaJuego() {
        // Simular: S (Salir)
        simularInput("S\n");
        
        Juego juego = new Juego(); // El constructor usa el System.in modificado
        juego.iniciar();
        
        String salida = outputStream.toString();
        
        // Verifica que el mensaje de salida se haya impreso
        assertTrue(salida.contains("Has salido del juego."));
    }

    @Test
    void testOpcionInvalida_ManejoYContinuacion() {
        // Simular: X (Inválida), S (Salir)
        simularInput("X\nS\n");
        
        Juego juego = new Juego();
        juego.iniciar();
        
        String salida = outputStream.toString();
        
        // Verifica que el mensaje de error se haya impreso
        assertTrue(salida.contains("Opción inválida. Intenta de nuevo."));
        assertTrue(salida.contains("Has salido del juego."));
    }
    
    // ----------------------------------------------------
    // PRUEBAS DE CONVERSIÓN Y ERRORES DE INPUT
    // ----------------------------------------------------

    /**
     * Prueba el método privado 'convertirFila' usando Reflexión.
     */
    @Test
    void testConvertirFila() throws Exception {
        Juego juego = new Juego();
        // Obtener acceso al método privado
        Method convertirFila = Juego.class.getDeclaredMethod("convertirFila", char.class);
        convertirFila.setAccessible(true);
        
        // Probar conversiones válidas (A -> 0, J -> 9)
        assertEquals(0, convertirFila.invoke(juego, 'A'), "La fila 'A' debe convertirse a 0.");
        assertEquals(9, convertirFila.invoke(juego, 'J'), "La fila 'J' debe convertirse a 9.");
        
        // Probar letra fuera de rango (debe lanzar ArrayIndexOutOfBoundsException)
        assertThrows(java.lang.reflect.InvocationTargetException.class, () -> {
            convertirFila.invoke(juego, 'K');
        }, "La letra 'K' debe lanzar una excepción de fuera de rango.");
    }
    
    @Test
    void testManejoErrorColumnaNoEntera() {
        // Simular: D, A (Fila), 'a' (Columna - InputMismatchException), S (Salir)
        simularInput("D\nA\na\nS\n");

        Juego juego = new Juego();
        juego.iniciar();

        String salida = outputStream.toString();
        // El test verifica que el mensaje de error de InputMismatchException se imprima
        assertTrue(salida.contains("Error: la columna debe ser un número entero."));
        // Y que el juego continúe hasta que se introduzca 'S'
        assertTrue(salida.contains("Has salido del juego.")); 
    }
    
    @Test
    void testManejoErrorColumnaFueraDeRango() {
        // Simular: D, A (Fila), 11 (Columna fuera de rango), S (Salir)
        simularInput("D\nA\n11\nS\n"); 

        Juego juego = new Juego();
        juego.iniciar();

        String salida = outputStream.toString();
        // El test verifica que el mensaje de error de ArrayIndexOutOfBoundsException se imprima
        assertTrue(salida.contains("Error: coordenadas fuera del tablero. Usa filas A-J y columnas 1-10."));
        assertTrue(salida.contains("Has salido del juego.")); 
    }
    
    // ----------------------------------------------------
    // PRUEBAS DE LÓGICA DEL JUEGO (DERROTA/VICTORIA)
    // ----------------------------------------------------

    /**
     * Prueba una derrota simulada. Requiere un tablero preconfigurado para ser determinista.
     */
    @Test
    void testDescubrirMinaGeneraDerrota() throws Exception {
        // 1. Configurar un Tablero con una mina en (0, 0)
        Tablero tableroControlado = new Tablero() {
            // Sobrescribir el método de inicialización para colocar minas en lugares conocidos
            private void colocarMinaControlada(int fila, int col) {
                // Usar reflexión para acceder a las casillas y colocar la mina
                try {
                    Field casillasField = Tablero.class.getDeclaredField("casillas");
                    casillasField.setAccessible(true);
                    Casilla[][] casillas = (Casilla[][]) casillasField.get(this);
                    
                    if (casillas[fila][col] != null) {
                        casillas[fila][col].colocarMina();
                    }
                } catch (Exception e) {
                    // Ignorar
                }
            }
        
            @Override
            public boolean descubrirCasilla(int fila, int col) {
                if (fila == 0 && col == 0) {
                    return true; // Simular que se pisa una mina
                }
                return false;
            }
        };

        // 2. Inyectar el tablero controlado en el objeto Juego
        Juego juego = new Juego();
        Field tableroField = Juego.class.getDeclaredField("tablero");
        tableroField.setAccessible(true);
        tableroField.set(juego, tableroControlado);

        // 3. Simular inputs: D, A, 1 (que equivale a 0, 0), S
        simularInput("D\nA\n1\nS\n"); 
        
        juego.iniciar();
        
        String salida = outputStream.toString();
        
        // Verifica el mensaje de derrota
        assertTrue(salida.contains("💥 Pisaste una mina. ¡Has perdido!"));
        // La condición juegoTerminado=true detiene el bucle, por lo que 'S' no debería ejecutarse
        assertFalse(salida.contains("Has salido del juego."), "La salida manual 'S' no debe aparecer si el juego terminó por derrota.");
    }
}