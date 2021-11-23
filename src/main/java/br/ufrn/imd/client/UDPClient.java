package br.ufrn.imd.client;
import java.io.IOException;
import java.net.*;

//import udpServer.EchoClient;

public class UDPClient {

	

	public static void main(String[] args) throws SocketException, UnknownHostException, IOException {
		// TODO Auto-generated method stub
		EchoClient client = new EchoClient();
		String received = "";
		received = client.sendEcho("HIE");
		System.out.println(received);
		received = client.sendEcho("ITS THE CLIENT");
		System.out.println(received);
		received = client.sendEcho("COMMON CUH");
		System.out.println(received);
		received = client.sendEcho("CHEEESE");
		System.out.println(received);
		client.close();
		
	}

}
