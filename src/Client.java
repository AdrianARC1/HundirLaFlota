import java.io.*;
import java.net.*;

public class Client {
    // Variables para gestionar la conexión de red y la comunicación.
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private BattleshipGame game;

    // Constructor: Inicializa un nuevo juego.

    public Client() {
        this.game = new BattleshipGame();
    }

    // Establece conexión con el servidor e inicia la lógica del juego.
    public void startConnection(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // Comienza el juego colocando los barcos.

            game.placeShips();

            // Controla el turno del cliente en el juego.

            boolean myTurn = false;
            while (!game.isGameOver()) {
                if (myTurn) {

                    // Lógica para el turno del cliente.

                    String clientAttack;
                    String response;
                    do {

                        // Obtiene las coordenadas de ataque y las envía al servidor.

                        clientAttack = game.getAttackCoordinates();
                        out.println(clientAttack);

                        // Espera y procesa la respuesta del servidor.

                        response = in.readLine();
                        game.processResponse(response);
                    } while (response.startsWith("¡Tocado!") && !game.isGameOver());

                    // Cambia el turno si no se ha tocado un barco.

                    if (!response.equals("Ya atacado.")) {
                        myTurn = false;
                    }
                } else {

                    // Procesa el turno del servidor.

                    String serverAttack = in.readLine();
                    if (serverAttack != null) {
                        String result = game.processAttack(serverAttack);
                        out.println(result);

                        // Cambia el turno si el servidor no ha tocado un barco.

                        if (!result.startsWith("¡Tocado!") || result.equals("Ya atacado.")) {
                            myTurn = true;
                        }
                    } else {

                        // Finaliza el juego si el servidor se desconecta.

                        System.out.println("Server desconectado.");
                        break;
                    }
                }
            }

            stopConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Cierra los recursos de red utilizados por el cliente.
    public void stopConnection() {
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.startConnection("127.0.0.1", 6666);
    }
}