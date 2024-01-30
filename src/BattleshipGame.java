import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BattleshipGame {
    // Tableros para el juego y el ataque, tamaño del tablero, lector de consola y lista de barcos
    private char[][] board;
    private char[][] attackBoard;
    public static final int BOARD_SIZE = 4;
    private Scanner scanner;
    private List<Ship> ships;

    // Constructor: inicializa los tableros y las estructuras necesarias para el juego
    public BattleshipGame() {
        board = new char[BOARD_SIZE][BOARD_SIZE];
        attackBoard = new char[BOARD_SIZE][BOARD_SIZE];
        scanner = new Scanner(System.in);
        ships = new ArrayList<>();
        initializeBoard();
    }

    // Inicializa ambos tableros con '-' indicando que no hay nada colocado
    private void initializeBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = '-';
                attackBoard[i][j] = '-';
            }
        }
    }

    // Método para colocar los barcos en el tablero
    public void placeShips() {
        placeShip(2); // Colocar barco de tamaño 2
        placeShip(3); // Colocar barco de tamaño 3
    }

    /**
     * Metodo para colocar un barco en el tablero.
     * @param size El tamaño del barco a colocar.
     */
    private void placeShip(int size) {
        boolean placed = false;
        while (!placed) {

            // Solicita al usuario la posición inicial y la dirección del barco.

            System.out.println("Pon el barco de " + size + " posiciones");
            System.out.print("Introduce las posiciones iniciales (e.j., A1): ");
            String startPos = scanner.nextLine();
            System.out.print("Introduce dirección (H para horizontal, V para vertical): ");
            String direction = scanner.nextLine();

            // Convierte la entrada en coordenadas de fila y columna.

            int row = startPos.charAt(1) - '1';
            int col = startPos.charAt(0) - 'A';
            boolean isHorizontal = direction.equalsIgnoreCase("H");

            // Verifica si la posición del barco es válida.

            if (isPlacementValid(row, col, size, isHorizontal)) {

                // Coloca el barco en el tablero.

                for (int i = 0; i < size; i++) {
                    int currentRow = isHorizontal ? row : row + i;
                    int currentCol = isHorizontal ? col + i : col;
                    board[currentRow][currentCol] = 'B';
                }

                // Agrega el barco a la lista de barcos.

                ships.add(new Ship(size, row, col, isHorizontal));
                placed = true;
            } else {
                System.out.println("Posición inválida para colocar, prueba de nuevo.");
            }
        }
        printBoard(board);
    }

    /**
     * Verifica si la posición elegida para un barco es válida.
     * @param row Fila inicial del barco.
     * @param col Columna inicial del barco.
     * @param size Tamaño del barco.
     * @param isHorizontal Verdadero si el barco es horizontal.
     * @return Verdadero si la posición es válida, Falso en caso contrario.
     */
    private boolean isPlacementValid(int row, int col, int size, boolean isHorizontal) {

        // Verifica cada posición que el barco ocuparía.

        for (int i = 0; i < size; i++) {
            int currentRow = isHorizontal ? row : row + i;
            int currentCol = isHorizontal ? col + i : col;
            if (currentRow < 0 || currentRow >= BOARD_SIZE || currentCol < 0 || currentCol >= BOARD_SIZE || board[currentRow][currentCol] != '-') {
                return false;
            }
        }
        return true;
    }

    // Obtiene las coordenadas de ataque del servidor y del cliente y muestra los tableros
    public String getAttackCoordinates() {
        printBoards();
        System.out.print("Introduce las coordenadas de ataque (e.j., A1): ");
        return scanner.nextLine();
    }

    // Muestra ambos tableros: el del jugador y el de ataque
    private void printBoards() {
        System.out.println("Tu tabla:");
        printBoard(board);
        System.out.println("\nTabla de ataque:");
        printBoard(attackBoard);
    }

    // Procesa un ataque en las coordenadas dadas, actualizando los tableros según sea necesario
    public String processAttack(String attackCoord) {

        // Conversión de coordenadas y verificación de validez

        int row = attackCoord.charAt(1) - '1';
        int col = attackCoord.charAt(0) - 'A';

        if (row < 0 || row >= BOARD_SIZE || col < 0 || col >= BOARD_SIZE) {
            return "Coordenadas invalidas.";
        }

        // Procesamiento del resultado del ataque

        char result = board[row][col];
        if (result == 'B') {

            // Lógica para manejar el impacto en un barco

            board[row][col] = 'X';
            attackBoard[row][col] = 'X';

            for (Ship ship : ships) {
                if (ship.isHit(row, col)) {
                    ship.hit();
                    if (ship.isSunk()) {
                        if (isGameOver()) {
                            return "¡Tocado y hundido! ¡Juego terminado!";
                        }
                        return "¡Tocado y hundido!";
                    }
                    return "¡Tocado!";
                }
            }
        } else if (result == '-') {
            // Lógica para manejar un fallo (agua)
            attackBoard[row][col] = 'W'; // Marca la casilla como agua
            return "¡Fallo!";
        } else {
            return "Ya atacado.";
        }
        return "¡Fallo!";
    }

    // Procesa la respuesta del oponente mostrando el resultado del ataque
    public void processResponse(String response) {
        System.out.println("Respuesta del oponente: " + response);
    }

    // Verifica si el juego ha terminado (todos los barcos hundidos)
    public boolean isGameOver() {
        for (Ship ship : ships) {
            if (!ship.isSunk()) {
                return false;
            }
        }
        return true;
    }

    // Muestra el tablero con letras y numeros
    private void printBoard(char[][] boardToPrint) {
        System.out.println("  A B C D");
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < BOARD_SIZE; j++) {
                System.out.print(boardToPrint[i][j] + " ");
            }
            System.out.println();
        }
    }
}
