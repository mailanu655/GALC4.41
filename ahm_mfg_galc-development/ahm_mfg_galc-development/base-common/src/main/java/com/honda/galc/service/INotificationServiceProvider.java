package com.honda.galc.service;

import com.honda.galc.notification.service.INotificationService;

public interface INotificationServiceProvider {
    public  <T extends INotificationService> T getNotificationService(Class<T> serviceClass);
    public  <T extends INotificationService> T getNotificationService(Class<T> serviceClass, String producerPP);
}
