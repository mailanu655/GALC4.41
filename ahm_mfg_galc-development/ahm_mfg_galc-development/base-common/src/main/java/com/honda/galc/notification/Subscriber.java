package com.honda.galc.notification;

import com.honda.galc.net.TcpEndpoint;


public class Subscriber extends TcpEndpoint implements ISubscriber{
     
    public Subscriber(String name, String ip,int port) {
        super(name,ip,port);
    }
    
    public Subscriber(String ip,int port) {
        this(null,ip,port);
    }    
        
    public String getClientHostName() {
        return getIp();
    }


    public int getClientPort() {
        return getPort();
    }
    
    public String toString() {
        return "Subscriber(" + super.toString() + ")";
    }

 
}
