package servidor;
import java.io.*;
import java.net.*;
import java.util.*;

public class Servidor {
    public static List<SalaDeJuego> salas = new ArrayList<>();
    public static List<String> enviarSalas = new ArrayList<>();
    
    public static void main(String[] args){
		try(ServerSocket serverSocket = new ServerSocket(12345);) {
	        // Creo 5 salas al inicio
	        for (int i = 1; i <= 5; i++) {
	        	salas.add(new SalaDeJuego("Sala " + i));
	        	enviarSalas.add("Sala" + i);
	        }

	        while (true) {
	        	try {
	        		Socket socketJugador = serverSocket.accept();
	        		ObjectOutputStream out = new ObjectOutputStream(socketJugador.getOutputStream());
	        		ObjectInputStream in = new ObjectInputStream(socketJugador.getInputStream());
	                boolean asignado = false;
	                while (!asignado) {
	                    //Le envio las salas al cliente
	                    out.writeObject(enviarSalas);
	                    out.flush();
	                    
	                    // Recibo el número de sala seleccionado por el cliente
	                    int numeroSalaSeleccionada = in.readInt();
	                    // Intento asignar al jugador a la sala seleccionada
	                	if (numeroSalaSeleccionada >= 1 && numeroSalaSeleccionada <= salas.size()) {
	                        SalaDeJuego salaSeleccionada = salas.get(numeroSalaSeleccionada - 1);

	                        if (!salaSeleccionada.isFull()) {
	                            salaSeleccionada.addJugador(socketJugador,out,in);
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
	                    
	                }
	        	} catch(IOException e) {
	        		e.printStackTrace();
	        	}
	        	
	        }
	        
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}