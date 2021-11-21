package br.ufrn.imd.server;

public class TCPServerApplication {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TCPServer server = new TCPServer(Integer.parseInt(args[0]));
		server.start();
		
		
		
	}

}
