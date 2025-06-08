package com.honda.galc.notification.service;

import com.honda.galc.entity.conf.ComponentProperty;



public interface IPropertyNotification extends INotificationService{
    
    public void execute(ComponentProperty componentProperty);

}
