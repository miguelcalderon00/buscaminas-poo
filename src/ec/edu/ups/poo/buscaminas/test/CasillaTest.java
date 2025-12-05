package ec.edu.ups.poo.buscaminas.test; // Asumiendo que las pruebas van en el paquete 'test'

import ec.edu.ups.poo.buscaminas.modelo.Casilla; // ¡Importación Corregida!
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*; // Para usar assertTrue, assertEquals, etc.

public class CasillaTest {

    // ----------------------------------------------------
    // Prueba para el método setMinasAlrededor y getMinasAlrededor
    // ----------------------------------------------------
    
    @Test
    void testSetMinasAlrededor() {
        // 1. Arrange (Preparar)
        Casilla casilla = new Casilla();
        int minasEsperadas = 5;

        // 2. Act (Ejecutar)
        casilla.setMinasAlrededor(minasEsperadas);

        // 3. Assert (Verificar)
        // Usamos assertEquals para comprobar que el valor devuelto sea 5
        assertEquals(minasEsperadas, casilla.getMinasAlrededor(), 
                     "El número de minas alrededor no es el esperado.");
        
        // También podemos probar con 0 o con un valor diferente
        casilla.setMinasAlrededor(0);
        assertEquals(0, casilla.getMinasAlrededor(), "Debe poder establecerse a cero.");
    }
    
    // ----------------------------------------------------
    // Prueba para el método tieneMina y colocarMina
    // ----------------------------------------------------
    
    @Test
    void testColocarMina() {
        Casilla casilla = new Casilla();
        
        // Al inicio, no debe tener mina
        assertFalse(casilla.tieneMina(), "Al inicio, la casilla no debe tener mina.");
        
        // Colocamos la mina
        casilla.colocarMina();
        
        // Ahora debe tener mina
        assertTrue(casilla.tieneMina(), "Después de colocar la mina, debe tener una mina.");
    }

    // ----------------------------------------------------
    // Prueba para el método descubrir
    // ----------------------------------------------------

    @Test
    void testDescubrir() {
        Casilla casilla = new Casilla();
        
        // Al inicio, no debe estar descubierta
        assertFalse(casilla.estaDescubierta(), "Al inicio, la casilla no debe estar descubierta.");
        
        // Descubrimos la casilla
        casilla.descubrir();
        
        // Ahora debe estar descubierta
        assertTrue(casilla.estaDescubierta(), "Después de llamar a descubrir(), debe estar descubierta.");
    }

    // ----------------------------------------------------
    // Prueba para los métodos marcar y desmarcar
    // ----------------------------------------------------
    
    @Test
    void testMarcarYDesmarcar() {
        Casilla casilla = new Casilla();

        // 1. Verificar estado inicial
        assertFalse(casilla.estaMarcada(), "Al inicio, la casilla no debe estar marcada.");
        
        // 2. Marcar y verificar
        casilla.marcar();
        assertTrue(casilla.estaMarcada(), "Después de marcar, la casilla debe estar marcada.");
        
        // 3. Desmarcar y verificar
        casilla.desmarcar();
        assertFalse(casilla.estaMarcada(), "Después de desmarcar, la casilla no debe estar marcada.");
    }
}