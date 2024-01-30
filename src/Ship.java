public class Ship {
    // Atributos de la clase Ship
    private int size; // Tamaño del barco
    private int hits; // Número de veces que el barco ha sido golpeado
    private int startX; // Coordenada X inicial del barco
    private int startY; // Coordenada Y inicial del barco
    private boolean isHorizontal; // Indica si el barco está colocado horizontalmente

    /**
     * Constructor de la clase Ship.
     * @param size Tamaño del barco.
     * @param startX Coordenada X inicial del barco.
     * @param startY Coordenada Y inicial del barco.
     * @param isHorizontal Verdadero si el barco está colocado horizontalmente.
     */
    public Ship(int size, int startX, int startY, boolean isHorizontal) {
        this.size = size;
        this.startX = startX;
        this.startY = startY;
        this.isHorizontal = isHorizontal;
        this.hits = 0; // Inicializa el contador de golpes a 0
    }

    /**
     * Verifica si el barco ha sido golpeado en las coordenadas especificadas.
     * @param x Coordenada X del ataque.
     * @param y Coordenada Y del ataque.
     * @return Verdadero si el barco es golpeado en esas coordenadas.
     */
    public boolean isHit(int x, int y) {
        // Comprueba si las coordenadas del ataque coinciden con la posición del barco
        if (isHorizontal) {
            return x == startX && y >= startY && y < startY + size;
        } else {
            return y == startY && x >= startX && x < startX + size;
        }
    }

    /**
     * Incrementa el contador de golpes al barco.
     */
    public void hit() {
        hits++; // Aumenta el número de golpes recibidos por el barco
    }

    /**
     * Verifica si el barco ha sido hundido.
     * @return Verdadero si el número de golpes es igual al tamaño del barco.
     */
    public boolean isSunk() {
        return hits == size; // Compara los golpes recibidos con el tamaño del barco
    }

}
