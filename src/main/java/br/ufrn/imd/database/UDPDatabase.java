package br.ufrn.imd.database;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class UDPDatabase extends Thread {
	private DatagramSocket serverSocket;
    private boolean isConnected;
    private byte[] buf = new byte[1024];
    private boolean isPrimary;
    private ArrayList <String []> database;
	
	public UDPDatabase (int portNumber, boolean isPrimary) {
		this.database = new ArrayList <String[]> ();
		this.isPrimary = isPrimary;
		try {
			this.serverSocket = new DatagramSocket(portNumber);
			System.out.println("[INFO]: Database Socket initialized on port: " + portNumber);
		}catch (Exception e) {
			System.err.println("[ERROR]: Unable to initialize the socket on the port: " + portNumber);
		}
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
			 if(!receivedMsg.equalsIgnoreCase("DB-RESPONSE")) {
				 System.out.println("[INFO]: Storing the data - " + receivedMsg);
				 try {
					this.serverSocket.send(new DatagramPacket("DB-RESPONSE".getBytes(), "DB-RESPONSE".getBytes().length, packet.getAddress(), packet.getPort()));
				 } catch (Exception e) {
					// TODO Auto-generated catch block
					System.err.println("[ERROR]: Fail to respond the save request");
				 }
				 this.database.add(receivedMsg.split(","));
				 
				 if(this.isPrimary) {
					this.sendToSecondaryDatabase(receivedMsg);
				 }
			 }
			
		
		}
	}
	
	public void sendToSecondaryDatabase (String data) {
		try {
			 byte [] returnMsg = data.getBytes();
			 InetAddress address = InetAddress.getLocalHost();
			 DatagramPacket dataPakcet = new DatagramPacket(returnMsg, returnMsg.length, address, 4481);
			 this.serverSocket.send(dataPakcet);
		}catch (Exception e) {
			System.err.println("[ERROR]: Unnable to connect to the secondary database");
		}
	}

}
