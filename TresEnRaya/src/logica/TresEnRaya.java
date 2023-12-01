package logica;

import java.util.ArrayList;

public class TresEnRaya {
	 
    //Represenntaciones del simbolo de los jugadores y del simbolo por defecto, que indica que esa casilla esta vacia.
    private final char J1 = 'X';
    private final char J2 = 'O';
    private final char VACIO = '-';
 
    //Turno
    //Si es true es el turno del J1, si es false del J2
    private boolean turno;
 
    //Tablero
    private char tablero[][];
 
    public TresEnRaya() {
        this.turno = true;
        this.tablero = new char[3][3];
        this.inicializarTablero();
    }
 
    /**
     * Inicializa el tablero vacio
     */
    private void inicializarTablero() {
 
        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero.length; j++) {
                tablero[i][j] = VACIO;
            }
        }
 
    }
 
    /**
     * Indica si es el fin de la partida, acaba cuando hay un ganador o el
     * tablero esta lleno
     *
     * @return fin de partida
     */
    public boolean finPartida() {
 
        if (tableroLleno()
                || coincidenciaFila() != VACIO
                || coincidenciaColumna() != VACIO
                || coincidenciaDiagonal() != VACIO) {
            return true;
        }
 
        return false;
    }
 
    /**
     * Indica si el tablero esta lleno. 
     * Cuando el simbolo por defecto, en este caso '-', no aparezca.
     * @return true esta lleno, false no esta lleno.
     */
    public boolean tableroLleno() {
        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero[0].length; j++) {
                if (tablero[i][j] == VACIO) {
                    return false;
                }
            }
        }
        return true;
    }
 
    /**
     * Indica si hay algun ganador en alguna fila
     *
     * @return Devuelve el simbolo del ganador, en caso de no haber ninguno devolvera el simbolo por defecto.
     */
    private char coincidenciaFila() {
 
        char simbolo;
        boolean coincidencia;
 
        for (int i = 0; i < tablero.length; i++) {
 
            coincidencia = true;
            simbolo = tablero[i][0];
            if (simbolo != VACIO) {
                for (int j = 1; j < tablero[0].length; j++) {
                    if (simbolo != tablero[i][j]) {
                        coincidencia = false;
                    }
                }
 
                if (coincidencia) {
                    return simbolo;
                }
 
            }
 
        }
 
        //Si no hay ganador, devuelvo el simbolo por defecto
        return VACIO;
 
    }
 
    /**
     * Indica si hay un ganador en alguna columna
     *
     * @return Devuelve el simbolo del ganador, en caso de no haber ninguno devolvera el simbolo por defecto.
     */
    private char coincidenciaColumna() {
 
        char simbolo;
        boolean coincidencia;
 
        for (int j = 0; j < tablero.length; j++) {
 
            coincidencia = true;
            simbolo = tablero[0][j];
            if (simbolo != VACIO) {
                for (int i = 1; i < tablero[0].length; i++) {
                    if (simbolo != tablero[i][j]) {
                        coincidencia = false;
                    }
                }
 
                if (coincidencia) {
                    return simbolo;
                }
 
            }
 
        }
 
        //Si no hay ganador, devuelvo el simbolo por defecto
        return VACIO;
 
    }
 
    /**
     * Indica si hay un ganador en alguna columna
     *
     * @return Devuelve el simbolo del ganador, en caso de no haber ninguno devolvera el simbolo por defecto.
     */
    private char coincidenciaDiagonal() {
 
        char simbolo;
        boolean coincidencia = true;
 
        //Diagonal principal
        simbolo = tablero[0][0];
        if (simbolo != VACIO) {
            for (int i = 1; i < tablero.length; i++) {
                if (simbolo != tablero[i][i]) {
                    coincidencia = false;
                }
            }
 
            if (coincidencia) {
                return simbolo;
            }
 
        }
 
        coincidencia = true;
 
        //Diagonal inversa
        simbolo = tablero[0][2];
        if (simbolo != VACIO) {
            for (int i = 1, j = 1; i < tablero.length; i++, j--) {
                if (simbolo != tablero[i][j]) {
                    coincidencia = false;
                }
            }
 
            if (coincidencia) {
                return simbolo;
            }
        }
 
        //Si no hay ganador, devuelvo el simbolo por defecto
        return VACIO;
 
    }
 
    /**
     * Muestra el ganador de la partida
     * @return 
     */
    public ArrayList<String> ganador() {
 
        char simbolo = coincidenciaFila();
        
        ArrayList<String> g = new ArrayList<>();
        
        if (simbolo != VACIO) {
        	
        	g.add(String.valueOf(simbolo));
        	g.add(String.valueOf(1));
            return g;
 
        }
 
        simbolo = coincidenciaColumna();
 
        if (simbolo != VACIO) {
 
        	g.add(String.valueOf(simbolo));
        	g.add(String.valueOf(2));
            return g;
 
        }
 
        simbolo = coincidenciaDiagonal();
 
        if (simbolo != VACIO) {
 
        	g.add(String.valueOf(simbolo));
        	g.add(String.valueOf(3));
            return g;
 
        }
        
        g.add("Habeis empatado");
        return g;
 
    }
 
//    /**
//     * Funcion auxiliar de la anterior funcion
//     *
//     * @param simbolo
//     * @param tipo
//     */
//    private String ganador(char simbolo, int tipo) {
// 
//        switch (tipo) {
//            case 1:
//                if (simbolo == J1) {
//                    return("Ha ganado el Jugador 1 por linea");
//                } else {
//                	return("Ha ganado el Jugador 2 por linea");
//                }
// 
//		case 2:
//                if (simbolo == J1) {
//                	return("Ha ganado el Jugador 1 por columna");
//                } else {
//                	return("Ha ganado el Jugador 2 por columna");
//                }
//		case 3:
//                if (simbolo == J1) {
//                	return("Ha ganado el Jugador 1 por diagonal");
//                } else {
//                	return("Ha ganado el Jugador 2 por diagonal");
//                }
//        }
//		return null;
//    }
 
    /**
     * Insertamos en una posición del tablero un simbolo
     *
     * @param fila
     * @param columna
     */
    public void insertarEn(int fila, int columna) {
        if (turno) {
            this.tablero[fila][columna] = J1;
        } else {
            this.tablero[fila][columna] = J2;
        }
    }
 
    /**
     * Devuelve el tablero
     *
     */
    public ArrayList<String> getTablero() {
    	
		ArrayList<String> tablero = new ArrayList<>();
    	
        for (int i = 0; i < this.tablero.length; i++) {
            for (int j = 0; j < this.tablero[0].length; j++) {
            	tablero.add(this.tablero[i][j] + " ");
            }
        }
        
        return tablero;
 
    }
    
    /**
     * Devuelve el turno
     */
    public boolean getTurno() {
 
        return turno;
 
    }
 
    /**
     * Cambia el turno
     */
    public void cambiaTurno() {
        this.turno = !this.turno;
    }
 
    /**
     * Valida la posición
     *
     * @param fila
     * @param columna
     * @return
     */
    public boolean validarPosicion(int fila, int columna) {
 
        if (fila >= 0 && fila < tablero.length && columna >= 0 && columna < tablero.length) {
            return true;
        }
        return false;
 
    }
 
    /**
     * Indicamos si en una posicion en concreto esta ocupada o no
     *
     * @param fila
     * @param columna
     * @return true si la posicion esta ocupada, false si no.
     */
    public boolean hayValorPosicion(int fila, int columna) {
        if (this.tablero[fila][columna] != VACIO) {
            return true;
        }
 
        return false;
    }
 
}