package br.ufrn.imd.loadbalancer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UDPDispatcher implements Runnable {
	private byte [] data;
	private DatagramPacket clientPacket;
	private DatagramSocket server, loadBalancerSocket;
	private int [] serversPorts;
	private int roundRobinInitialServer;
	
	public UDPDispatcher(DatagramPacket incomingPacket, int roundRobinInit, DatagramSocket lbSocket) throws SocketException {
		this.clientPacket = incomingPacket;
		this.data = new byte [incomingPacket.getLength()];
		this.roundRobinInitialServer = roundRobinInit;
		this.serversPorts = new int [2];
		this.serversPorts[0] = 4444;
		this.serversPorts[1] = 4445;
		
		
		this.server = new DatagramSocket();
		this.loadBalancerSocket = lbSocket;
	}
	
	private void sendPacketToAFreeServer() throws UnknownHostException, SocketException {
		int freeServerPort = -1;
		InetAddress localhost = InetAddress.getByName("localhost");
		int roundRobinIndex = this.roundRobinInitialServer;
		int index = this.roundRobinInitialServer;
		boolean isNotConnected = true;
		DatagramPacket packetToSend, checkAlivePacket;
		byte [] signalMsg =  "DISPATCHER-CONNECT".getBytes();
		byte [] signalResponseMsg = new byte [1024];
		this.data = this.clientPacket.getData();
		this.server.setSoTimeout(1);
		while(isNotConnected) {
			if(true) {
				try {
					
					//-----[Check if the server is alive and, free]------
					checkAlivePacket = new DatagramPacket(signalMsg, signalMsg.length, localhost, this.serversPorts[index]);
					this.server.send(checkAlivePacket);
					checkAlivePacket = new DatagramPacket(signalResponseMsg, signalResponseMsg.length);
					this.server.receive(checkAlivePacket);
					 
					//------[Send the packet]-------
					packetToSend = new DatagramPacket(this.data, this.data.length, localhost, this.serversPorts[index]);
					this.server.send(packetToSend);
					isNotConnected = false;
					//System.out.println("[INFO-DISPATCHER]: Free server on the index : " + index);
					return;
					
				} catch (Exception e) {
					System.err.println("[INFO-DISPATCHER]: Server on the index " + index + " Do not respond");
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
			this.sendPacketToAFreeServer();
			this.server.setSoTimeout(0);
			byte [] returnMsg = new byte [1024];
			DatagramPacket serverResponse = new DatagramPacket(returnMsg, returnMsg.length);
			this.server.receive(serverResponse);			
			
			InetAddress clientAddress = clientPacket.getAddress();
            int clientPort = clientPacket.getPort();
           
            DatagramPacket returnPacket = new DatagramPacket(serverResponse.getData(), serverResponse.getData().length, clientPacket.getAddress(), clientPacket.getPort());
            
            //server.send(returnPacket);
            
            //THIS ABERRATION IS JUST BECAUSE IT'S THE ONLY WAY THAT I FOUND FOR JMETER ACCEPT THE RESPONSE
            //THE NORMAL SITUATION HERE IS SEND USING THE SERVERSOCKET FROM THE DISPATCHER
            this.loadBalancerSocket.send(returnPacket);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.err.println("[ERROR]: Unnable to send the packet to the server");
		}
		
	}

}
