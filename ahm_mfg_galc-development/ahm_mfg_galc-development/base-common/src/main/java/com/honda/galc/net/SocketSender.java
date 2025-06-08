package com.honda.galc.net;

public abstract class SocketSender<T> {
	
	protected SocketClient socketClient;
	
	public SocketSender(SocketClient socketClient) {
		this.socketClient = socketClient;
	}
	
	
	protected abstract void send(T message);
	
	protected  T syncSend(T message) {
		send(message);
		return receive();
	}
	
	protected abstract T receive();
	
}
