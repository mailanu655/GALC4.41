package com.honda.galc.net;

public class TcpEndpoint extends AbstractEndpoint{
    private String name;
    private String ip;
    int port;
    
    public TcpEndpoint(String ip,int port) {
        this(null,ip,port);
    }
    
    public TcpEndpoint(String name, String ip,int port) {
        this.name = name;
        this.ip = ip;
        this.port = port;
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public int getPort() {
        return port;
    }
    public void setPort(int port) {
        this.port = port;
    }
    
    public String toString() {
        return name == null? ip + ":" + port : name + "-" + ip + ":" + port;
    }
    
    public boolean isHost(String ip, int port) {
        return this.ip.equals(ip) && this.port == port;
    }

	public String name() {
		return ip + ":" + port;
	}

	public boolean connect() {
		SocketRequestInvoker requestInvoker= new SocketRequestInvoker(this);
		try{
			requestInvoker.initConnection();
		}catch(Exception e) {
			return false;
		}
		return true;
	}
    
    
}
