package servidor;

import java.io.*;
import java.net.*;
import java.util.*;

import logica.TresEnRaya;

public class SalaDeJuego implements Runnable,Serializable{

	private String nombreSala;
    private List<Socket> jugadores;
    private Map<Socket, ObjectOutputStream> outputs;
    private Map<Socket, ObjectInputStream> inputs;
    
    public SalaDeJuego(String nomSala) {
        this.nombreSala = nomSala;
        this.jugadores = new ArrayList<>();
        this.outputs = new HashMap<>();
        this.inputs = new HashMap<>();
    }

    public synchronized void addJugador(Socket socketJugador, ObjectOutputStream out, ObjectInputStream in) {
        jugadores.add(socketJugador);
    	outputs.put(socketJugador, out);
    	inputs.put(socketJugador, in);
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
    
    private int pedirPosicion(Socket jugador) {
            try {
            	ObjectInputStream in = inputs.get(jugador);
				return in.readInt();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return -1;
			}
    }
    
    private void mostrarGanador(TresEnRaya tr) {
    	
    	ArrayList<String> g = tr.ganador();
    	
    	if(g.get(1).equals("1")) {
    		if(g.get(0).equalsIgnoreCase("X")){
    			enviarMensaje((Object)"¡HAS GANADO por linea!", this.jugadores.get(0));
    			enviarMensaje((Object)"PERDEDOR. Te han hecho linea", this.jugadores.get(1));
    		}else {
    			enviarMensaje((Object)"¡HAS GANADO por linea!", this.jugadores.get(1));
    			enviarMensaje((Object)"PERDEDOR. Te han hecho linea", this.jugadores.get(0));
    		}
    	}
    	else if(g.get(1).equals("2")) {
    		if(g.get(0).equalsIgnoreCase("X")){
    			enviarMensaje((Object)"¡HAS GANADO por columna!", this.jugadores.get(0));
    			enviarMensaje((Object)"PERDEDOR. Te han hecho columna", this.jugadores.get(1));
    		}else {
    			enviarMensaje((Object)"¡HAS GANADO por columna!", this.jugadores.get(1));
    			enviarMensaje((Object)"PERDEDOR. Te han hecho columna", this.jugadores.get(0));
    		}
    	}
    	else if(g.get(1).equals("3")) {
    		if(g.get(0).equalsIgnoreCase("X")){
    			enviarMensaje((Object)"¡HAS GANADO por diagonal!", this.jugadores.get(0));
    			enviarMensaje((Object)"PERDEDOR. Te han hecho diagonal", this.jugadores.get(1));
    		}else {
    			enviarMensaje((Object)"¡HAS GANADO por diagonal!", this.jugadores.get(1));
    			enviarMensaje((Object)"PERDEDOR. Te han hecho diagonal", this.jugadores.get(0));
    		}
    	}
    	else {
    		enviarMensaje((Object)"HABEIS EMPATADO", this.jugadores.get(0));
			enviarMensaje((Object)"HABEIS EMPATADO", this.jugadores.get(1));
    	}
    }
    
    @Override
    public void run() {
    	System.out.println("La sala " + nombreSala + " ha comenzado.");
    	
    	try {
    		enviarMensaje((Object)"¡Que comience el juego! Seras las X", this.jugadores.get(0));
            enviarMensaje((Object)"¡Que comience el juego! Seras los O", this.jugadores.get(1));
            
        	TresEnRaya tr = new TresEnRaya();
        	 
        	 while (!tr.finPartida()) {
        		 if(tr.getTurno()) {
        			 enviarMensaje((Object)"¡Te toca jugar!", this.jugadores.get(0));
        			 enviarMensaje((Object)"Esperando tu turno ...", this.jugadores.get(1));
        			 enviarMensaje(true, this.jugadores.get(0));
        			 enviarMensaje(false, this.jugadores.get(1));
        			 
        			 juegaTurno(tr, this.jugadores.get(0));
        		 }
        		 else {
        			 enviarMensaje((Object)"¡Te toca jugar!", this.jugadores.get(1));
        			 enviarMensaje((Object)"Esperando tu turno ...", this.jugadores.get(0));
        			 enviarMensaje(true, this.jugadores.get(1));
        			 enviarMensaje(false, this.jugadores.get(0));
        			 
        			 juegaTurno(tr, this.jugadores.get(1));
        		 }
        		 enviarMensaje(tr.finPartida(), this.jugadores.get(0));
    			 enviarMensaje(tr.finPartida(), this.jugadores.get(1));
        	 }
        	 
             //Muestra el tablero a ambos jugadores
        	 enviarMensaje(tr.getTablero(),this.jugadores.get(0));
        	 enviarMensaje(tr.getTablero(),this.jugadores.get(1));
             
        	 this.mostrarGanador(tr);
             
             enviarMensaje(true,this.jugadores.get(0));
             enviarMensaje(true,this.jugadores.get(1));
    	}  finally {
            // Cierre de recursos en el bloque finally
            for (Socket socket : jugadores) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            for (ObjectOutputStream out : outputs.values()) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            for (ObjectInputStream in : inputs.values()) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
         
    
    private void juegaTurno(TresEnRaya tr, Socket socketJugador) {
    	int fila, columna;
        boolean posValida, correcto;
 
        do {
        	
            enviarMensaje(tr.getTablero(),socketJugador);
        	tr.getTablero();
 
            correcto = false;
            
            enviarMensaje("Dame la fila: ",socketJugador);
            fila = pedirPosicion(socketJugador);
            
            enviarMensaje("Dame la columna: ",socketJugador);
            columna = pedirPosicion(socketJugador);
 
            //Validamos la posicion
            posValida = tr.validarPosicion(fila, columna);
 
            //Si es valido, comprobamos que no haya ninguna marca
            if (posValida) {
                //Si no hay marca, significa que es correcto
                if (!tr.hayValorPosicion(fila, columna)) {
                	enviarMensaje(true,socketJugador);
                    correcto = true;
                } else {
                	enviarMensaje(false,socketJugador);
                    enviarMensaje("Ya hay una marca en esa posicion",socketJugador);
                }
            } else {
            	enviarMensaje(false,socketJugador);
                enviarMensaje("La posicion no es valida",socketJugador);
            }
 
            //Mientras no sea correcto, no salgo
        } while (!correcto);
 
        //depende del turno, inserta un simbolo u otro
        tr.insertarEn(fila, columna);
 
        tr.cambiaTurno();

    }
 
    

}
