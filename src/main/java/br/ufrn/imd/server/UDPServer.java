package br.ufrn.imd.server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPServer extends Thread {
	private DatagramSocket serverSocket;
    private boolean isConnected;
    private byte[] buf = new byte[1024];
	
	public UDPServer (int portNumber) {
		try {
			this.serverSocket = new DatagramSocket(portNumber);
			System.out.println("[INFO]: Socket initialized on port: " + portNumber);
		}catch (Exception e) {
			System.err.println("[ERROR]: Unable to initialize the socket on the port: " + portNumber);
		}
	}
	
	public void run () {
		
		while(true) {
			 DatagramPacket packet = new DatagramPacket(buf, buf.length);
			 
			 try {
				 this.serverSocket.receive(packet);
			 }catch (Exception e){
				 System.err.println("[ERROR]: Unnable to receive the packet");
			 }
			 
			 String receivedMsg = new String(packet.getData()).trim();
			 
			 if(!receivedMsg.equalsIgnoreCase("END")) {
				 System.out.println("[INFO]: received this data: " + receivedMsg);
				 
				 InetAddress address = packet.getAddress();
	             int port = packet.getPort();
	             
	             byte [] returnMsg = new byte [1024];
	             returnMsg = "REQUEST-ACCEPTED".getBytes();
	             DatagramPacket returnPacket = new DatagramPacket(returnMsg, returnMsg.length, address, port);
	             
	            // packet = new DatagramPacket(returnMsg, returnMsg.length, address, port);
	             try {
	            	 this.serverSocket.send(returnPacket);
	             }catch (Exception e) {
	            	System.err.println("[ERROR]: Unnable to Send Back the packet"); 
	             }
			 }
		
		}
	}

}
