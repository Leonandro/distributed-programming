package br.ufrn.imd.loadbalancer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class HTTPDispatcher implements Runnable {
	private Socket clientSocket, serverSocket;
	private BufferedReader clientInput, serverInput;
	private DataOutputStream clientOutput, serverOutput;
	private int [] serversPorts;
	private int serverIndex;
	private int roundRobinInitialServer;
	
	public HTTPDispatcher(Socket incomingSocket, int roundRobinInit) {
		this.clientSocket = incomingSocket;
		this.roundRobinInitialServer = roundRobinInit;
		this.serverIndex = 0;
		this.serverSocket = null;
		this.serversPorts = new int [2];
		this.serverInput = null;
		this.serverOutput =  null;
//		this.serversStatus = null;
		this.clientInput = null;
		this.clientOutput = null;
		
		this.serversPorts[0] = 4444;
		this.serversPorts[1] = 4445;
		
		this.findFreeServer();
		System.out.println("[INFO-DISPATCHER]: Dispatcher initialized");
	}
	
	private void findFreeServer() {
		int freeServerPort = -1;
		
		int roundRobinIndex = this.roundRobinInitialServer;
		int index = this.roundRobinInitialServer;
		boolean isNotConnected = true;
		while(isNotConnected) {
			if(true) {
				try {
	
					this.serverSocket = new Socket ("127.0.0.1", this.serversPorts[index]);
					this.serverInput = new BufferedReader(new InputStreamReader(this.serverSocket.getInputStream()));
					this.serverOutput =  new DataOutputStream(this.serverSocket.getOutputStream());
					//serversList.set(i, 0);
					isNotConnected = false;
				
					System.out.println("[INFO]: Found a free server on index: " + index);
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
		try {
//			this.serverInput = new BufferedReader(new InputStreamReader(this.serverSocket.getInputStream()));
//			this.serverOutput =  new DataOutputStream(this.serverSocket.getOutputStream());
			this.clientInput = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
			this.clientOutput =  new DataOutputStream(this.clientSocket.getOutputStream());
			
			System.out.println("Reading the msg from client --------------");
			String receivedRequestFromClient [] = this.read(this.clientInput);
			this.sendMsg(receivedRequestFromClient, this.serverOutput);
			this.serverOutput.flush();
			
			System.out.println("Expecting the response from server");
			String [] receivedRequestFromServer = this.read(this.serverInput);
			this.sendMsg(receivedRequestFromServer, this.clientOutput);
			this.clientOutput.flush();
			
//			this.serverOutput.writeBytes("POST / HTTP/1.1" + "\r\n");
//			this.serverOutput.writeBytes("\r\n");
//			this.serverOutput.writeBytes("END");
			
			
//			this.clientOutput.writeBytes("HTTP/1.0 200 OK" + "\r\n");
//			this.clientOutput.writeBytes("\r\n");
//			this.clientOutput.writeBytes("thurubangos");
			
			
			this.clientOutput.close();
			this.clientInput.close();
			this.clientSocket.close();
			
			
			this.serverOutput.close();
			this.serverInput.close();
			this.serverSocket.close();
			
			
			
		}catch(Exception e) {
			System.err.println("[ERROR]: FAil man");
			e.printStackTrace();
		}
	}
	
	public String [] read(BufferedReader inputStream) throws IOException {
	    StringBuilder result = new StringBuilder();
	    char buddy = 0;
	    while ( inputStream.ready() ) {
	    	buddy = (char) inputStream.read();
	        result.append(buddy);

	    }
	    return result.toString().split("\r\n");
	}
	
	public ArrayList<String> readMsg(BufferedReader stream){
		ArrayList<String> clientRequest = new ArrayList<String> ();
		try {
			String requestLine = "";
			boolean hasData = true;
			while(hasData) {
				requestLine = stream.readLine();
				
				if(requestLine.isEmpty()) {
					char [] response = new char [20];
					stream.read(response);
					String responseString = String.valueOf(response);
					clientRequest.add("\r\n");
					clientRequest.add(responseString);
					hasData = false;
					return clientRequest;
				}
				else {
					clientRequest.add(requestLine + "\r\n");
				}
				
				
			}
		}catch (Exception e) {
		
		}
		return null;
	}
	
	public void sendMsg(String [] request, DataOutputStream stream) throws IOException {
		System.out.println("Tentando enviar a msg - " + request[0]);
		//boolean isBody = false;
		for(String requestLine : request) {
			if(request.equals("")) {
				stream.writeBytes("\r\n");
			}else {
				stream.writeBytes(requestLine + "\r\n");
				//System.out.print(requestLine + "\r\n");
			}
			
		}
		//System.out.println("\r\n");
		//stream.writeBytes(request.get(request.size() -1));
		System.out.println("terminei de enviar a msg");
	}

}
