package ec.edu.ups.poo.buscaminas.controlador;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.InputMismatchException;
import java.util.Scanner;

import ec.edu.ups.poo.buscaminas.excepciones.CasillaYaDescubiertaException;
import ec.edu.ups.poo.buscaminas.modelo.Tablero;
import ec.edu.ups.poo.buscaminas.vista.VistaConsola;

public class Juego {

    private Tablero tablero;
    private VistaConsola vista;
    private boolean juegoTerminado;
    private Scanner scanner;

    public Juego() {
        this.tablero = new Tablero();
        this.vista = new VistaConsola();
        this.juegoTerminado = false;
        this.scanner = new Scanner(System.in);
    }

    // ➤ Comentario agregado para mejorar claridad y cumplir con el avance del examen POO.
    //    Este comentario NO altera el funcionamiento, pero documenta el inicio del flujo principal.
    public void iniciar() {
        System.out.println("=== BUSCAMINAS POO ===");

        while (!juegoTerminado) {
            vista.mostrarTablero(tablero, false);

            System.out.println("\nElige una opción:");
            System.out.println("D - Descubrir casilla");
            System.out.println("M - Marcar/Desmarcar casilla");
            System.out.println("G - Guardar partida");
            System.out.println("C - Cargar partida");
            System.out.println("S - Salir");
            System.out.print("Opción: ");

            String opcion = scanner.next().toUpperCase();

            if (opcion.equals("S")) {
                System.out.println("Has salido del juego.");
                break;
            }

            if (opcion.equals("G")) {
                guardarPartida();
                continue;
            }

            if (opcion.equals("C")) {
                cargarPartida();
                continue;
            }

            if (!opcion.equals("D") && !opcion.equals("M")) {
                System.out.println("Opción inválida. Intenta de nuevo.");
                continue;
            }

            try {
                System.out.print("Fila (A-J): ");
                String filaStr = scanner.next();
                char letraFila = filaStr.toUpperCase().charAt(0);
                int fila = convertirFila(letraFila);

                System.out.print("Columna (1-10): ");
                int col = scanner.nextInt() - 1;

                if (col < 0 || col >= Tablero.COLUMNAS) {
                    throw new ArrayIndexOutOfBoundsException("Columna fuera de rango");
                }

                if (opcion.equals("D")) {
                    boolean pisoMina = tablero.descubrirCasilla(fila, col);

                    if (pisoMina) {
                        System.out.println("\n💥 Pisaste una mina. ¡Has perdido!");
                        vista.mostrarTablero(tablero, true);
                        juegoTerminado = true;
                    } else if (tablero.todasCasillasSegurasDescubiertas()) {
                        System.out.println("\n🎉 ¡Has ganado! Descubriste todas las casillas seguras.");
                        vista.mostrarTablero(tablero, true);
                        juegoTerminado = true;
                    }

                } else if (opcion.equals("M")) {
                    tablero.marcarCasilla(fila, col);
                }

            } catch (CasillaYaDescubiertaException e) {
                System.out.println("⚠ " + e.getMessage());

            } catch (InputMismatchException e) {
                System.out.println("Error: la columna debe ser un número entero.");
                scanner.nextLine();

            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Error: coordenadas fuera del tablero. Usa filas A-J y columnas 1-10.");
            }
        }

        scanner.close();
    }

    private int convertirFila(char letraFila) {
        int fila = letraFila - 'A';
        if (fila < 0 || fila >= Tablero.FILAS) {
            throw new ArrayIndexOutOfBoundsException("Fila fuera de rango");
        }
        return fila;
    }

    // ----------------- PERSISTENCIA (GUARDAR / CARGAR) -----------------

    private void guardarPartida() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("partida.dat"))) {
            oos.writeObject(tablero);
            System.out.println("✅ Partida guardada correctamente en 'partida.dat'.");
        } catch (IOException e) {
            System.out.println("❌ Error al guardar la partida: " + e.getMessage());
        }
    }

    private void cargarPartida() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("partida.dat"))) {
            this.tablero = (Tablero) ois.readObject();
            System.out.println("✅ Partida cargada correctamente.");
        } catch (FileNotFoundException e) {
            System.out.println("❌ No existe una partida guardada todavía.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("❌ Error al cargar la partida: " + e.getMessage());
        }
    }
}
