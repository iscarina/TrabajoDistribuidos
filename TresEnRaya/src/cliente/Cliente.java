package cliente;
import java.io.*;
import java.net.*;
import java.util.*;

public class Cliente {
    public static void main(String[] args){
		try(Socket socket = new Socket("localhost", 12345);
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			Scanner scanner = new Scanner(System.in)) 
		{
			boolean asignacion = false;
			while(!asignacion) {
			
				List<String> salasJuego = (List<String>) in.readObject();
				
				// Muestra la lista de salas disponibles
		        System.out.println("Salas disponibles:");
				
		        for (int i = 0; i < salasJuego.size(); i++) {
		            String s = salasJuego.get(i);
		            System.out.println(i + 1 + ". " + s + " - Jugadores: " + "" + "/2");
		        }
		        
		        // Permite al usuario seleccionar una sala
	            System.out.print("Seleccione una sala (1-" + salasJuego.size() + "): ");
	            int numeroSala = scanner.nextInt();
	
	            // Envía el número de sala seleccionada al servidor
	            out.writeInt(numeroSala);
	            out.flush();
	            
	            // Recibe la respuesta del servidor
                asignacion = in.readBoolean();
                if (!asignacion) {
                    // Si la asignación fue exitosa, salir del bucle
                	System.out.println("La sala que has seleccionado esta llena o es incorrecta");
                }
                System.out.println(asignacion);
            
			}
			System.out.println("Estas en la sala");
			
	        
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
    }
}