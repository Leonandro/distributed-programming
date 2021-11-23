package br.ufrn.imd.database;

public class UDPDatabaseApplication {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		UDPDatabase db = new UDPDatabase(Integer.parseInt(args[0]), Boolean.parseBoolean(args[1]));
		db.start();
	}

}
