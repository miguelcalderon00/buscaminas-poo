 Proyecto Buscaminas en Java – Examen POO

Este proyecto implementa una versión del clásico juego Buscaminas, desarrollado en Java como parte del examen práctico de la asignatura Programación Orientada a Objetos (POO).

 Características implementadas
 Etapa 1 – Modelo

Clase Casilla con atributos: mina, descubierta, marcada y número de minas alrededor.

Clase Tablero con:

Matriz de casillas 10x10

Generación aleatoria de minas

Cálculo automático del número de minas vecinas

Métodos para descubrir, marcar y validar casillas

Etapa 2 – Vista

Clase VistaConsola encargada de mostrar el tablero en pantalla.

Representación clara de:

Casillas ocultas

Casillas descubiertas

Minas

Casillas marcadas como sospecha

 Etapa 3 – Controlador

Clase Juego con toda la lógica principal:

Menú interactivo por consola

Lectura de coordenadas (A-J / 1-10)

Validación con excepciones personalizadas

Condición de victoria y derrota

 Etapa 4 – Persistencia

Guardado de partida en archivo partida.dat

Carga de partidas previas

Uso de Serializable

 Excepciones personalizadas

Implementadas según la rúbrica:

CasillaYaDescubiertaException

Otras validaciones mediante ArrayIndexOutOfBoundsException e InputMismatchException.

Cómo ejecutar el programa

Descargar el proyecto

Importar en Eclipse como Java Project

Ejecutar la clase:

src/ec/edu/ups/poo/buscaminas/controlador/BuscaminasApp.java


Jugar desde la consola con las teclas:

D = Descubrir

M = Marcar

G = Guardar

C = Cargar

S = Salir

 Enlace al repositorio

(https://github.com/miguelcalderon00/buscaminas-poo)

 Autores

Miguel Calderón
Milton Manosalvas
Elly Anilema
Universidad Politécnica Salesiana
Examen Práctico – Programación Orientada a Objetos
