package cliente;
import java.io.*;
import java.net.*;
import java.util.*;

import logica.TresEnRaya;

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
                	System.out.println("La sala que has seleccionado esta llena o es incorrecta");
                }
            
			}
			
			int numero;
			boolean juegoTerminado = false;
			//Leo mensaje bienvenida
			System.out.println(in.readObject().toString());
			while(!juegoTerminado){
				
				//Leo mensaje turno
				System.out.println(in.readObject().toString());
				
				if((boolean) in.readObject()) {
					boolean posValida = false;
					while(!posValida) {
						//Leo y muestro el tablero fila
						ArrayList<String> tablero = (ArrayList<String>) in.readObject();
						System.out.println(tablero.get(0) + tablero.get(1) + tablero.get(2));
						System.out.println(tablero.get(3) + tablero.get(4) + tablero.get(5));
						System.out.println(tablero.get(6) + tablero.get(7) + tablero.get(8));
						//Le pido fila
						System.out.println(in.readObject().toString());
						
						numero = 0;
						
						while (!scanner.hasNextInt()) {
				            System.out.println("Entrada no válida. Por favor, ingresa un número.");
				            scanner.next();
				        }
						//inserto fila
						numero = scanner.nextInt();
						out.writeInt(numero);
						out.flush();
						
						//Le pido columna
						System.out.println(in.readObject().toString());
						
						while (!scanner.hasNextInt()) {
				            System.out.println("Entrada no válida. Por favor, ingresa un número.");
				            scanner.next();
				        }
						
						//inserto columna
						numero = scanner.nextInt();
						out.writeInt(numero);
						out.flush();
						while(!(Integer.valueOf(numero) instanceof Integer)) {
							System.out.println("Escribe un numero entero: ");
							numero = scanner.nextInt();
						}
										
						posValida = (Boolean)in.readObject();
						if(!posValida) {
							System.out.println(in.readObject().toString());
						}
					}
					juegoTerminado = (Boolean)in.readObject();
				}
			}
			//Muestro como ha quedado el tablero final
			ArrayList<String> tablero = (ArrayList<String>) in.readObject();
			System.out.println(tablero.get(0) + tablero.get(1) + tablero.get(2));
			System.out.println(tablero.get(3) + tablero.get(4) + tablero.get(5));
			System.out.println(tablero.get(6) + tablero.get(7) + tablero.get(8));
			
			//Muestro el ganador
			System.out.println(in.readObject().toString());
	        
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
    }
}