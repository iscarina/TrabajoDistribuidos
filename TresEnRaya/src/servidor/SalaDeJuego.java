package servidor;

import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SalaDeJuego implements Runnable,Serializable{

	private String nombreSala;
    private List<Socket> jugadores;
    
    public SalaDeJuego(String nomSala) {
        this.nombreSala = nomSala;
        this.jugadores = new ArrayList<>();
    }

    public synchronized void addJugador(Socket socketJugador) {
        jugadores.add(socketJugador);
    }

    public synchronized boolean isFull() {
        return jugadores.size() >= 2;
    }
    
    public synchronized  List<Socket> getJugadores() {
    	 return new ArrayList<>(jugadores);
    }
    
    public synchronized String getNombre() {
        return this.nombreSala;
    }

    @Override
    public void run() {
        // Lógica del juego y comunicación entre jugadores
        // Puedes utilizar esta clase para gestionar el juego dentro de la sala
    	System.out.println("He empezado");
    }

}
