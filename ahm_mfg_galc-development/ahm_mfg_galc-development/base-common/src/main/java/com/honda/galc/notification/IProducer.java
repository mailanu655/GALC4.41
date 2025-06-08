package com.honda.galc.notification;

import java.util.List;

import com.honda.galc.net.NotificationRequest;

public interface IProducer {
    public String getName();
    public void subscribe(ISubscriber subscriber);
    public List<ISubscriber> getSubscribers();
    public void unsubscribe();
    public void publish(NotificationRequest request, ISubscriber subscriber);
    public boolean contains(String clientHostName, int clientPort);
}
