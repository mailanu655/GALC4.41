package com.honda.galc.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.honda.galc.common.exception.ServiceTimeoutException;

public class SocketRequestInvoker extends AbstractRequestInvoker<Request>{
    
    private TcpEndpoint endPoint;
    private boolean isAlways = false;
    private SocketClient socket;
    
    private static int SOCKET_CONNECTION_TIMEOUT = 2000;
    //Socket read time out
    private static int SOCKET_TIMEOUT = 2000;
    
    public SocketRequestInvoker(String name,String ip,int port) {
        this.endPoint = new TcpEndpoint(name,ip,port);
    }
    public SocketRequestInvoker(String ip,int port) {
        this(null,ip,port);
    }
    
    public SocketRequestInvoker(TcpEndpoint endPoint) {
        this.endPoint = endPoint;
    }
    
    public SocketRequestInvoker(TcpEndpoint endPoint, boolean isAlways) {
        this.endPoint = endPoint;
        this.isAlways = isAlways;
        if(isAlways) createSocket();
    }
    
    public SocketRequestInvoker(String ip,int port, boolean isAlways) {
       this(null,ip,port,isAlways);
    }
    
    public SocketRequestInvoker(String name,String ip,int port, boolean isAlways) {
        this(new TcpEndpoint(name,ip,port),isAlways);
    }
    
    
    public void notify(Request notification) {
        if(socket == null) createSocket();
        
        try {
            send(notification);
        }finally{
            if(!isAlways) close();
        }
        
    }
    
    private void createSocket() throws ServiceTimeoutException{

    	socket = new SocketClient(endPoint.getIp(),endPoint.getPort(),
            		SOCKET_CONNECTION_TIMEOUT,SOCKET_TIMEOUT);
    	
    }
    
    @Override
    public String getServerAddress() {
        return endPoint.toString();
    }
    
    @Override
    public ObjectInputStream getInputStream() throws IOException{
        return new ObjectInputStream(socket.getInputStream());
    }
    
    @Override
    public ObjectOutputStream getOutputStream() throws IOException {
        return new ObjectOutputStream(socket.getOutputStream());
    }
    @Override
    public void initConnection() {
        createSocket();
    }
}
