package com.documentics.backend;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import java.net.InetAddress;
import java.net.NetworkInterface;

import java.net.SocketException;
import java.util.Enumeration;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
public class SocketServerThread  {

	static final int SocketServerPORT = 9999;
	int count = 0;
	//ServerSocket serverSocket;
	StringBuilder socketData= new StringBuilder();
	String message ;
	Socket client;
	private Socket clientSocket;
	int cnt;
	String result;
	boolean checkUpdate=true;

	public void setData(String data)
	{
		message = "TF-IDF :";
		message += data;

	}

	public String run() {
		try {
			//serverSocket = new ServerSocket(SocketServerPORT);
			String hostIp = "10.205.0.116";
			int portNumber = 9999;
			client = new Socket(hostIp, portNumber);
			BufferedWriter writer;
			BufferedReader reader;
			String line;
			String clientMsg="";

			writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

			writer.write(message);
			writer.newLine();
			writer.flush();
			writer.close();

			client = new Socket(hostIp, portNumber);

			reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
			line = reader.readLine();
			result = line;
			System.out.println(line);



		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			return result;	
		
	}


}

