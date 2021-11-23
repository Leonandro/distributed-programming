package br.ufrn.imd.loadbalancer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class TCPLoadBalancer {

	

	public static void main(String[] args) throws IOException  {
		// TODO Auto-generated method stub
		Socket clientSocket;
		Socket firstServerSocket;
		ServerSocket serverSocket;
	    PrintWriter firstServerOutput, clientOutput;
	    BufferedReader in, firstServerInput;
	    
	    
	    serverSocket = new ServerSocket(4443);
	    AtomicIntegerArray serversStatus = new AtomicIntegerArray (2);
	    serversStatus.set(0, 1);
	    serversStatus.set(1, 1);
		int numberOfConnections = 0;
	    
		System.out.println("[INFO]: Load Balancer initialized on port 4443");
	    
	    while (true) {
	    	try {
		    	System.out.println("[INFO]: Waiting for one connection");
		    	clientSocket = serverSocket.accept();
		    	new Thread (new TCPDispatcher(clientSocket, (numberOfConnections % 2) )).start();
		    	numberOfConnections++;
	    	}catch (Exception e) {
	    		System.err.println("[ERROR]: Fail to listen or to dispatch the request");
	    	}
	    }
	}

}
