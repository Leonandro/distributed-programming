package br.ufrn.imd.loadbalancer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Dispatcher implements Runnable {
	protected Socket clientSocket;
	protected Socket [] servers;
	protected PrintWriter [] serversOutputs;
	protected BufferedReader [] serversInputs;
	protected int serverIndex;
	
	
	public Dispatcher(Socket incomingSocket) throws UnknownHostException, IOException {
		this.clientSocket = incomingSocket;
		this.serverIndex = 0;
		this.servers = new Socket [2];
		this.serversInputs = new BufferedReader [2];
		this.serversOutputs = new PrintWriter [2];
		
		this.servers[0] = new Socket ("127.0.0.1", 4444);
		this.serversInputs[0]= new BufferedReader(new InputStreamReader(this.servers[0].getInputStream()));
		this.serversOutputs[0] =  new PrintWriter(this.servers[0].getOutputStream(), true);
		
//		this.servers[1] = new Socket ("127.0.0.1", 4445);
//		this.serversInputs[1]= new BufferedReader(new InputStreamReader(this.servers[1].getInputStream()));
//		this.serversOutputs[1] =  new PrintWriter(this.servers[1].getOutputStream(), true);
		
		//this.serversOutputs[0].println("TCFLB");
		System.out.println("[INFO]: Dispatcher initialized");
	}
	
	public void run() {
		// TODO Auto-generated method stub
		BufferedReader  clientInput;
		PrintWriter  clientOutput;
		System.out.println("[INFO]: The server is free on index: " + this.serverIndex);
		
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
						this.serversOutputs[this.serverIndex].println(msg);
						this.serversOutputs[this.serverIndex].flush();
						serverResponse = this.serversInputs[this.serverIndex].readLine();
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
				this.serversOutputs[this.serverIndex].close();
				this.serversInputs[this.serverIndex].close();
				
				
			}	
			catch (Exception e) {
				
			}
		}
		
		System.out.println("[INFO]: Closing the dispatcher");
	}

}
