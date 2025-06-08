package com.honda.galc.notification;

public interface ISubscriber {
    public boolean isHost(String clientHostName,int clientPort);
    public String getClientHostName();
    public int getClientPort();
}
