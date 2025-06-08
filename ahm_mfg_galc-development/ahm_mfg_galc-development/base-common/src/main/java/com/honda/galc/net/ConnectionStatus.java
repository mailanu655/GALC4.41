package com.honda.galc.net;

public class ConnectionStatus {
	String connectionId;
	Endpoint endpoint;
	Status status = Status.DISCONNECTED;
	
	
	public ConnectionStatus(String connectionId,boolean isConnected) {
		this.connectionId = connectionId;
		status = isConnected ? Status.CONNECTED : Status.DISCONNECTED;
	}

	public Endpoint getEndpoint() {
		return endpoint;
	}


	public void setEndpoint(Endpoint endpoint) {
		this.endpoint = endpoint;
	}


	public String getConnectionId() {
		return connectionId;
	}


	public void setConnectionId(String id) {
		this.connectionId = id;
	}


	public Status getStatus() {
		return status;
	}


	public void setStatusString(Status status) {
		this.status = status;
	}
	
	public boolean isConnected() {
		return Status.CONNECTED.equals(status); 
	}
	
	public enum Status{
		CONNECTED,DISCONNECTED;
	}
	
	public boolean equals(Object object) {
		if(object instanceof String) return connectionId.equals(object);
		else if(object instanceof ConnectionStatus) 
			return ((ConnectionStatus) object).getConnectionId().equals(connectionId);
		else return false;
	}
	

}


