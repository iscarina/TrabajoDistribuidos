package servidor;

import java.io.*;
import java.net.*;
import java.util.*;

public class SalaDeJuego implements Runnable,Serializable{

	private String nombreSala;
    private List<Socket> jugadores;
    private Map<Socket, ObjectOutputStream> outputs;
    
    public SalaDeJuego(String nomSala) {
        this.nombreSala = nomSala;
        this.jugadores = new ArrayList<>();
        this.outputs = new HashMap<>();
    }

    public synchronized void addJugador(Socket socketJugador, ObjectOutputStream out) {
        jugadores.add(socketJugador);
    	outputs.put(socketJugador, out);
    }

    public synchronized boolean isFull() {
        return jugadores.size() >= 2;
    }
    
    public synchronized List<Socket> getJugadores() {
    	 return new ArrayList<>(jugadores);
    }
    
    public synchronized String getNombre() {
        return this.nombreSala;
    }

    private void enviarMensaje(Object mensaje, Socket destinatario) {
        try {
        	ObjectOutputStream out = outputs.get(destinatario);
            out.writeObject(mensaje);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void run() {
    	System.out.println("La sala " + nombreSala + " ha comenzado.");
    	
        enviarMensaje((Object)"¡Hola, jugador!", this.jugadores.get(0));
    }

}
