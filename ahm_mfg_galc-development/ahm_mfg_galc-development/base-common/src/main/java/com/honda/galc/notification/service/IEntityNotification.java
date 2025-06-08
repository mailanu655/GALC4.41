package com.honda.galc.notification.service;

import com.honda.galc.entity.IEntity;

public interface IEntityNotification {
    public void updated(IEntity entity);
    public void inserted(IEntity entity);
    public void deleted(IEntity entity);
}
