package br.ufrn.imd.server;

public class UDPServerApplication {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		UDPServer server = new UDPServer(Integer.parseInt(args[0]));
		server.start();
	}

}
