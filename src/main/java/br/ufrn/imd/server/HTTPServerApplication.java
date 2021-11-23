package br.ufrn.imd.server;

public class HTTPServerApplication {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HTTPServer server = new HTTPServer(Integer.parseInt(args[0]));
		server.start();
	}

}
