package br.ufrn.imd.loadbalancer;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPLoadBalancer {

	public static void main(String[] args) throws SocketException {
		// TODO Auto-generated method stub
		byte[] buf = new byte[1024];
		DatagramPacket incomingPacket = new DatagramPacket(buf, buf.length);
		DatagramSocket serverSocket = new DatagramSocket(4443);
		int numberOfConnections = 0;
		System.out.println("[INFO]: Load Balancer initialized on port 4443");
		while(true) {
			try {
				serverSocket.receive(incomingPacket);
				new Thread (new UDPDispatcher(incomingPacket, (numberOfConnections % 2), serverSocket)).start();
				//System.out.println("[INFO-LB]: Send: " + new String(incomingPacket.getData(), 0, incomingPacket.getData().length));
		    	numberOfConnections++;
		    	//serverSocket.send(new DatagramPacket(buf, buf.length, incomingPacket.getAddress(), incomingPacket.getPort()));
		    	buf = new byte [1024];
		    	incomingPacket = new DatagramPacket(buf, buf.length);
		    	
			}catch (Exception e) {
				
			}
		}
		

	}

}
