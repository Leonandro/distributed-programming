package br.ufrn.imd.loadbalancer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class HTTPLoadBalancer {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Socket clientSocket;
		ServerSocket serverSocket;
	
	    
	    
	    serverSocket = new ServerSocket(4443);
		int numberOfConnections = 0;
	    
		System.out.println("[INFO]: Load Balancer initialized on port 4443");
	    
	    while (true) {
	    	try {
		    	System.out.println("[INFO]: Waiting for one connection");
		    	clientSocket = serverSocket.accept();
		    	new Thread (new HTTPDispatcher(clientSocket, (numberOfConnections % 2) )).start();
		    	numberOfConnections++;
	    	}catch (Exception e) {
	    		System.err.println("[ERROR]: Fail to listen or to dispatch the request");
	    	}
	    }
	}

}
