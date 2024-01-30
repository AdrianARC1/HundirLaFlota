import java.io.*;
import java.net.*;

public class Server {
    private ServerSocket serverSocket;
    private BattleshipGame game;

    // Constructor del servidor. Inicializa el juego.
    public Server() {
        this.game = new BattleshipGame();
    }

    // Inicia el servidor en el puerto especificado y espera conexiones de clientes.
    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server en el puerto " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado.");
                new ClientHandler(clientSocket, game).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Clase interna que maneja la lógica de comunicación con un cliente.
    private static class ClientHandler extends Thread {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        private BattleshipGame game;

        public ClientHandler(Socket socket, BattleshipGame game) {
            this.clientSocket = socket;
            this.game = game;
        }

        // Lógica principal del hilo que maneja la interacción con el cliente.

        public void run() {
            try {

                // Establece flujos de entrada y salida para la comunicación con el cliente.

                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                // Comienza el juego colocando los barcos.

                game.placeShips();

                boolean myTurn = true;
                while (!game.isGameOver()) {
                    if (myTurn) {

                        // Lógica para el turno del servidor.

                        String serverAttack;
                        String response;
                        do {
                            serverAttack = game.getAttackCoordinates();
                            out.println(serverAttack);
                            response = in.readLine();
                            game.processResponse(response);
                        } while (response.startsWith("¡Tocado!") && !game.isGameOver());

                        if (!response.equals("Ya atacado.")) {
                            myTurn = false;
                        }
                    } else {

                        // Lógica para el turno del cliente.

                        String clientAttack = in.readLine();
                        if (clientAttack != null) {
                            String result = game.processAttack(clientAttack);
                            out.println(result);
                            if (!result.startsWith("¡Tocado!") || result.equals("Ya atacado.")) {
                                myTurn = true;
                            }
                        } else {
                            System.out.println("Cliente desconectado.");
                            break;
                        }
                    }
                }

                // Cierra los recursos una vez finalizado el juego.

                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("Cliente desconectado o ha ocurrido un error: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start(6666);
    }
}