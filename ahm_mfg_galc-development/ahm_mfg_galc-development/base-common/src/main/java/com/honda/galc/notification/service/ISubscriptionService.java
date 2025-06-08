package com.honda.galc.notification.service;

import com.honda.galc.service.IService;

public interface ISubscriptionService extends IService{
    public boolean subscribe(String producerName,String clientIp,int clientPort, String provider, String clientName);
    public boolean unsubscribe(String producerName,String clientIp,int clientPort);
    public boolean unsubscribe(String clientHostName,int clientPort);
    public boolean heartbeat(String producerName,String clientHostName,int clientPort);
}
