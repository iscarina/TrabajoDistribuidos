package servidor;

import java.io.*;
import java.net.Socket;

public class AsignarSala implements Runnable{
	
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private Socket s;
	
	public AsignarSala(ObjectOutputStream o, ObjectInputStream i, Socket sc) {
		this.out = o;
		this.in = i;
		this.s = sc;
	}

	public void run() {
        boolean asignado = false;
        while (!asignado && !s.isClosed()) {
            try {
				out.writeObject(Servidor.getSalasEnviar());
	            out.flush();
	            
	            // Recibo el número de sala seleccionado por el cliente
	            int numeroSalaSeleccionada = in.readInt();
	            // Intento asignar al jugador a la sala seleccionada
	        	if (numeroSalaSeleccionada >= 1 && numeroSalaSeleccionada <= Servidor.getSalasDeJuego().size()) {
	                SalaDeJuego salaSeleccionada = Servidor.getSalasDeJuego().get(numeroSalaSeleccionada - 1);

	                if (!salaSeleccionada.isFull()) {
	                    salaSeleccionada.addJugador(s,out,in);
	                    if (salaSeleccionada.getJugadores().size() == 2) {
	                    	new Thread(salaSeleccionada).start();
	                    }
	                    
	                    System.out.println("Jugador asignado a la Sala " + numeroSalaSeleccionada);
	                    asignado = true;
	                    out.writeBoolean(asignado);
	                    out.flush();
	                    break;
	                } else {
	                    System.out.println("La Sala " + numeroSalaSeleccionada + " está llena. El jugador no pudo ser asignado.");
	                    out.writeBoolean(asignado);
	                    out.flush();
	                }
	            } else {
	                System.out.println("Número de sala no válido. El jugador no pudo ser asignado.");
	                out.writeBoolean(asignado);
	                out.flush();
	            }
			} catch (IOException e) {
				e.printStackTrace();
			}
            
        }
	}
	
}
