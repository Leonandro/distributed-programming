package br.ufrn.imd.server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPServer extends Thread {
	private DatagramSocket serverSocket, databaseSocket;
    private boolean isConnected;
    private byte[] buf = new byte[1024];
	
	public UDPServer (int portNumber) {
			
		try {
			this.serverSocket = new DatagramSocket(portNumber);
			
			this.databaseSocket = new DatagramSocket(9000 + (portNumber % 4445));
			this.databaseSocket.setSoTimeout(1);
			System.out.println("[INFO]: Socket initialized on port: " + portNumber);
		}catch (Exception e) {
			System.err.println("[ERROR]: Unable to initialize the socket on the port: " + portNumber);
		}
	}
	
	public boolean sendToDatabase (String data) {
		int n = 3;
		
		while(n>0) {
			try {
				 byte [] returnMsg = data.getBytes();
				 InetAddress address = InetAddress.getLocalHost();
				 DatagramPacket dataPakcet = new DatagramPacket(returnMsg, returnMsg.length, address, 4480);
				 this.databaseSocket.send(dataPakcet);
				 this.databaseSocket.receive(dataPakcet);
				 return true;
			}catch (Exception e) {
				
			}
			
			try {
				 byte [] returnMsg = data.getBytes();
				 InetAddress address = InetAddress.getLocalHost();
				 DatagramPacket dataPakcet = new DatagramPacket(returnMsg, returnMsg.length, address, 4481);
				 this.databaseSocket.send(dataPakcet);
				 this.databaseSocket.receive(dataPakcet);
				 return true;
			}catch (Exception e) {
				
			}
			n--;
		}
		System.err.println("[ERROR]: Unnable to store the data");
		return false;
	}
	
	public void run () {
		
		while(true) {
			 buf = new byte[1024];
			 DatagramPacket packet = new DatagramPacket(buf, buf.length);
			 
			 try {
				 this.serverSocket.receive(packet);
			 }catch (Exception e){
				 System.err.println("[ERROR]: Unnable to receive the packet");
			 }
			 
			 String receivedMsg = new String(packet.getData()).trim();
			 
			 if(!receivedMsg.equalsIgnoreCase("DISPATCHER-CONNECT")) {
				 System.out.println("[INFO]: received this data {" + receivedMsg + "} from 127.0.0.1:" + packet.getPort());
				 
				 boolean sendedToDB = this.sendToDatabase(receivedMsg);
				 
				 InetAddress address = packet.getAddress();
	             int port = packet.getPort();
	             
	             byte [] returnMsg = new byte [1024];
	             
	             if(sendedToDB) {
	            	 returnMsg = "REQUEST-STORED".getBytes();
	             }
	             else {
	            	 returnMsg = "REQUEST-ACCEPTED".getBytes();
	             }
	             DatagramPacket returnPacket = new DatagramPacket(returnMsg, returnMsg.length, address, port);
	             
	            // packet = new DatagramPacket(returnMsg, returnMsg.length, address, port);
	             try {
	            	 this.serverSocket.send(returnPacket);
	             }catch (Exception e) {
	            	System.err.println("[ERROR]: Unnable to Send Back the packet"); 
	             }
			 }
			 else {
				 
				 //System.out.println("[INFO]: Received alive packet");
				 InetAddress address = packet.getAddress();
	             int port = packet.getPort();
	             
	             byte [] returnMsg = new byte [1024];
	             returnMsg = "CONNECTION-ACCEPTED".getBytes();
	             DatagramPacket returnPacket = new DatagramPacket(returnMsg, returnMsg.length, address, port);
	             
	             try {
	            	 this.serverSocket.send(returnPacket);
	             }catch (Exception e) {
	            	System.err.println("[ERROR]: Unnable to Send Back the alive packet"); 
	             }
			 }
		
		}
		
		
	}

}
