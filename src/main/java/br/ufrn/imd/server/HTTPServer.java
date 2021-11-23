package br.ufrn.imd.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class HTTPServer extends Thread {
	private ServerSocket serverSocket;
	private Socket clientSocket;
	private BufferedReader in;
	private DataOutputStream out;
	boolean isConnected;
	
	public HTTPServer (int portNumber) {
		this.isConnected = false;
		this.in = null;
		this.out = null;
		try {
			this.serverSocket = new ServerSocket(portNumber);
			System.out.println("[INFO]: Socket initialized on port: " + portNumber);
		}catch (Exception e) {
			System.err.println("[ERROR]: Unable to initialize the socket on the port: " + portNumber);
		}
	}
	
	private void sendResponse (int statusCode, String responseString) {
		String statusLine;

		String serverHeader = "Server: WebServer\r\n";

		String contentTypeHeader = "Content-Type: text/plain\r\n";
		
		try {
			 
			 if(statusCode == 200) {
				 System.out.println("[INFO]: Sending success request");
				 statusLine = "HTTP/1.0 200 OK" + "\r\n";
				 String contentLengthHeader = "Content-Length: " + responseString.length() + "\r\n";
				 out.writeBytes(statusLine);
				 out.writeBytes(serverHeader);
				 out.writeBytes(contentTypeHeader);
				 out.writeBytes(contentLengthHeader);
				 out.writeBytes("\r\n");
				 out.writeBytes(responseString);
			 }
			 
			 else if (statusCode == 405) {
				statusLine = "HTTP/1.0 405 Method Not Allowed" + "\r\n";
				out.writeBytes(statusLine);
				out.writeBytes("\r\n");

			} else {
				statusLine = "HTTP/1.0 404 Not Found" + "\r\n";
				out.writeBytes(statusLine);
				out.writeBytes("\r\n");
			}
			
			
		}catch (Exception e) {
			System.err.println("[ERROR]: Fail to send the response" + responseString);
		}
		
	}
	
	public String readBody(){
		ArrayList<String> clientRequest = new ArrayList<String> ();
		try {
			String requestLine = "";
			boolean hasData = true;
			while(hasData) {
				System.out.println("First step of the while");
				requestLine = this.in.readLine();
				System.out.println(requestLine);
				
				if(requestLine.equals("\r\n")) {
					char [] response = new char [20];
					this.in.read(response);
					String responseString = String.valueOf(response);
					clientRequest.add("\r\n");
					clientRequest.add(responseString);
					hasData = false;
					return responseString;
				}
				else {
					System.out.println(requestLine);
				}
				
				
			}
		}catch (Exception e) {
		
		}
		return null;
	}
	
	public void run () {
		while(true) {
			try {
				this.clientSocket = this.serverSocket.accept();
				this.in = new BufferedReader(new InputStreamReader( this.clientSocket.getInputStream() )  );
				this.out = new DataOutputStream(this.clientSocket.getOutputStream());
				boolean ff = true;
				System.out.println("Conection on");
				while(ff) {
					//String headerLine = in.readLine();
					//StringTokenizer tokenizer = new StringTokenizer(headerLine);
					
					System.out.println("on While");
					String [] request = this.read(this.in);
					//System.out.println(request[0]);
					System.out.println("after read");
					StringTokenizer tokenizer = new StringTokenizer(request[0]);
					String httpMethod = tokenizer.nextToken();
					
					String body = request[request.length -1];
					System.out.println("BODY:" + body + "METHOD:" + httpMethod);
					if(!body.equalsIgnoreCase("END")) {
						System.out.println("Received connection with method: " + httpMethod + " and body: " + body);
						
						if(httpMethod.equals("POST")) {
							this.sendResponse(200, "REQUEST-ACCEPTED");
						}
						
						else {
							this.sendResponse(405, "METHOD-NOT-ALLOWED");
						}
					}
					else {
						ff = false;
						System.out.println("[INFO]: Closing the connection");
					}
					
				
				}
				out.close();
				in.close();
				this.clientSocket.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public ArrayList<String> readMsg(BufferedReader stream){
		ArrayList<String> clientRequest = new ArrayList<String> ();
		try {
			String requestLine = "";
			boolean hasData = true;
			while(hasData) {
				System.out.println("------------");
				requestLine = stream.readLine();
				System.out.println(requestLine);
				System.out.println("------------");
				if(requestLine.isEmpty()) {
					char [] response = new char [20];
					stream.read(response);
					String responseString = String.valueOf(response);
					clientRequest.add("\r\n");
					clientRequest.add(responseString);
					hasData = false;
					return clientRequest;
				}
				else {
					clientRequest.add(requestLine);
				}
				
				
			}
		}catch (Exception e) {
		
		}
		return null;
	}
	
	public String [] read(BufferedReader inputStream) throws IOException {
	    StringBuilder result = new StringBuilder();
	    int buddy = inputStream.read();
	    //System.out.println("Is ready to read? " + inputStream.ready());
	    while ( inputStream.ready() ) {
	    	
	    	//System.out.println("------[" + (char)buddy + "]------");
	        result.append((char)buddy);
	        buddy = inputStream.read();

	    }
	    return result.toString().split("\r\n");
	}
}
