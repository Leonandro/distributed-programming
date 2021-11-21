package br.ufrn.imd.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class client {

	public static void main(String[] args) throws UnknownHostException, IOException {
		// TODO Auto-generated method stub
		Scanner sc=new Scanner(System.in);  
		Socket server = null;
		server = new Socket("localhost", 4443);
		PrintWriter serverOutput = new PrintWriter(server.getOutputStream(), true);
		BufferedReader serverInput = new BufferedReader(new InputStreamReader(server.getInputStream()));
		String msg;
		
		serverOutput.println("MILK, RICE, PEPPER");
		serverOutput.flush();
		System.out.println(serverInput.readLine());
		serverOutput.println("RICE, RICE, PEPPER");
		serverOutput.flush();
		System.out.println(serverInput.readLine());
		serverOutput.println("PEPPER, RICE, PEPPER");
		serverOutput.flush();
		System.out.println(serverInput.readLine());
		msg = sc.nextLine();
		serverOutput.println("END");
		serverOutput.flush();
		
//		while(true) {
//			System.out.println("Type");
//			msg = sc.nextLine();
//			serverOutput.println(msg);
//			System.out.println(serverInput.readLine());
//		}
	
	}

}
