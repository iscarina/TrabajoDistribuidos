package servidor;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Servidor {
    public static List<SalaDeJuego> salas = new ArrayList<>();
    public static List<String> enviarSalas = new ArrayList<>();
    
    public static void main(String[] args){
    	ExecutorService pool = Executors.newCachedThreadPool();
		try(ServerSocket serverSocket = new ServerSocket(12345);) {
	        // Creo 7 salas al inicio
	        for (int i = 1; i <= 7; i++) {
	        	salas.add(new SalaDeJuego("Sala " + i));
	        	enviarSalas.add("Sala" + i);
	        }

	        while (true) {
	        	Socket socketJugador = null;
	        	ObjectOutputStream out = null;
	        	ObjectInputStream in = null;
	        	try {
	        		socketJugador = serverSocket.accept();
	        		out = new ObjectOutputStream(socketJugador.getOutputStream());
	        		in = new ObjectInputStream(socketJugador.getInputStream());
	        		
	        		AsignarSala as = new AsignarSala(out, in, socketJugador);
	        		
	        		pool.submit(as);
	        		
	        	} catch(IOException e) {
	        		e.printStackTrace();
	        	}
	        	
	        }
	        
		} catch (IOException e) {
			e.printStackTrace();
		} 
    }
    
    public static synchronized List<String> getSalasEnviar() {
    	return enviarSalas;
    }
    
    public static synchronized List<SalaDeJuego> getSalasDeJuego() {
    	return salas;
    }
    
}