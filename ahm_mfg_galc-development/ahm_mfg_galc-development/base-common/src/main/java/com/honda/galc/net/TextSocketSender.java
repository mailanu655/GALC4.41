package com.honda.galc.net;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;

import com.honda.galc.common.exception.ServiceInvocationException;


public class TextSocketSender extends SocketSender<String>{
	
	boolean isDurable = true;
	
	public TextSocketSender(String ip,int port) {
		super(new SocketClient(ip,port));
		this.isDurable = false;
	}
	
	public TextSocketSender(SocketClient socketClient) {
		super(socketClient);
	}
	
	public TextSocketSender(Socket socket) {
		super(new SocketClient(socket));
	}

	public  String syncSend(String message) {
		System.out.println("XML STRING : \n" + message);
		basicSend(message);
		return receive();
	}

	@Override
	public void send(String message) {
		try {
			basicSend(message);
		}finally{
			if(!isDurable) socketClient.close();	
		}

	}
	
	public void basicSend(String message) {
		BufferedWriter bufferedWriter = socketClient.getBufferedWriter();
		try {
			bufferedWriter.write(message);
			bufferedWriter.flush();
		} catch (IOException e) {
			throw new ServiceInvocationException("Unable to send message to a socket due to " + e.getMessage() );
		}
	}

	
	protected String receive() {
		
		TextSocketReceiver reader = new TextSocketReceiver(socketClient,null);
		return reader.readSocket();
		
	}
	

}
