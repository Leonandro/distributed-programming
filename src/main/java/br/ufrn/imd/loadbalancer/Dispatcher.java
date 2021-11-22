package br.ufrn.imd.loadbalancer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class Dispatcher implements Runnable {
	protected Socket clientSocket;
	protected Socket  server;
	protected int [] serversPorts;
	protected PrintWriter  serverOutput;
	protected BufferedReader serverInput;
	protected BufferedReader  clientInput;
	protected PrintWriter  clientOutput;
	protected int serverIndex;
	protected AtomicIntegerArray serversStatus;
	protected int roundRobinInitialServer;
	
	
	public Dispatcher(Socket incomingSocket, int roundRobinInit) throws UnknownHostException, IOException {
		this.clientSocket = incomingSocket;
		this.roundRobinInitialServer = roundRobinInit;
		this.serverIndex = 0;
		this.server = null;
		this.serversPorts = new int [2];
		this.serverInput = null;
		this.serverOutput =  null;
		this.serversStatus = null;
		
		this.serversPorts[0] = 4444;
		this.serversPorts[1] = 4445;
		
		this.findFreeServer();
		
//		this.servers[0] = new Socket ("127.0.0.1", 4444);
//		this.serversInputs[0]= new BufferedReader(new InputStreamReader(this.servers[0].getInputStream()));
//		this.serversOutputs[0] =  new PrintWriter(this.servers[0].getOutputStream(), true);
//		
//		this.servers[1] = new Socket ("127.0.0.1", 4445);
//		this.serversInputs[1]= new BufferedReader(new InputStreamReader(this.servers[1].getInputStream()));
//		this.serversOutputs[1] =  new PrintWriter(this.servers[1].getOutputStream(), true);
		
		//this.serversOutputs[0].println("TCFLB");
		System.out.println("[INFO]: Dispatcher initialized");
	}
	
	private void findFreeServer() {
		int freeServerPort = -1;
		
		int roundRobinIndex = this.roundRobinInitialServer;
		int index = this.roundRobinInitialServer;
		boolean isNotConnected = true;
		while(isNotConnected) {
			if(true) {
				try {
	
					this.server = new Socket ("127.0.0.1", this.serversPorts[index]);
					this.serverInput = new BufferedReader(new InputStreamReader(this.server.getInputStream()));
					this.serverOutput =  new PrintWriter(this.server.getOutputStream(), true);
					//serversList.set(i, 0);
					isNotConnected = false;
				
					System.out.println("[INFO]: Found a free server on index: " + this.serverIndex);
					return;
					
				} catch (Exception e) {
					
				}
			}
			roundRobinIndex++;
			index = roundRobinIndex % 2;
			
		}
		
		//return false;
	}
	
	public void run() {
		// TODO Auto-generated method stub
		
		
		
		if(this.serverIndex != -1) {
			try {
				clientOutput = new PrintWriter(clientSocket.getOutputStream(), true);
				clientInput = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				
				String msg = "DISPATCHER, INIT";
				String serverResponse = "";
				boolean hasData = true;
				while(hasData) {
					msg = clientInput.readLine();
					
					
					if(!msg.equalsIgnoreCase("END") && (msg != null)) {
						System.out.println("[INFO]: Dispatching this data " + msg + " to the server");
						this.serverOutput.println(msg);
						this.serverOutput.flush();
						serverResponse = this.serverInput.readLine();
						clientOutput.println(serverResponse);
						clientOutput.flush();
						
					}
					else {
						hasData = false;
						System.out.println("[INFO]: Ending the connection");
					}
					
					
					
				}
				
				clientOutput.close();
				clientInput.close();
				this.clientSocket.close();
				this.serverOutput.close();
				this.serverInput.close();
				
				
				
			}	
			catch (Exception e) {
				//System.err.println("[ERROR]: Fail to set/maintain the channel between client and server");
				try {
					clientOutput.close();
					clientInput.close();
					this.clientSocket.close();
				}catch (Exception ee) {
					System.err.println("[ERROR]: Unnable to close conection with the client");
				}
			}
		}
		
		System.out.println("[INFO]: Closing the dispatcher");
	}

}
