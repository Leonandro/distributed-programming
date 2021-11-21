package br.ufrn.imd.database;

import br.ufrn.imd.server.TCPServer;

public class TCPDatabaseApplication {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TCPDatabase db = new TCPDatabase(Integer.parseInt(args[0]), Boolean.parseBoolean(args[1]));
		db.start();
	}

}
