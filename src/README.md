# **BattleshipGame.java**

Este archivo contiene la lógica central del juego de batalla naval. Gestiona el tablero del juego, la colocación de los barcos, y el proceso de ataque durante el juego. Incluye métodos para colocar barcos en el tablero, verificar si un ataque ha golpeado un barco, y determinar si el juego ha terminado.

# **Server.java**

Este archivo establece y maneja el servidor para el juego de batalla naval. Espera conexiones de clientes, inicia hilos y coordina la parte del juego que ocurre en el servidor. Utiliza la clase ClientHandler para gestionar la comunicación y la lógica del juego para el cliente conectado.

# **Client.java**

Este archivo implementa la funcionalidad del cliente en el juego de batalla naval. Se conecta al servidor, maneja la interacción del usuario para el juego (como hacer ataques), recibe y procesa respuestas del servidor, y mantiene el estado del juego desde la perspectiva del cliente.

# **Ship.java**

Este archivo define la clase Ship, que representa un barco individual en el juego de batalla naval. Maneja la información sobre el tamaño del barco, su posición en el tablero, la dirección (horizontal o vertical) y el número de veces que ha sido golpeado.