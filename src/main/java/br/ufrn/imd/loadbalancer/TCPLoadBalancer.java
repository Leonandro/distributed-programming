package br.ufrn.imd.loadbalancer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPLoadBalancer {

	

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Socket clientSocket;
		Socket firstServerSocket;
		ServerSocket serverSocket;
	    PrintWriter firstServerOutput, clientOutput;
	    BufferedReader in, firstServerInput;
	    
	    
	    serverSocket = new ServerSocket(4443);
		
		System.out.println("[INFO]: Load Balancer initialized on port 4443");
	    
	    while (true) {
	    	clientSocket = serverSocket.accept();
	    	new Thread (new Dispatcher(clientSocket)).start();
	    	
	    	
	    }
	}

}
